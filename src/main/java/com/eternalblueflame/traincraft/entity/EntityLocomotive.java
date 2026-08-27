package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.inventory.LocomotiveInventory;
import com.eternalblueflame.traincraft.locomotive.LocomotiveDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.Vec3;
import com.eternalblueflame.traincraft.menu.LocomotiveMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;

public abstract class EntityLocomotive extends EntityRollingStock {
    private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(
            EntityLocomotive.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(
            EntityLocomotive.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(
            EntityLocomotive.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> OVERHEAT_LEVEL = SynchedEntityData.defineId(
            EntityLocomotive.class, EntityDataSerializers.INT);

    /** The legacy 4-4-0's 50 km/h profile maps to the current safe 0.4 B/t cap. */
    private static final double REFERENCE_TOP_SPEED_KPH = 50.0D;
    private static final double REFERENCE_MAX_SPEED_BLOCKS_PER_TICK = 0.4D;

    /** How much throttle changes per tick while a key is held (~2.5s 0 to 100% at 0.02). */
    private static final float THROTTLE_STEP = 0.02F;

    /**
     * After throttle hits 0 while a direction key is still held, ignore further
     * input until the key is released so you cannot cross through 0 into reverse/forward.
     */
    private boolean throttleNeutralLatch = false;

    private boolean inventoryDropped = false;

    private final LocomotiveInventory inventory;

    protected EntityLocomotive(EntityType<? extends EntityLocomotive> entityType, Level level) {
        super(entityType, level);
        this.inventory = new LocomotiveInventory(inventorySize(), () -> {});
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENGINE_ON, true);
        builder.define(THROTTLE, 0.0F);
        // A newly placed locomotive starts cold and unstocked, like a furnace.
        builder.define(FUEL, 0);
        builder.define(OVERHEAT_LEVEL, 0);
    }

    /** Number of working slots: fuel only by default, steam adds a water slot. */
    protected int inventorySize() {
        return 1;
    }

    /** Immutable system definition for this locomotive type. */
    public abstract LocomotiveDefinition getDefinition();

    public LocomotiveInventory getInventory() {
        return inventory;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (!level().isClientSide()) {
                setEngineOn(!isEngineOn());
                player.displayClientMessage(
                        Component.literal(isEngineOn() ? "Locomotive started" : "Locomotive stopped"),
                        true);
            }
            return InteractionResult.sidedSuccess(level().isClientSide());
        }
        return super.interact(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            return;
        }

        Player rider = getFirstPassenger() instanceof Player player ? player : null;
        float input = rider == null ? 0.0F : Mth.clamp(rider.zza, -1.0F, 1.0F);
        updateThrottleLever(input);

        if (!isFuelled()) {
            pullFuelFromInventory();
        }

        float throttle = getThrottle();

        if (!isEngineOn() || throttle == 0.0F || !isFuelled()) {
            setDeltaMovement(getDeltaMovement().scale(getBrakeFactor()));
        } else {
            float yaw = getYRot() * ((float) Math.PI / 180.0F);
            Vec3 forward = new Vec3(Math.cos(yaw), 0.0D, Math.sin(yaw));
            double direction = throttle > 0.0F ? 1.0D : -1.0D;
            double force = getSpecAccel() * 0.01D * Math.abs(throttle) * direction;
            Vec3 movement = getDeltaMovement().add(forward.scale(force));
            double horizontalSpeed = Math.sqrt(movement.x * movement.x + movement.z * movement.z);
            double maximumSpeed = getLocomotiveMaxSpeed();
            if (horizontalSpeed > maximumSpeed) {
                double scale = maximumSpeed / horizontalSpeed;
                movement = new Vec3(movement.x * scale, movement.y, movement.z * scale);
            }
            setDeltaMovement(movement);

            consumeFuel();
        }

        updateOverheat();
    }

    /** Stokes the firebox according to this locomotive's fuel-system definition. */
    private void pullFuelFromInventory() {
        var fuelConfiguration = getDefinition().getFuelConfiguration();
        ItemStack fuelSlot = inventory.getItem(fuelConfiguration.fuelSlot());
        var fuelSystem = getDefinition().getFuelSystem();

        if (!fuelSlot.isEmpty() && fuelSystem.acceptsFuel(fuelSlot)) {
            // Read the fuel value before shrinking. A one-item stack becomes
            // empty after shrink(), at which point a fuel registry correctly
            // reports zero burn time.
            int fuelAmount = fuelSystem.getFuelAmount(fuelSlot);
            if (fuelAmount <= 0) {
                return;
            }
            fuelSlot.shrink(1);
            inventory.setChanged();
            addFuel(fuelAmount);
        }
    }

    /**
     * Lever-style throttle:
     * - Hold W: increase toward +1 (or toward 0 if currently in reverse)
     * - Hold S: decrease toward -1 (or toward 0 if currently in forward)
     * - Release: hold current value
     * - Crossing 0 requires release then press the new direction
     */
    private void updateThrottleLever(float input) {
        float throttle = getThrottle();

        if (input == 0.0F) {
            throttleNeutralLatch = false;
            return;
        }

        if (throttleNeutralLatch) {
            return;
        }

        if (input > 0.0F) {
            if (throttle < 0.0F) {
                throttle = Math.min(0.0F, throttle + THROTTLE_STEP);
                if (throttle == 0.0F) {
                    throttleNeutralLatch = true;
                }
            } else {
                throttle = Math.min(1.0F, throttle + THROTTLE_STEP);
            }
        } else {
            if (throttle > 0.0F) {
                throttle = Math.max(0.0F, throttle - THROTTLE_STEP);
                if (throttle == 0.0F) {
                    throttleNeutralLatch = true;
                }
            } else {
                throttle = Math.max(-1.0F, throttle - THROTTLE_STEP);
            }
        }

        setThrottle(throttle);
    }

    public boolean isEngineOn() {
        return entityData.get(ENGINE_ON);
    }

    public void setEngineOn(boolean engineOn) {
        entityData.set(ENGINE_ON, engineOn);
    }

    public float getThrottle() {
        return entityData.get(THROTTLE);
    }

    private void setThrottle(float throttle) {
        entityData.set(THROTTLE, throttle);
    }

    /**
     * Legacy Traincraft locomotion parameters.
     *
     * Each concrete locomotive overrides these values, while this shared
     * EntityLocomotive class applies them to rail movement and braking.
     */
    public abstract double transportTopSpeed();

    public abstract double transportMetricHorsePower();

    public abstract double getSpecAccel();

    public abstract double getSpecBrake();

    /** Top speed after the legacy horsepower-versus-consist-load calculation. */
    public double getTopSpeedKph() {
        double pulledMass = getPulledMass();
        if (pulledMass <= 0.0D) {
            return transportTopSpeed();
        }

        double powerLoad = pulledMass / (transportMetricHorsePower() * 0.37D);
        return powerLoad > 1.0D
                ? transportTopSpeed() / powerLoad
                : transportTopSpeed();
    }

    public double getMetricHorsepower() {
        return transportMetricHorsePower();
    }

    public double getBrakeFactor() {
        return getSpecBrake();
    }

    /**
     * Coupling will replace this with the combined mass of linked rolling
     * stock. Keeping it here makes the locomotion definition useful today
     * without inventing a temporary coupling system.
     */
    protected double getPulledMass() {
        return 0.0D;
    }

    protected double getLocomotiveMaxSpeed() {
        return getTopSpeedKph() / REFERENCE_TOP_SPEED_KPH
                * REFERENCE_MAX_SPEED_BLOCKS_PER_TICK;
    }

    // --- Fuel & heat state -------------------------------------------------

    public boolean isFuelled() {
        return getFuel() > 0;
    }

    public int getFuel() {
        return entityData.get(FUEL);
    }

    public int getFuelCapacity() {
        return getDefinition().getFuelConfiguration().fuelCapacity();
    }

    public void addFuel(int ticks) {
        setFuel(Math.min(getFuelCapacity(), getFuel() + ticks));
    }

    private void setFuel(int fuel) {
        entityData.set(FUEL, Math.max(0, fuel));
    }

    private void consumeFuel() {
        setFuel(getFuel() - 1);
    }

    /** Locomotives run hot by default; subclasses may opt out. */
    public boolean canOverheat() {
        return getOverheatTime() > 0;
    }

    /** Legacy Traincraft heating scale supplied by each locomotive type. */
    public abstract int getOverheatTime();

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        return position().add(0.0D, 1.0D, 1.1D);
    }

    /** The legacy gauge reserves 30 points above the nominal heat scale. */
    public int getOverheatMaximum() {
        return getOverheatTime() + 30;
    }

    /** Normal operating heat, matching legacy Traincraft's midpoint. */
    public int getAverageOverheat() {
        return getOverheatMaximum() / 2;
    }

    public int getOverheatLevel() {
        return entityData.get(OVERHEAT_LEVEL);
    }

    /** Subclasses return true when running without coolant (e.g. a dry steam loco). */
    protected boolean lacksCoolant() {
        return false;
    }

    /** Subclasses return true when their coolant level can cool an over-hot engine. */
    protected boolean hasEffectiveCoolant() {
        return false;
    }

    private void updateOverheat() {
        if (!canOverheat()) {
            return;
        }

        int level = getOverheatLevel();
        int normalHeat = getAverageOverheat();

        // A fired locomotive warms gradually to its normal operating level.
        if (isEngineOn() && isFuelled() && level < normalHeat && random.nextInt(7) == 0) {
            level++;
        }

        // An unfuelled or switched-off locomotive cools down naturally.
        if ((!isEngineOn() || !isFuelled()) && level > 0 && random.nextInt(10) == 0) {
            level--;
        }

        // Above normal operating heat, the boiler dissipates heat slowly.
        if (level > normalHeat && random.nextInt(30) == 0) {
            level--;
        }

        // A well-filled steam boiler has the same additional cooling effect
        // as the legacy steam implementation.
        if (hasEffectiveCoolant() && level > normalHeat) {
            level--;
        }

        // A fired dry boiler overheats rapidly.
        if (lacksCoolant() && random.nextInt(10) == 0) {
            level += 3;
        }

        entityData.set(OVERHEAT_LEVEL, Mth.clamp(level, 0, getOverheatMaximum()));
    }

    /** Human-readable operating state, shown on the HUD and in GUIs. */
    public String getState() {
        if (canOverheat()) {
            int heat = getOverheatLevel();
            int normal = getAverageOverheat();
            if (heat > normal + heatRange(24)) {
                return "Broken";
            }
            if (heat > normal + heatRange(17)) {
                return "Too hot";
            }
            if (heat > normal + heatRange(12)) {
                return "Very hot";
            }
            if (heat >= normal - heatRange(4)) {
                return "Hot";
            }
            if (heat >= normal - heatRange(17)) {
                return "Warm";
            }
            return "Cold";
        }
        if (!isEngineOn()) {
            return "Off";
        }
        if (!isFuelled()) {
            return "Out of fuel";
        }
        if (Math.abs(getThrottle()) > 0.01F) {
            return "Running";
        }
        return "Stopped";
    }

    private int heatRange(int gaugePixels) {
        return getOverheatTime() * gaugePixels / 50;
    }

    /** Fuel scaled to an arbitrary bar length for gauges. */
    public int getFuelDiv(int scale) {
        return getFuel() * scale / getFuelCapacity();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("EngineOn", isEngineOn());
        tag.putFloat("Throttle", getThrottle());
        tag.putInt("Fuel", getFuel());
        tag.putInt("Overheat", getOverheatLevel());
        tag.put("Inventory", inventory.save(new CompoundTag(), level().registryAccess()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setEngineOn(!tag.contains("EngineOn") || tag.getBoolean("EngineOn"));
        setThrottle(tag.getFloat("Throttle"));
        setFuel(tag.contains("Fuel") ? tag.getInt("Fuel") : 0);
        entityData.set(OVERHEAT_LEVEL, Mth.clamp(tag.getInt("Overheat"), 0, getOverheatMaximum()));
        if (tag.contains("Inventory")) {
            inventory.load(tag.getCompound("Inventory"), level().registryAccess());
        }
    }

    /** Open the locomotive working inventory (fuel / water). Called from the R-key packet. */
    public void openInventory(Player player) {
        if (level().isClientSide()) {
            return;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (player.getVehicle() != this) {
            return;
        }

        MenuProvider provider = new SimpleMenuProvider(
                (containerId, playerInventory, p) -> new LocomotiveMenu(containerId, playerInventory, this),
                Component.translatable("container.traincraft.loco"));
        serverPlayer.openMenu(provider);
    }

    /** Drops every stored stack before vanilla drops the locomotive item. */
    @Override
    public void destroy(DamageSource damageSource) {
        if (!level().isClientSide() && !inventoryDropped) {
            Containers.dropContents(level(), this, inventory);
            inventory.clearContent();
            inventoryDropped = true;
        }
        super.destroy(damageSource);
    }
}

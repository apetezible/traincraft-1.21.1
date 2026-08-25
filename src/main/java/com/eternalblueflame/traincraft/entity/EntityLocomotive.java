package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.inventory.LocomotiveInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

    protected double accelerate = 0.65D;
    protected double brake = 0.95D;

    /** Burn time added per coal item, matching vanilla furnace values. */
    public static final int FUEL_PER_COAL = 1600;

    /** Full bunker of fuel in ticks (~20 minutes at one tick per running tick). */
    public static final int MAX_FUEL_TICKS = 24000;

    /** Ticks of overheating before the locomotive reaches critical temperature. */
    public static final int OVERHEAT_TIME = 60;

    /** Hard cap for the overheat gauge so it stops just past the red line. */
    public static final int OVERHEAT_MAX = OVERHEAT_TIME + 30;

    /** How much throttle changes per tick while a key is held (~2.5s 0 to 100% at 0.02). */
    private static final float THROTTLE_STEP = 0.02F;

    /**
     * After throttle hits 0 while a direction key is still held, ignore further
     * input until the key is released so you cannot cross through 0 into reverse/forward.
     */
    private boolean throttleNeutralLatch = false;

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
        builder.define(FUEL, MAX_FUEL_TICKS);
        builder.define(OVERHEAT_LEVEL, 0);
    }

    /** Number of working slots: fuel only by default, steam adds a water slot. */
    protected int inventorySize() {
        return 1;
    }

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
            setDeltaMovement(getDeltaMovement().scale(brake));
            coolDownOverheat();
            return;
        }

        float yaw = getYRot() * ((float) Math.PI / 180.0F);
        Vec3 forward = new Vec3(Math.cos(yaw), 0.0D, Math.sin(yaw));
        double direction = throttle > 0.0F ? 1.0D : -1.0D;
        double force = accelerate * 0.01D * Math.abs(throttle) * direction;
        Vec3 movement = getDeltaMovement().add(forward.scale(force));
        double horizontalSpeed = Math.sqrt(movement.x * movement.x + movement.z * movement.z);
        if (horizontalSpeed > getLocomotiveMaxSpeed()) {
            double scale = getLocomotiveMaxSpeed() / horizontalSpeed;
            movement = new Vec3(movement.x * scale, movement.y, movement.z * scale);
        }
        setDeltaMovement(movement);

        consumeFuel();
        updateOverheat();
    }

    /** Furnace-style stoking: pull the next coal from the firebox slot when empty. */
    private void pullFuelFromInventory() {
        ItemStack fuelSlot = inventory.getItem(0);
        if (!fuelSlot.isEmpty() && fuelSlot.is(ItemTags.COALS)) {
            fuelSlot.shrink(1);
            addFuel(FUEL_PER_COAL);
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

    protected double getLocomotiveMaxSpeed() {
        return 0.4D;
    }

    // --- Fuel & heat state -------------------------------------------------

    public boolean isFuelled() {
        return getFuel() > 0;
    }

    public int getFuel() {
        return entityData.get(FUEL);
    }

    public void addFuel(int ticks) {
        setFuel(Math.min(MAX_FUEL_TICKS, getFuel() + ticks));
    }

    private void setFuel(int fuel) {
        entityData.set(FUEL, Math.max(0, fuel));
    }

    private void consumeFuel() {
        setFuel(getFuel() - 1);
    }

    /** Locomotives run hot by default; subclasses may opt out. */
    public boolean canOverheat() {
        return true;
    }

    public int getOverheatLevel() {
        return entityData.get(OVERHEAT_LEVEL);
    }

    /** Subclasses return true when running without coolant (e.g. a dry steam loco). */
    protected boolean lacksCoolant() {
        return false;
    }

    private void updateOverheat() {
        int level = getOverheatLevel();
        level = lacksCoolant() ? level + 1 : level - 1;
        entityData.set(OVERHEAT_LEVEL, Mth.clamp(level, 0, OVERHEAT_MAX));
    }

    private void coolDownOverheat() {
        entityData.set(OVERHEAT_LEVEL, Math.max(0, getOverheatLevel() - 1));
    }

    /** Human-readable operating state, shown on the HUD and in GUIs. */
    public String getState() {
        if (canOverheat() && getOverheatLevel() >= OVERHEAT_TIME) {
            return "Overheated";
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

    /** Fuel scaled to an arbitrary bar length for gauges. */
    public int getFuelDiv(int scale) {
        return getFuel() * scale / MAX_FUEL_TICKS;
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
        setFuel(tag.contains("Fuel") ? tag.getInt("Fuel") : MAX_FUEL_TICKS);
        entityData.set(OVERHEAT_LEVEL, Mth.clamp(tag.getInt("Overheat"), 0, OVERHEAT_MAX));
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
}

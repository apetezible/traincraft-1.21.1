package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import com.eternalblueflame.traincraft.locomotive.LocomotiveDefinition;
import com.eternalblueflame.traincraft.locomotive.effects.ParticleEmissionDefinition;
import com.eternalblueflame.traincraft.locomotive.effects.ParticleProfile;
import com.eternalblueflame.traincraft.locomotive.fuel.FuelSystemConfiguration;
import com.eternalblueflame.traincraft.locomotive.fuel.SolidFuelSystem;
import com.eternalblueflame.traincraft.locomotive.propulsion.SteamPropulsionDefinition;
import com.eternalblueflame.traincraft.locomotive.sound.LocomotiveSoundProfile;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EntityLocoSteam4_4_0 extends EntityLocomotive {
    private static final LocomotiveDefinition DEFINITION = new LocomotiveDefinition(
            "steam_4_4_0",
            SteamPropulsionDefinition.INSTANCE,
            new SolidFuelSystem(),
            new FuelSystemConfiguration(20_000, 5_000, 0, 1),
            new ParticleProfile(List.of(
                    new ParticleEmissionDefinition(
                            ParticleEmissionDefinition.Type.LARGE_SMOKE,
                            3, 1.0D, 2.6D, 0.0D)
            )),
            new LocomotiveSoundProfile(
                    sound("american_steam_horn"), 0.8F,
                    sound("bell"), 0.5F,
                    sound("steam_run"), 0.2F, 0.001F,
                    sound("steam_run"), 0.2F, 0.4F)
    );

    private static final EntityDataAccessor<Integer> WATER = SynchedEntityData.defineId(
            EntityLocoSteam4_4_0.class, EntityDataSerializers.INT);

    /** Boiler water capacity in milli-buckets, matching the legacy 4-4-0. */
    public static final int WATER_CAPACITY = DEFINITION.getFuelConfiguration().waterCapacity();

    /** Water added per drained water bucket. */
    public static final int WATER_PER_BUCKET = 1000;

    /** Boiler drinks 1 mB every N ticks while under way (full tank ~13 min). */
    private static final int WATER_DRAIN_INTERVAL = 8;

    private int waterDrainCounter = 0;

    public EntityLocoSteam4_4_0(EntityType<? extends EntityLocoSteam4_4_0> entityType, Level level) {
        super(entityType, level);
    }

    private static ResourceLocation sound(String path) {
        return ResourceLocation.fromNamespaceAndPath("traincraft", path);
    }

    @Override
    public LocomotiveDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    public double transportTopSpeed() {
        return 50.0D;
    }

    @Override
    public double transportMetricHorsePower() {
        return 400.0D;
    }

    @Override
    public double getSpecAccel() {
        return 0.65D;
    }

    @Override
    public double getSpecBrake() {
        return 0.95D;
    }

    @Override
    public int getOverheatTime() {
        return 190;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        // The boiler must be filled by the player; it does not spawn full.
        builder.define(WATER, 0);
    }

    @Override
    protected int inventorySize() {
        return 11; // fuel, water containers, then the 3 x 3 cargo grid
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            return;
        }
        drainWaterBucket();
        consumeBoilerWater();
    }

    /** Pours any water bucket sitting in the liquid slot into the boiler. */
    private void drainWaterBucket() {
        var inventory = getInventory();
        int waterSlot = getDefinition().getFuelConfiguration().waterSlot();
        ItemStack slot = inventory.getItem(waterSlot);
        if (!slot.is(Items.WATER_BUCKET)) {
            return;
        }
        if (getWater() >= WATER_CAPACITY) {
            return;
        }
        setWater(Math.min(WATER_CAPACITY, getWater() + WATER_PER_BUCKET));
        inventory.setItem(waterSlot, new ItemStack(Items.BUCKET));
    }

    private void consumeBoilerWater() {
        if (!isEngineOn() || Math.abs(getThrottle()) <= 0.01F) {
            return;
        }
        waterDrainCounter++;
        if (waterDrainCounter >= WATER_DRAIN_INTERVAL) {
            waterDrainCounter = 0;
            setWater(Math.max(0, getWater() - 1));
        }
    }

    public int getWater() {
        return entityData.get(WATER);
    }

    private void setWater(int water) {
        entityData.set(WATER, water);
    }

    @Override
    protected boolean lacksCoolant() {
        // Legacy behaviour: a fired boiler running dry builds heat.
        return isEngineOn() && isFuelled() && getWater() <= 1;
    }

    @Override
    protected boolean hasEffectiveCoolant() {
        return getWater() > WATER_CAPACITY / 2;
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.LOCO_STEAM_4_4_0;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Water", getWater());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(WATER, tag.contains("Water") ? tag.getInt("Water") : 0);
    }
}

package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
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
    private static final EntityDataAccessor<Integer> WATER = SynchedEntityData.defineId(
            EntityLocoSteam4_4_0.class, EntityDataSerializers.INT);

    /** Boiler water capacity in milli-buckets, mirroring the legacy tender tank scale. */
    public static final int WATER_CAPACITY = 2000;

    /** Water added per drained water bucket. */
    public static final int WATER_PER_BUCKET = 1000;

    /** Boiler drinks 1 mB every N ticks while under way (full tank ~13 min). */
    private static final int WATER_DRAIN_INTERVAL = 8;

    private int waterDrainCounter = 0;

    public EntityLocoSteam4_4_0(EntityType<? extends EntityLocoSteam4_4_0> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WATER, WATER_CAPACITY);
    }

    @Override
    protected int inventorySize() {
        return 2; // slot 0: fuel, slot 1: water containers
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
        ItemStack slot = inventory.getItem(1);
        if (!slot.is(Items.WATER_BUCKET)) {
            return;
        }
        if (getWater() >= WATER_CAPACITY) {
            return;
        }
        setWater(Math.min(WATER_CAPACITY, getWater() + WATER_PER_BUCKET));
        inventory.setItem(1, new ItemStack(Items.BUCKET));
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
        entityData.set(WATER, tag.contains("Water") ? tag.getInt("Water") : WATER_CAPACITY);
    }
}

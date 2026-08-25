package com.eternalblueflame.traincraft.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

public abstract class EntityLocomotive extends EntityRollingStock {
    private static final EntityDataAccessor<Boolean> ENGINE_ON = SynchedEntityData.defineId(
            EntityLocomotive.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> THROTTLE = SynchedEntityData.defineId(
            EntityLocomotive.class, EntityDataSerializers.FLOAT);

    protected double accelerate = 0.65D;
    protected double brake = 0.95D;

    protected EntityLocomotive(EntityType<? extends EntityLocomotive> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENGINE_ON, true);
        builder.define(THROTTLE, 0.0F);
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

    /** How much throttle changes per tick while a key is held (~2.5s 0→100% at 0.02). */
    private static final float THROTTLE_STEP = 0.02F;

    /**
     * After throttle hits 0 while a direction key is still held, ignore further
     * input until the key is released so you cannot cross through 0 into reverse/forward.
     */
    private boolean throttleNeutralLatch = false;



    @Override
public void tick() {
    super.tick();
        if (level().isClientSide()) {
            return;
        }

        Player rider = getFirstPassenger() instanceof Player player ? player : null;
        float input = rider == null ? 0.0F : Mth.clamp(rider.zza, -1.0F, 1.0F);
        updateThrottleLever(input);

        float throttle = getThrottle();

        if (!isEngineOn() || throttle == 0.0F) {
            setDeltaMovement(getDeltaMovement().scale(brake));
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

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("EngineOn", isEngineOn());
        tag.putFloat("Throttle", getThrottle());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setEngineOn(!tag.contains("EngineOn") || tag.getBoolean("EngineOn"));
        setThrottle(tag.getFloat("Throttle"));
    }
}
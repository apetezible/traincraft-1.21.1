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
                player.displayClientMessage(Component.literal(isEngineOn() ? "Locomotive started" : "Locomotive stopped"), true);
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

        updateFacingFromMovement();

        Player rider = getFirstPassenger() instanceof Player player ? player : null;
        float riderThrottle = rider == null ? 0.0F : Mth.clamp(rider.zza, -1.0F, 1.0F);
        setThrottle(riderThrottle);

        if (!isEngineOn() || riderThrottle == 0.0F) {
            setThrottle(0.0F);
            setDeltaMovement(getDeltaMovement().scale(brake));
            return;
        }

        float yaw = getYRot() * ((float) Math.PI / 180.0F);
        Vec3 forward = new Vec3(Math.cos(yaw), 0.0D, -Math.sin(yaw));
        double direction = riderThrottle > 0.0F ? 1.0D : -1.0D;
        double force = accelerate * 0.01D * Math.abs(riderThrottle) * direction;
        Vec3 movement = getDeltaMovement().add(forward.scale(force));
        double horizontalSpeed = Math.sqrt(movement.x * movement.x + movement.z * movement.z);
        if (horizontalSpeed > getLocomotiveMaxSpeed()) {
            double scale = getLocomotiveMaxSpeed() / horizontalSpeed;
            movement = new Vec3(movement.x * scale, movement.y, movement.z * scale);
        }
        setDeltaMovement(movement);
    }

    private void updateFacingFromMovement() {
        Vec3 movement = getDeltaMovement();
        if (movement.horizontalDistanceSqr() < 0.000001D) {
            return;
        }

        setYRot(Mth.wrapDegrees((float) Math.toDegrees(Math.atan2(-movement.z, movement.x))));
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

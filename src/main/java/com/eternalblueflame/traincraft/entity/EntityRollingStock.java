package com.eternalblueflame.traincraft.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class EntityRollingStock extends Minecart {
    protected EntityRollingStock(EntityType<? extends EntityRollingStock> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected abstract Item getDropItem();

    /** Rolling stock is not shoved around by players/mobs — it shoves them. */
    @Override
    public boolean isPushable() {
        return false;
    }

    /**
     * Traincraft-style: do not run vanilla mutual push (that reverses carts on curves).
     * Instead, if we are moving, push the other entity along our path and optionally hurt it.
     */
    @Override
    public void push(Entity other) {
        if (level().isClientSide() || other == this || other.isPassenger() || isPassenger()) {
            return;
        }
        // Other rolling stock: ignore for now (coupling later).
        if (other instanceof EntityRollingStock) {
            return;
        }

        Vec3 motion = getDeltaMovement();
        double speedSq = motion.horizontalDistanceSqr();
        if (speedSq < 0.0001D) {
            return; // stationary — do nothing (no mutual shove)
        }

        double speed = Math.sqrt(speedSq);
        Vec3 dir = new Vec3(motion.x / speed, 0.0D, motion.z / speed);

        // Push the other entity in our travel direction (not a symmetric separation).
        double strength = Math.min(speed * 2.5D, 1.8D);
        other.push(dir.x * strength, 0.15D, dir.z * strength);
        other.hurtMarked = true;

        // Speed-based damage (tune thresholds later).
        if (speed > 0.2D && other instanceof LivingEntity living) {
            float damage = (float) ((speed - 0.15D) * 12.0D);
            if (damage > 0.0F) {
                living.hurt(trainHitDamage(), damage);
            }
        }
    }

    /** Damage source attributed to this rolling stock. */
    protected DamageSource trainHitDamage() {
        return damageSources().source(
                net.minecraft.world.damagesource.DamageTypes.MOB_ATTACK, this);
    }
}

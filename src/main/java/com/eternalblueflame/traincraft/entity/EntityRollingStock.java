package com.eternalblueflame.traincraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public abstract class EntityRollingStock extends Minecart {
    protected EntityRollingStock(EntityType<? extends EntityRollingStock> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected abstract Item getDropItem();
}

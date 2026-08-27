package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.wagon.WagonDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityWagon extends EntityRollingStock {
    protected EntityWagon(EntityType<? extends EntityWagon> entityType, Level level) {
        super(entityType, level);
    }

    public abstract WagonDefinition getDefinition();
}

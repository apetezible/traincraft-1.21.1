package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class EntityLocoSteam4_4_0 extends EntityRollingStock {
    public EntityLocoSteam4_4_0(EntityType<? extends EntityLocoSteam4_4_0> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.LOCO_STEAM_4_4_0;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

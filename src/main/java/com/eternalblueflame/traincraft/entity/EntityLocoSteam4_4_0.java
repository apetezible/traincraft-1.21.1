package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftEntities;
import com.eternalblueflame.traincraft.TraincraftItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;

public class EntityLocoSteam4_4_0 extends Minecart {
    public EntityLocoSteam4_4_0(EntityType<? extends EntityLocoSteam4_4_0> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

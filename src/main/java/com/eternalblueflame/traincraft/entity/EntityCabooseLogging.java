package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import com.eternalblueflame.traincraft.wagon.WagonDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class EntityCabooseLogging extends EntityWagon {
    private static final WagonDefinition DEFINITION = new WagonDefinition(
            "caboose_logging",
            400.0F,
            0
    );

    public EntityCabooseLogging(EntityType<? extends EntityCabooseLogging> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public WagonDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.CABOOSE_LOGGING;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

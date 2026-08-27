package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import com.eternalblueflame.traincraft.wagon.WagonDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;

public class EntityFreightCartYellow extends EntityWagon {
    private static final WagonDefinition DEFINITION = new WagonDefinition(
            "freight_cart_yellow",
            6000.0F,
            4
    );

    public EntityFreightCartYellow(EntityType<? extends EntityFreightCartYellow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public WagonDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.FREIGHT_CART_YELLOW;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

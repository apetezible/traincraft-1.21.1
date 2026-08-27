package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import com.eternalblueflame.traincraft.wagon.WagonDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class EntityStockCar extends EntityWagon {
    private static final WagonDefinition DEFINITION = new WagonDefinition(
            "stock_car",
            4000.0F,
            0
    );

    public EntityStockCar(EntityType<? extends EntityStockCar> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public WagonDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.STOCK_CAR;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

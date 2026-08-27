package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import com.eternalblueflame.traincraft.wagon.WagonDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class EntityFlatCarLogsDB extends EntityWagon {
    private static final WagonDefinition DEFINITION = new WagonDefinition(
            "flat_car_logs_db",
            4000.0F,
            45
    );

    public EntityFlatCarLogsDB(EntityType<? extends EntityFlatCarLogsDB> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public WagonDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.FLAT_CAR_LOGS_DB;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}

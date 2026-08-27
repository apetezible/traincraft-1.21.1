package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.entity.EntityLocoSteam4_4_0;
import com.eternalblueflame.traincraft.entity.EntityFreightCartYellow;
import com.eternalblueflame.traincraft.entity.EntityPassengerCar1;
import com.eternalblueflame.traincraft.entity.EntityCabooseLogging;
import com.eternalblueflame.traincraft.entity.EntityStockCar;
import com.eternalblueflame.traincraft.entity.EntityFlatCarLogsDB;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.core.Registry;

public final class TraincraftEntities {
    public static final EntityType<EntityLocoSteam4_4_0> LOCO_STEAM_4_4_0 = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "loco_steam_4_4_0"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityLocoSteam4_4_0::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    public static final EntityType<EntityPassengerCar1> PASSENGER_CAR_1 = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "passenger_car_1"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityPassengerCar1::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    public static final EntityType<EntityFreightCartYellow> FREIGHT_CART_YELLOW = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "freight_cart_yellow"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityFreightCartYellow::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    public static final EntityType<EntityCabooseLogging> CABOOSE_LOGGING = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "caboose_logging"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityCabooseLogging::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    public static final EntityType<EntityStockCar> STOCK_CAR = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "stock_car"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityStockCar::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    public static final EntityType<EntityFlatCarLogsDB> FLAT_CAR_LOGS_DB = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "flat_car_logs_db"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, EntityFlatCarLogsDB::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(3)
                    .build()
    );

    private TraincraftEntities() {
    }

    public static void register() {
        Traincraft.LOGGER.info("Registering Traincraft entities");
    }
}

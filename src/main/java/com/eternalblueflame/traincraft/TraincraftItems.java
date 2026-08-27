package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.item.LocomotiveItem;
import com.eternalblueflame.traincraft.item.WagonItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class TraincraftItems {
    public static final Item LOCO_STEAM_4_4_0 = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "loco_steam_4_4_0"),
            new LocomotiveItem(new Item.Properties().stacksTo(1), TraincraftEntities.LOCO_STEAM_4_4_0)
    );

    public static final Item PASSENGER_CAR_1 = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "passenger_car_1"),
            new WagonItem(new Item.Properties().stacksTo(1), TraincraftEntities.PASSENGER_CAR_1)
    );

    public static final Item FREIGHT_CART_YELLOW = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "freight_cart_yellow"),
            new WagonItem(new Item.Properties().stacksTo(1), TraincraftEntities.FREIGHT_CART_YELLOW)
    );

    public static final Item CABOOSE_LOGGING = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "caboose_logging"),
            new WagonItem(new Item.Properties().stacksTo(1), TraincraftEntities.CABOOSE_LOGGING)
    );

    public static final Item STOCK_CAR = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "stock_car"),
            new WagonItem(new Item.Properties().stacksTo(1), TraincraftEntities.STOCK_CAR)
    );

    public static final Item FLAT_CAR_LOGS_DB = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "flat_car_logs_db"),
            new WagonItem(new Item.Properties().stacksTo(1), TraincraftEntities.FLAT_CAR_LOGS_DB)
    );

    private TraincraftItems() {
    }

    public static void register() {
        Traincraft.LOGGER.info("Registering Traincraft items");
    }
}

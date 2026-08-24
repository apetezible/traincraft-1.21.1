package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.item.LocomotiveItem;
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

    private TraincraftItems() {
    }

    public static void register() {
        Traincraft.LOGGER.info("Registering Traincraft items");
    }
}

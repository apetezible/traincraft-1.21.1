package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.menu.LocomotiveMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class TraincraftMenus {
    public static final MenuType<LocomotiveMenu> LOCO = Registry.register(
            BuiltInRegistries.MENU,
            ResourceLocation.fromNamespaceAndPath("traincraft", "loco"),
            new MenuType<>(LocomotiveMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private TraincraftMenus() {}

    /** Call from mod init so the class loads and registers. */
    public static void init() {
        // side-effect: static field registration
    }
}
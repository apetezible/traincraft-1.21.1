package com.eternalblueflame.traincraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/** Keybinds appear under Controls → Traincraft and are fully rebindable. */
public final class TraincraftKeybinds {
    public static KeyMapping OPEN_INVENTORY;

    private TraincraftKeybinds() {}

    public static void register() {
        OPEN_INVENTORY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.traincraft.inventory",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.traincraft"
        ));
    }
}
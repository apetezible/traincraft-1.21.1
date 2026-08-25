package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.client.TraincraftKeybinds;
import com.eternalblueflame.traincraft.client.screen.LocomotiveScreen;
import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.hud.HudLocomotive;
import com.eternalblueflame.traincraft.model.ModelLocoSteam4_4_0;
import com.eternalblueflame.traincraft.network.OpenLocoInventoryPayload;
import com.eternalblueflame.traincraft.render.RenderLocoSteam4_4_0;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;

public class TraincraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new HudLocomotive());
        EntityModelLayerRegistry.registerModelLayer(
                RenderLocoSteam4_4_0.MODEL_LAYER,
                ModelLocoSteam4_4_0::createBodyLayer);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.LOCO_STEAM_4_4_0,
                RenderLocoSteam4_4_0::new);

        TraincraftKeybinds.register();
        MenuScreens.register(TraincraftMenus.LOCO, LocomotiveScreen::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TraincraftKeybinds.OPEN_INVENTORY.consumeClick()) {
                if (client.player != null
                        && client.player.getVehicle() instanceof EntityLocomotive
                        && client.screen == null) {
                    ClientPlayNetworking.send(new OpenLocoInventoryPayload());
                }
            }
        });
    }
}
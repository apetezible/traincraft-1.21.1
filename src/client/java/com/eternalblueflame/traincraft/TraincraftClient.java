package com.eternalblueflame.traincraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MinecartRenderer;

public class TraincraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.LOCO_STEAM_4_4_0,
                context -> new MinecartRenderer<>(context, ModelLayers.MINECART)
        );
    }
}

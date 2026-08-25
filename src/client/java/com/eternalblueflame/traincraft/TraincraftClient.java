package com.eternalblueflame.traincraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import com.eternalblueflame.traincraft.model.ModelLocoSteam4_4_0;
import com.eternalblueflame.traincraft.render.RenderLocoSteam4_4_0;
import com.eternalblueflame.traincraft.hud.HudLocomotive;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class TraincraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new HudLocomotive());
        EntityModelLayerRegistry.registerModelLayer(
            RenderLocoSteam4_4_0.MODEL_LAYER,
            ModelLocoSteam4_4_0::createBodyLayer
        );
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.LOCO_STEAM_4_4_0,
            RenderLocoSteam4_4_0::new
        );
    }
}

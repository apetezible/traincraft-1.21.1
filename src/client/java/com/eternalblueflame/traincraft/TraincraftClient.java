package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.client.TraincraftKeybinds;
import com.eternalblueflame.traincraft.client.sound.LocomotiveSoundController;
import com.eternalblueflame.traincraft.client.screen.LocomotiveScreen;
import com.eternalblueflame.traincraft.entity.EntityCabooseLogging;
import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.entity.EntityStockCar;
import com.eternalblueflame.traincraft.entity.EntityFlatCarLogsDB;
import com.eternalblueflame.traincraft.hud.HudLocomotive;
import com.eternalblueflame.traincraft.model.ModelCabooseLogging;
import com.eternalblueflame.traincraft.model.ModelLocoSteam4_4_0;
import com.eternalblueflame.traincraft.model.ModelFreightCartYellow;
import com.eternalblueflame.traincraft.model.ModelPassengerCar1;
import com.eternalblueflame.traincraft.model.ModelStockCar;
import com.eternalblueflame.traincraft.model.ModelFlatCarLogsDB;
import com.eternalblueflame.traincraft.network.OpenLocoInventoryPayload;
import com.eternalblueflame.traincraft.render.RenderCabooseLogging;
import com.eternalblueflame.traincraft.render.RenderLocoSteam4_4_0;
import com.eternalblueflame.traincraft.render.RenderFreightCartYellow;
import com.eternalblueflame.traincraft.render.RenderPassengerCar1;
import com.eternalblueflame.traincraft.render.RenderStockCar;
import com.eternalblueflame.traincraft.render.RenderFlatCarLogsDB;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;

public class TraincraftClient implements ClientModInitializer {
    private final LocomotiveSoundController locomotiveSounds = new LocomotiveSoundController();
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new HudLocomotive());
        EntityModelLayerRegistry.registerModelLayer(
                RenderLocoSteam4_4_0.MODEL_LAYER,
                ModelLocoSteam4_4_0::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(
                RenderPassengerCar1.MODEL_LAYER,
                ModelPassengerCar1::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(
                RenderFreightCartYellow.MODEL_LAYER,
                ModelFreightCartYellow::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(
                RenderCabooseLogging.MODEL_LAYER,
                ModelCabooseLogging::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(
                RenderStockCar.MODEL_LAYER,
                ModelStockCar::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(
                RenderFlatCarLogsDB.MODEL_LAYER,
                ModelFlatCarLogsDB::createBodyLayer);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.LOCO_STEAM_4_4_0,
                RenderLocoSteam4_4_0::new);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.PASSENGER_CAR_1,
                RenderPassengerCar1::new);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.FREIGHT_CART_YELLOW,
                RenderFreightCartYellow::new);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.CABOOSE_LOGGING,
                RenderCabooseLogging::new);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.STOCK_CAR,
                RenderStockCar::new);
        EntityRendererRegistry.INSTANCE.register(
                TraincraftEntities.FLAT_CAR_LOGS_DB,
                RenderFlatCarLogsDB::new);

        TraincraftKeybinds.register();
        MenuScreens.register(TraincraftMenus.LOCO, LocomotiveScreen::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            locomotiveSounds.tick(client);
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

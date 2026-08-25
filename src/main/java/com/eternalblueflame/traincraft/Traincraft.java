package com.eternalblueflame.traincraft;

import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.network.OpenLocoInventoryPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Traincraft implements ModInitializer {
    public static final String MOD_ID = "traincraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        TraincraftEntities.register();
        TraincraftItems.register();
        TraincraftMenus.init();

        PayloadTypeRegistry.playC2S().register(
                OpenLocoInventoryPayload.ID,
                OpenLocoInventoryPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(OpenLocoInventoryPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                var player = context.player();
                if (player.getVehicle() instanceof EntityLocomotive loco) {
                    loco.openInventory(player);
                }
            });
        });

        LOGGER.info("Initializing Traincraft 1.21.1...");
    }
}
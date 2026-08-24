package com.eternalblueflame.traincraft;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Traincraft implements ModInitializer {
    public static final String MOD_ID = "traincraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Traincraft 1.21.1...");
    }
}
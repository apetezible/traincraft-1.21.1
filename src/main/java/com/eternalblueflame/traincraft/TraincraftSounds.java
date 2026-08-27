package com.eternalblueflame.traincraft;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/** Sound-event registrations shared by locomotive sound profiles. */
public final class TraincraftSounds {
    public static final SoundEvent AMERICAN_STEAM_HORN = register("american_steam_horn");
    public static final SoundEvent BELL = register("bell");
    public static final SoundEvent STEAM_RUN = register("steam_run");

    private TraincraftSounds() {
    }

    private static SoundEvent register(String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    /** Forces static sound-event registration during common initialization. */
    public static void register() {
    }
}

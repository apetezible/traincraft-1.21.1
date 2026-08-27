package com.eternalblueflame.traincraft.locomotive.sound;

import net.minecraft.resources.ResourceLocation;

/** Immutable sound profile for one locomotive type. */
public record LocomotiveSoundProfile(
        ResourceLocation horn,
        float hornVolume,
        ResourceLocation bell,
        float bellVolume,
        ResourceLocation idle,
        float idleVolume,
        float idlePitch,
        ResourceLocation running,
        float runningVolume,
        float runningPitch) {

    public LocomotiveSoundProfile {
        if (hornVolume < 0.0F || bellVolume < 0.0F || idleVolume < 0.0F || runningVolume < 0.0F) {
            throw new IllegalArgumentException("Sound volumes cannot be negative.");
        }
    }
}

package com.eternalblueflame.traincraft.locomotive.effects;

/** One model-relative particle emitter declared by a locomotive definition. */
public record ParticleEmissionDefinition(
        Type type,
        int density,
        double x,
        double y,
        double z) {

    public enum Type {
        LARGE_SMOKE
    }

    public ParticleEmissionDefinition {
        if (density <= 0) {
            throw new IllegalArgumentException("Particle density must be greater than zero.");
        }
    }
}

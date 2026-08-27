package com.eternalblueflame.traincraft.locomotive.effects;

import java.util.List;

/** Immutable set of visual emitters for a locomotive type. */
public record ParticleProfile(List<ParticleEmissionDefinition> emissions) {

    public ParticleProfile {
        emissions = List.copyOf(emissions);
    }
}

package com.eternalblueflame.traincraft.locomotive;

import com.eternalblueflame.traincraft.locomotive.fuel.FuelSystemConfiguration;
import com.eternalblueflame.traincraft.locomotive.fuel.FuelSystemDefinition;
import com.eternalblueflame.traincraft.locomotive.propulsion.PropulsionDefinition;

/**
 * Static definition of a locomotive.
 *
 * This describes what a locomotive is capable of and how its systems
 * are configured. Runtime state belongs to the locomotive entity.
 */
public final class LocomotiveDefinition {

    private final String id;

    private final PropulsionDefinition propulsion;

    private final FuelSystemDefinition fuelSystem;

    private final FuelSystemConfiguration fuelConfiguration;

    public LocomotiveDefinition(
            String id,
            PropulsionDefinition propulsion,
            FuelSystemDefinition fuelSystem,
            FuelSystemConfiguration fuelConfiguration
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "Locomotive id cannot be empty."
            );
        }

        if (propulsion == null) {
            throw new IllegalArgumentException(
                    "Locomotive must have a propulsion definition."
            );
        }

        if (fuelSystem == null) {
            throw new IllegalArgumentException(
                    "Locomotive must have a fuel system definition."
            );
        }

        if (fuelConfiguration == null) {
            throw new IllegalArgumentException(
                    "Locomotive must have a fuel system configuration."
            );
        }

        this.id = id;
        this.propulsion = propulsion;
        this.fuelSystem = fuelSystem;
        this.fuelConfiguration = fuelConfiguration;
    }

    public String getId() {
        return id;
    }

    public PropulsionDefinition getPropulsion() {
        return propulsion;
    }

    public FuelSystemDefinition getFuelSystem() {
        return fuelSystem;
    }

    public FuelSystemConfiguration getFuelConfiguration() {
        return fuelConfiguration;
    }
}
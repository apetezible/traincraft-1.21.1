package com.eternalblueflame.traincraft.locomotive.fuel;

/**
 * Configuration supplied by an individual locomotive when it uses
 * a particular fuel system.
 *
 * The fuel system defines what these values mean.
 * The locomotive defines the actual values.
 */
public record FuelSystemConfiguration(
        int fuelCapacity,
        int waterCapacity,
        int fuelSlot,
        int waterSlot) {

    public FuelSystemConfiguration {
        if (fuelCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Fuel capacity must be greater than zero."
            );
        }

        if (waterCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Water capacity must be greater than zero."
            );
        }

        if (fuelSlot < 0) {
            throw new IllegalArgumentException(
                    "Fuel slot cannot be negative."
            );
        }

        if (waterSlot < 0) {
            throw new IllegalArgumentException(
                    "Water slot cannot be negative."
            );
        }
        if (fuelSlot == waterSlot) {
            throw new IllegalArgumentException(
                    "Fuel slot and water slot must be different."
            );
            }
    }
}
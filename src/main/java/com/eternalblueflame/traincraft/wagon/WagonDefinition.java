package com.eternalblueflame.traincraft.wagon;

/**
 * Static definition of a wagon / rolling stock car.
 *
 * This keeps wagon-specific data separate from locomotives while still
 * sharing the same minecart-style runtime entity base.
 */
public final class WagonDefinition {
    private final String id;
    private final float weightKg;
    private final int inventoryRows;

    public WagonDefinition(String id, float weightKg, int inventoryRows) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Wagon id cannot be empty.");
        }
        this.id = id;
        this.weightKg = weightKg;
        this.inventoryRows = inventoryRows;
    }

    public String getId() {
        return id;
    }

    public float getWeightKg() {
        return weightKg;
    }

    public int getInventoryRows() {
        return inventoryRows;
    }
}

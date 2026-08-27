package com.eternalblueflame.traincraft.locomotive.propulsion;

/**
 * Reusable steam-boiler propulsion profile.
 *
 * The legacy 4-4-0 uses a heat scale of 190, with normal operating heat
 * centred at 110.  The values belong to the propulsion system, while fuel,
 * water, and movement values remain locomotive-specific configuration.
 */
public final class SteamPropulsionDefinition implements PropulsionDefinition {

    public static final SteamPropulsionDefinition INSTANCE =
            new SteamPropulsionDefinition();

    private static final double MINIMUM_HEAT = 0.0D;
    private static final double OPTIMAL_HEAT = 110.0D;
    private static final double MAXIMUM_HEAT = 190.0D;

    private SteamPropulsionDefinition() {
    }

    @Override
    public String getId() {
        return "steam";
    }

    @Override
    public boolean requiresHeat() {
        return true;
    }

    @Override
    public double getMinimumHeat() {
        return MINIMUM_HEAT;
    }

    @Override
    public double getOptimalHeat() {
        return OPTIMAL_HEAT;
    }

    @Override
    public double getMaximumHeat() {
        return MAXIMUM_HEAT;
    }

    @Override
    public double getEfficiency(double heat) {
        if (heat <= MINIMUM_HEAT || heat >= MAXIMUM_HEAT) {
            return 0.0D;
        }

        if (heat <= OPTIMAL_HEAT) {
            return heat / OPTIMAL_HEAT;
        }

        return (MAXIMUM_HEAT - heat) / (MAXIMUM_HEAT - OPTIMAL_HEAT);
    }
}

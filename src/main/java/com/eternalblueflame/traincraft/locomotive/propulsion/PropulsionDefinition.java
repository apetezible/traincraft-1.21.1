package com.eternalblueflame.traincraft.locomotive.propulsion;

/**
 * Defines the reusable propulsion characteristics of a locomotive.
 *
 * A propulsion definition describes how a locomotive generates movement
 * from its available resources. It does not contain the locomotive's
 * current runtime state.
 */
public interface PropulsionDefinition {

    /**
     * Returns the name/type of this propulsion system.
     */
    String getId();

    /**
     * Whether this propulsion system requires heat in order to operate.
     */
    boolean requiresHeat();

    /**
     * Minimum heat required for the locomotive to operate.
     *
     * Below this value the locomotive should not be considered operational.
     */
    double getMinimumHeat();

    /**
     * Heat level at which the propulsion system operates optimally.
     */
    double getOptimalHeat();

    /**
     * Maximum useful/safe heat level for this propulsion system.
     */
    double getMaximumHeat();

    /**
     * Calculates the efficiency of the propulsion system at a given heat level.
     *
     * The returned value is normalized:
     *
     * 0.0 = no propulsion efficiency
     * 1.0 = optimal efficiency
     *
     * The actual movement/energy consumption logic will be handled later.
     */
    double getEfficiency(double heat);
}

package com.eternalblueflame.traincraft.locomotive.fuel;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.ItemStack;

/**
 * Solid fuel system used by steam locomotives.
 *
 * Any item recognized by Minecraft's fuel registry is accepted.
 *
 * The system accepts normal furnace fuels that are consumed as items.
 * Container fuels (for example lava buckets) are deliberately excluded:
 * a steam firebox must not destroy the container or its contents.
 *
 * Solid fuel locomotives currently require water.
 */
public final class SolidFuelSystem implements FuelSystemDefinition {

    @Override
    public String getId() {
        return "solid_fuel";
    }

    @Override
    public boolean requiresWater() {
        return true;
    }

    @Override
    public boolean acceptsFuel(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }

        return !stack.getItem().hasCraftingRemainingItem()
                && FuelRegistry.INSTANCE.get(stack.getItem()) != null;
    }

    @Override
    public int getFuelAmount(ItemStack stack) {
        if (!acceptsFuel(stack)) {
            return 0;
        }

        Integer fuelTicks = FuelRegistry.INSTANCE.get(stack.getItem());

        return fuelTicks != null ? fuelTicks : 0;
    }
}

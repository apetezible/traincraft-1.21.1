package com.eternalblueflame.traincraft.locomotive.fuel;

import net.minecraft.world.item.ItemStack;

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
        return false;
    }

    @Override
    public int getFuelAmount(ItemStack stack) {
        return 0;
    }
}
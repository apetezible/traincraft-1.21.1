package com.eternalblueflame.traincraft.locomotive.fuel;

import net.minecraft.world.item.ItemStack;

public interface FuelSystemDefinition {

    String getId();

    boolean requiresWater();

    boolean acceptsFuel(ItemStack stack);

    int getFuelAmount(ItemStack stack);
}
package com.eternalblueflame.traincraft.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Working slots for a locomotive: 0 = fuel, steam adds 1 = water.
 */
public final class LocomotiveInventory implements Container {
    private final NonNullList<ItemStack> items;
    private final Runnable markDirty;

    public LocomotiveInventory(int size, Runnable markDirty) {
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
        this.markDirty = markDirty;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < items.size() ? items.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack slot = getItem(index);
        if (slot.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = slot.split(count);
        if (slot.isEmpty()) {
            items.set(index, ItemStack.EMPTY);
        }
        markDirty.run();
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index < 0 || index >= items.size()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = items.get(index);
        items.set(index, ItemStack.EMPTY);
        markDirty.run();
        return removed;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index < 0 || index >= items.size()) {
            return;
        }
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        markDirty.run();
    }

    @Override
    public void setChanged() {
        markDirty.run();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        items.clear();
        markDirty.run();
    }

    /** 1.21: ItemStack NBT needs a registry provider. */
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(tag, items, false, registries);
        return tag;
    }

    public void load(CompoundTag tag, HolderLookup.Provider registries) {
        items.clear();
        ContainerHelper.loadAllItems(tag, items, registries);
    }
}
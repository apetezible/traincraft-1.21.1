package com.eternalblueflame.traincraft.menu;

import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.entity.EntityLocoSteam4_4_0;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Container for the locomotive working slots, laid out like the legacy steam GUI. */
public class LocomotiveMenu extends AbstractContainerMenu {
    private final EntityLocomotive loco;

    /** Server-side construction with the real locomotive reference. */
    public LocomotiveMenu(int containerId, Inventory playerInventory, EntityLocomotive loco) {
        super(TraincraftMenus.LOCO, containerId);
        this.loco = loco;

        addTrainSlots();
        addPlayerSlots(playerInventory);
    }

    /** Client-side construction: resolve the locomotive from the entity id written by the server. */
    public LocomotiveMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, resolveLoco(playerInventory, buf));
    }

    private static EntityLocomotive resolveLoco(Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        Entity entity = playerInventory.player.level().getEntity(entityId);
        if (!(entity instanceof EntityLocomotive loco)) {
            throw new IllegalStateException("Unknown locomotive entity " + entityId);
        }
        return loco;
    }

    private void addTrainSlots() {
        Container inventory = loco.getInventory();
        addSlot(new Slot(inventory, 0, 8, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ItemTags.COALS);
            }
        });
        if (loco instanceof EntityLocoSteam4_4_0) {
            addSlot(new Slot(inventory, 1, 32, 53) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(Items.WATER_BUCKET);
                }
            });
        }
    }

    private void addPlayerSlots(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private int getTrainSlotCount() {
        return loco instanceof EntityLocoSteam4_4_0 ? 2 : 1;
    }

    public EntityLocomotive getLoco() {
        return loco;
    }

    @Override
    public boolean stillValid(Player player) {
        return loco.isAlive() && player.getVehicle() == loco;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        int trainSlots = getTrainSlotCount();
        int playerStart = trainSlots;

        if (index < trainSlots) {
            if (!moveItemStackTo(stack, playerStart, playerStart + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.is(ItemTags.COALS)) {
            if (!moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (loco instanceof EntityLocoSteam4_4_0 && stack.is(Items.WATER_BUCKET)) {
            if (!moveItemStackTo(stack, 1, 2, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        return original;
    }
}

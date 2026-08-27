package com.eternalblueflame.traincraft.menu;

import com.eternalblueflame.traincraft.TraincraftMenus;
import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Server/client container for a Traincraft locomotive.
 *
 * The slot coordinates mirror the legacy Traincraft locomotive GUI:
 *
 * Fuel:
 *   (8, 53)
 *
 * Water:
 *   (32, 53) - steam locomotives only
 *
 * Cargo:
 *   first row  -> y = 18
 *   second row -> y = 36
 *   third row  -> y = 54
 *
 * Player inventory:
 *   (8, 84) onward
 *
 * Hotbar:
 *   (8, 142) onward
 */
public class LocomotiveMenu extends AbstractContainerMenu {

    private final EntityLocomotive loco;

    /*
     * Number of locomotive-owned slots.
     *
     * This is deliberately obtained from the locomotive inventory rather
     * than hard-coded. When cargo storage is added to LocomotiveInventory,
     * the menu automatically exposes those slots.
     */
    private final int trainSlotCount;

    /**
     * Server-side constructor.
     */
    public LocomotiveMenu(
            int containerId,
            Inventory playerInventory,
            EntityLocomotive loco
    ) {
        super(TraincraftMenus.LOCO, containerId);

        this.loco = loco;
        this.trainSlotCount = loco.getInventory().getContainerSize();

        addTrainSlots();
        addPlayerSlots(playerInventory);
    }

    /**
     * Client-side constructor used by MenuType.
     *
     * The player must still be riding the locomotive when the menu is created.
     */
    public LocomotiveMenu(
            int containerId,
            Inventory playerInventory
    ) {
        this(
                containerId,
                playerInventory,
                resolveFromVehicle(playerInventory.player)
        );
    }

    /**
     * Resolve the locomotive that opened the menu.
     *
     * The current implementation opens the menu only while the player
     * is riding the locomotive, so this is sufficient for the current
     * foundation.
     */
    private static EntityLocomotive resolveFromVehicle(Player player) {
        if (player.getVehicle() instanceof EntityLocomotive loco) {
            return loco;
        }

        throw new IllegalStateException(
                "Player is not riding a locomotive"
        );
    }

    /**
     * Adds all locomotive-owned slots.
     *
     * Slot indices are determined by LocomotiveInventory:
     *
     * Steam locomotive:
     *   0 = fuel
     *   1 = water
     *   2+ = cargo
     *
     * Other locomotives:
     *   0 = fuel
     *   1+ = cargo
     */
    private void addTrainSlots() {
        Container inventory = loco.getInventory();

        /*
         * Fuel slot.
         */
        int fuelSlot = loco.getDefinition().getFuelConfiguration().fuelSlot();
        addSlot(new Slot(inventory, fuelSlot, 8, 53) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return loco.getDefinition().getFuelSystem().acceptsFuel(stack);
            }

        });

        /*
         * Steam locomotive water slot.
         */
        if (loco.getDefinition().getFuelSystem().requiresWater()) {
            int waterSlot = loco.getDefinition().getFuelConfiguration().waterSlot();

            addSlot(new Slot(inventory, waterSlot, 32, 53) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(Items.WATER_BUCKET);
                }

            });
        }

        /*
         * Cargo slots.
         *
     * The current steam panel exposes a three-by-three cargo grid.
         *
         * The actual number of cargo slots is determined by the
         * locomotive inventory size.
         */
        int cargoStart = loco.getDefinition().getFuelSystem().requiresWater() ? 2 : 1;

        int cargoIndex = cargoStart;

        for (int row = 0; row < 3; row++) {

            for (int column = 0; column < 3; column++) {

                if (cargoIndex >= trainSlotCount) {
                    return;
                }

                int x = 80 + column * 18;
                int y = 18 + row * 18;

                addSlot(new Slot(
                        inventory,
                        cargoIndex,
                        x,
                        y
                ));

                cargoIndex++;
            }
        }
    }

    /**
     * Adds the player's normal inventory.
     */
    private void addPlayerSlots(Inventory playerInventory) {

        /*
         * Main inventory: 3 rows × 9 columns.
         */
        for (int row = 0; row < 3; row++) {

            for (int column = 0; column < 9; column++) {

                addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        84 + row * 18
                ));
            }
        }

        /*
         * Hotbar: 9 slots.
         */
        for (int column = 0; column < 9; column++) {

            addSlot(new Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    142
            ));
        }
    }

    public EntityLocomotive getLoco() {
        return loco;
    }

    public int getTrainSlotCount() {
        return trainSlotCount;
    }

    /**
     * Number of slots belonging to the locomotive.
     *
     * This is important because the player's inventory starts immediately
     * after these slots in the AbstractContainerMenu slot list.
     */
    public int getPlayerInventoryStart() {
        return trainSlotCount;
    }

    /**
     * Number of slots belonging to the player.
     */
    public static final int PLAYER_INVENTORY_SLOT_COUNT = 36;

    @Override
    public boolean stillValid(Player player) {
        /*
         * The locomotive must still exist and the player must still
         * be riding this exact locomotive.
         */
        return loco.isAlive()
                && player.getVehicle() == loco;
    }

    /**
     * Shift-click transfer.
     *
     * Locomotive slots -> player inventory
     *
     * Player inventory -> appropriate locomotive slot.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        if (index < 0 || index >= slots.size()) {
            return ItemStack.EMPTY;
        }

        Slot slot = slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        int playerStart = trainSlotCount;
        int playerEnd = playerStart + PLAYER_INVENTORY_SLOT_COUNT;

        /*
         * Locomotive -> player.
         */
        if (index < playerStart) {

            if (!moveItemStackTo(
                    stack,
                    playerStart,
                    playerEnd,
                    true
            )) {
                return ItemStack.EMPTY;
            }
        }

        /*
         * Player -> locomotive.
         */
        else {

            /*
             * Fuel.
             */
            if (loco.getDefinition().getFuelSystem().acceptsFuel(stack)) {

                int fuelSlot = loco.getDefinition().getFuelConfiguration().fuelSlot();

                if (!moveItemStackTo(
                        stack,
                        fuelSlot,
                        fuelSlot + 1,
                        false
                )) {
                    return ItemStack.EMPTY;
                }
            }

            /*
             * Steam water.
             */
            else if (
                    loco.getDefinition().getFuelSystem().requiresWater()
                    && stack.is(Items.WATER_BUCKET)
            ) {

                int waterSlot = loco.getDefinition().getFuelConfiguration().waterSlot();
                if (!moveItemStackTo(
                        stack,
                        waterSlot,
                        waterSlot + 1,
                        false
                )) {
                    return ItemStack.EMPTY;
                }
            }

            /*
             * Other items go into cargo slots.
             *
             * For now, cargo slots are simply normal storage slots.
             * More restrictive cargo rules can be added later if the
             * original Traincraft behavior requires them.
             */
            else {

                int cargoStart =
                        loco.getDefinition().getFuelSystem().requiresWater()
                                ? 2
                                : 1;

                if (!moveItemStackTo(
                        stack,
                        cargoStart,
                        trainSlotCount,
                        false
                )) {
                    return ItemStack.EMPTY;
                }
            }
        }

        /*
         * Update the source slot.
         */
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        /*
         * Nothing actually moved.
         */
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        return original;
    }
}

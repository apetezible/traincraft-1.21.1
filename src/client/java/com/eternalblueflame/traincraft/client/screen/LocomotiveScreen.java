package com.eternalblueflame.traincraft.client.screen;

import com.eternalblueflame.traincraft.menu.LocomotiveMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LocomotiveScreen extends AbstractContainerScreen<LocomotiveMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "traincraft",
                    "textures/gui/gui_loco_steam.png"
            );

    public LocomotiveScreen(
            LocomotiveMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);

        // These are the dimensions of the legacy Traincraft GUI.
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                this.imageWidth,
                this.imageHeight
        );
    }
}
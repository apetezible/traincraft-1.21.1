package com.eternalblueflame.traincraft.client.screen;

import com.eternalblueflame.traincraft.menu.LocomotiveMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** Minimal container GUI for loco fuel (and water) slots. */
public class LocomotiveScreen extends AbstractContainerScreen<LocomotiveMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace(
            "textures/gui/container/generic_54.png");

    public LocomotiveScreen(LocomotiveMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        // Top strip (chest-style) + player inventory section
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, 17 + 18 * 3);
        graphics.blit(TEXTURE, x, y + 17 + 18 * 3, 0, 126, imageWidth, 96);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
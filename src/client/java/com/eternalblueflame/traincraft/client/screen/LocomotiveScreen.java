package com.eternalblueflame.traincraft.client.screen;

import com.eternalblueflame.traincraft.menu.LocomotiveMenu;
import com.eternalblueflame.traincraft.entity.EntityLocoSteam4_4_0;
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

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 256;

    public LocomotiveScreen(
            LocomotiveMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        int x = this.leftPos;
        int y = this.topPos;

        guiGraphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );

        renderFuelGauge(guiGraphics, x, y);
        renderWaterGauge(guiGraphics, x, y);
    }

    /** Matches the dynamic fuel overlay in the legacy steam-locomotive GUI. */
    private void renderFuelGauge(GuiGraphics guiGraphics, int x, int y) {
        var loco = menu.getLoco();
        if (!loco.isFuelled()) {
            return;
        }

        int fuelLevel = loco.getFuelDiv(12);
        guiGraphics.blit(
                TEXTURE,
                x + 8,
                y + 48 - fuelLevel,
                176,
                12 - fuelLevel,
                14,
                fuelLevel + 2,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    /** Matches the dynamic water overlay in the legacy steam-locomotive GUI. */
    private void renderWaterGauge(GuiGraphics guiGraphics, int x, int y) {
        if (!(menu.getLoco() instanceof EntityLocoSteam4_4_0 steamLoco)) {
            return;
        }

        int waterLevel = steamLoco.getWater() * 50 / EntityLocoSteam4_4_0.WATER_CAPACITY;
        guiGraphics.blit(
                TEXTURE,
                x + 143,
                y + 68 - waterLevel,
                190,
                69 - waterLevel,
                18,
                waterLevel + 1,
                TEXTURE_WIDTH,
                TEXTURE_HEIGHT
        );
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int relativeX = mouseX - leftPos;
        int relativeY = mouseY - topPos;
        if (relativeX > 143 && relativeX < 161 && relativeY > 18 && relativeY < 68
                && menu.getLoco() instanceof EntityLocoSteam4_4_0 steamLoco) {
            guiGraphics.renderTooltip(
                    font,
                    Component.literal("Water: " + steamLoco.getWater() + " mB / "
                            + EntityLocoSteam4_4_0.WATER_CAPACITY + " mB"),
                    mouseX,
                    mouseY
            );
        }
    }
}

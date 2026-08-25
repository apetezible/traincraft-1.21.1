package com.eternalblueflame.traincraft.hud;

import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.entity.EntityLocoSteam4_4_0;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Always-on riding panel, ported from the legacy 1.7.10 {@code HUDloco}:
 * a texture-driven gauge cluster in the bottom-left corner showing the
 * speedometer, fuel and water bars, overheat arrow and speed/state text.
 */
public final class HudLocomotive implements HudRenderCallback {

    private static final ResourceLocation STEAM_HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "traincraft", "textures/gui/loco_hud_steam.png");

    /** Legacy sheet layout: background panel region (137x90 at u=0,v=150). */
    private static final int PANEL_LEFT = 10;
    private static final int PANEL_U = 0;
    private static final int PANEL_V = 150;
    private static final int PANEL_WIDTH = 137;
    private static final int PANEL_HEIGHT = 90;

    /** Distance from the bottom of the screen to the top of the panel area. */
    private static final int BOTTOM_OFFSET = 100;

    /** Legacy speedometer full-scale reading in km/h. */
    private static final float SPEED_GAUGE_MAX = 280.0F;

    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int LOW_WATER_FLASH_TOP = 0x60300000;
    private static final int LOW_WATER_FLASH_BOTTOM = 0xA0803030;

    @Override
    public void onHudRender(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if (player == null || client.screen != null || !player.isPassenger()) {
            return;
        }
        if (!(player.getVehicle() instanceof EntityLocomotive loco)) {
            return;
        }

        renderPanel(graphics, client.font, loco);
    }

    private void renderPanel(GuiGraphics graphics, Font font, EntityLocomotive loco) {
        boolean steam = loco instanceof EntityLocoSteam4_4_0;
        int top = graphics.guiHeight() - BOTTOM_OFFSET;

        renderBackground(graphics, top);

        if (steam) {
            EntityLocoSteam4_4_0 steamLoco = (EntityLocoSteam4_4_0) loco;
            renderWaterBar(graphics, top, steamLoco);
            renderOverheatBar(graphics, top, loco);
        }

        renderThrottleGauge(graphics, top, loco);
        renderFuelBar(graphics, top, loco);
        renderText(graphics, font, top, loco, steam);

        // Legacy red overlay when a fired steam loco runs dry.
        if (steam && loco.isFuelled() && ((EntityLocoSteam4_4_0) loco).getWater() <= 1) {
            graphics.fillGradient(0, 0, graphics.guiWidth(), top + BOTTOM_OFFSET,
                    LOW_WATER_FLASH_TOP, LOW_WATER_FLASH_BOTTOM);
        }
    }

    private void renderBackground(GuiGraphics graphics, int top) {
        graphics.blit(STEAM_HUD_TEXTURE, PANEL_LEFT, top, PANEL_U, PANEL_V, PANEL_WIDTH, PANEL_HEIGHT);
    }

    /** Vertical black cover bar that hides the coloured water column as it drains. */
    private void renderWaterBar(GuiGraphics graphics, int top, EntityLocoSteam4_4_0 loco) {
        int cover = scaleGauge(loco.getWater(), EntityLocoSteam4_4_0.WATER_CAPACITY, 49);
        graphics.blit(STEAM_HUD_TEXTURE, 70, top + 17, 190, 169 + cover, 6, 49 - cover);
    }

    /** Steam-style vertical overheat column to the left of the water gauge. */
    private void renderOverheatBar(GuiGraphics graphics, int top, EntityLocomotive loco) {
        int max = EntityLocomotive.OVERHEAT_TIME + 30;
        int cover = scaleGauge(loco.getOverheatLevel(), max, 49);
        graphics.blit(STEAM_HUD_TEXTURE, 56, top + 17, 176, 169 + cover, 5, 49 - cover);
    }

    /** Throttle magnitude: full at ±100%, empty at 0. Direction is only in text if you add it. */
    private void renderThrottleGauge(GuiGraphics graphics, int top, EntityLocomotive loco) {
        // |throttle| in 0..100 → 0..49 px rise
        int rise = scaleGauge(Math.round(Math.abs(loco.getThrottle()) * 100.0F), 100, 49);
        graphics.blit(STEAM_HUD_TEXTURE, 84, top + 57 - rise, 177, 149, 16, 8);
    }

    /** Vertical black cover bar that hides the coloured fuel column as it drains. */
    private void renderFuelBar(GuiGraphics graphics, int top, EntityLocomotive loco) {
        int cover = clampGauge(loco.getFuelDiv(70), 70);
        graphics.blit(STEAM_HUD_TEXTURE, 34, top + 17, 154, 170 + cover, 9, 70 - cover);
    }

    private void renderText(GuiGraphics graphics, Font font, int top, EntityLocomotive loco, boolean steam) {
        float speedKmh = speedKilometersPerHour(loco);
        int offset = steam ? 15 : 13;

        graphics.drawString(font, "Speed:", 106, top + 7 + offset, TEXT_COLOR, true);
        graphics.drawString(font, "  " + (int) Math.abs(speedKmh), 106, top + 18 + offset, TEXT_COLOR, true);
        graphics.drawString(font, " Km/h", 106, top + 29 + offset, TEXT_COLOR, true);
        graphics.drawString(font, "State: " + loco.getState(), 50, top + 80, TEXT_COLOR, true);
    }

    private static float speedKilometersPerHour(EntityLocomotive loco) {
        var motion = loco.getDeltaMovement();
        double horizontal = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        return (float) (horizontal * 20.0D * 3.6D);
    }

    /** Scales value onto a gauge of gaugeLength pixels, clamped to [0, gaugeLength]. */
    private static int scaleGauge(int value, int maxValue, int gaugeLength) {
        if (maxValue <= 0) {
            return 0;
        }
        return clampGauge(Math.abs(value * gaugeLength / maxValue), gaugeLength);
    }

    private static int clampGauge(int value, int gaugeLength) {
        return Math.max(0, Math.min(gaugeLength, value));
    }
}

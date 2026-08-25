package com.eternalblueflame.traincraft.hud;

import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class HudLocomotive implements HudRenderCallback {
    @Override
    public void onHudRender(GuiGraphics graphics, net.minecraft.client.DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if (player == null || client.screen != null || !player.isPassenger()) {
            return;
        }

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof EntityLocomotive locomotive)) {
            return;
        }

        Vec3 movement = locomotive.getDeltaMovement();
        double speedKilometersPerHour = Math.sqrt(movement.x * movement.x + movement.z * movement.z) * 20.0D * 3.6D;
        int left = 12;
        int top = graphics.guiHeight() - 78;
        int width = 148;
        int height = 64;

        graphics.fill(left, top, left + width, top + height, 0xB0101418);
        graphics.fill(left, top, left + 4, top + height, locomotive.isEngineOn() ? 0xFFB33A3A : 0xFF62666B);
        graphics.drawString(client.font, Component.literal("4-4-0 STEAM LOCOMOTIVE"), left + 10, top + 8, 0xFFF0E4CE);
        graphics.drawString(client.font, Component.literal("ENGINE: " + (locomotive.isEngineOn() ? "ON" : "OFF")), left + 10, top + 23, 0xFFFFFFFF);
        graphics.drawString(client.font, Component.literal("THROTTLE: " + formatPercent(locomotive.getThrottle())), left + 10, top + 36, 0xFFFFFFFF);
        graphics.drawString(client.font, Component.literal(String.format("SPEED: %02d km/h", Math.round((float) speedKilometersPerHour))), left + 10, top + 49, 0xFFFFFFFF);
    }

    private static String formatPercent(float throttle) {
        return String.format("%+03d%%", Math.round(throttle * 100.0F));
    }
}

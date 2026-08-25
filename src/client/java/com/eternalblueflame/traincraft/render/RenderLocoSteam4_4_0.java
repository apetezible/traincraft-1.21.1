package com.eternalblueflame.traincraft.render;

import com.eternalblueflame.traincraft.entity.EntityLocoSteam4_4_0;
import com.eternalblueflame.traincraft.model.ModelLocoSteam4_4_0;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public final class RenderLocoSteam4_4_0 extends EntityRenderer<EntityLocoSteam4_4_0> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("traincraft", "loco_steam_4_4_0"), "main");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "traincraft", "textures/trains/4-4-0-loco_red.png");

    private final ModelLocoSteam4_4_0 model;

    public RenderLocoSteam4_4_0(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelLocoSteam4_4_0(context.bakeLayer(MODEL_LAYER));
        this.shadowRadius = 0.7F;
    }

    @Override
    public void render(EntityLocoSteam4_4_0 locomotive, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.0D);
        float visualYaw = 270.0F - entityYaw;
        if (isNorthSouth(entityYaw)) {
            visualYaw += 180.0F;
        }
        poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(visualYaw)));
        poseStack.scale(1.0F, 1.0F, 1.0F);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLocoSteam4_4_0 locomotive) {
        return TEXTURE;
    }

    private static boolean isNorthSouth(float entityYaw) {
        float normalizedYaw = entityYaw % 360.0F;
        if (normalizedYaw < 0.0F) {
            normalizedYaw += 360.0F;
        }
        return Math.abs(normalizedYaw - 90.0F) < 1.0F || Math.abs(normalizedYaw - 270.0F) < 1.0F;
    }
}

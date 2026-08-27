package com.eternalblueflame.traincraft.render;

import com.eternalblueflame.traincraft.entity.EntityCabooseLogging;
import com.eternalblueflame.traincraft.model.ModelCabooseLogging;
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

public final class RenderCabooseLogging extends EntityRenderer<EntityCabooseLogging> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("traincraft", "caboose_logging"), "main");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "traincraft", "textures/trains/cabLogging_Red.png");

    private final ModelCabooseLogging model;

    public RenderCabooseLogging(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelCabooseLogging(context.bakeLayer(MODEL_LAYER));
        this.shadowRadius = 0.7F;
    }

    @Override
    public void render(EntityCabooseLogging entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(0.0F - entityYaw)));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCabooseLogging entity) {
        return TEXTURE;
    }
}

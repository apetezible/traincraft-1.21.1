package com.eternalblueflame.traincraft.render;

import com.eternalblueflame.traincraft.entity.EntityFreightCartYellow;
import com.eternalblueflame.traincraft.model.ModelFreightCartYellow;
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

public final class RenderFreightCartYellow extends EntityRenderer<EntityFreightCartYellow> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("traincraft", "freight_cart_yellow"), "main");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "traincraft", "textures/trains/freightcart.png");

    private final ModelFreightCartYellow model;

    public RenderFreightCartYellow(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelFreightCartYellow(context.bakeLayer(MODEL_LAYER));
        this.shadowRadius = 0.7F;
    }

    @Override
    public void render(EntityFreightCartYellow entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(0.0F - entityYaw)));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFreightCartYellow entity) {
        return TEXTURE;
    }
}

package com.eternalblueflame.traincraft.render;

import com.eternalblueflame.traincraft.entity.EntityPassengerCar1;
import com.eternalblueflame.traincraft.model.ModelPassengerCar1;
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

public final class RenderPassengerCar1 extends EntityRenderer<EntityPassengerCar1> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("traincraft", "passenger_car_1"), "main");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "traincraft", "textures/trains/passenger3.png");

    private final ModelPassengerCar1 model;

    public RenderPassengerCar1(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelPassengerCar1(context.bakeLayer(MODEL_LAYER));
        this.shadowRadius = 0.6F;
    }

    @Override
    public void render(EntityPassengerCar1 entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(0.0F - entityYaw)));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPassengerCar1 entity) {
        return TEXTURE;
    }
}

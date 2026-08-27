package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelCabooseLogging {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(1, 81).addBox(-14.0F, 9.0F, -11.0F, 28.0F, 1.0F, 22.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("lower_shell",
                CubeListBuilder.create().texOffs(0, 105).addBox(-14.0F, 5.0F, -9.0F, 28.0F, 4.0F, 18.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("roof",
                CubeListBuilder.create().texOffs(0, 25).addBox(-12.0F, 26.0F, -10.0F, 19.0F, 4.0F, 4.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("front_window",
                CubeListBuilder.create().texOffs(0, 40).addBox(-2.0F, 10.0F, 3.0F, 9.0F, 8.0F, 7.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("rear_window",
                CubeListBuilder.create().texOffs(0, 56).addBox(-13.0F, 10.0F, 10.0F, 21.0F, 18.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("side_left",
                CubeListBuilder.create().texOffs(45, 56).addBox(-13.0F, 10.0F, -11.0F, 21.0F, 18.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("side_right",
                CubeListBuilder.create().texOffs(81, 59).addBox(-13.0F, 10.0F, -11.0F, 1.0F, 20.0F, 22.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("smokestack",
                CubeListBuilder.create().texOffs(83, 105).addBox(-9.0F, 20.0F, 6.0F, 2.0F, 16.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("pillow",
                CubeListBuilder.create().texOffs(0, 113).addBox(4.0F, 13.0F, -9.0F, 4.0F, 2.0F, 5.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 128, 128);
    }

    private final ModelPart root;

    public ModelCabooseLogging(ModelPart root) {
        this.root = root;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        root.render(poseStack, consumer, packedLight, packedOverlay);
    }
}

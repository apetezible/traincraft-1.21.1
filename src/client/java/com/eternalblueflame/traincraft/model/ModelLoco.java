package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelLoco {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("chassis", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0F, -2.0F, -18.0F, 16.0F, 4.0F, 36.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("boiler", CubeListBuilder.create()
                        .texOffs(0, 42)
                        .addBox(-5.0F, -9.0F, -12.0F, 10.0F, 10.0F, 18.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("frontBoiler", CubeListBuilder.create()
                        .texOffs(56, 42)
                        .addBox(-4.0F, -8.0F, -19.0F, 8.0F, 8.0F, 7.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("cab", CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(-7.0F, -15.0F, 6.0F, 14.0F, 16.0F, 10.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("roof", CubeListBuilder.create()
                        .texOffs(48, 64)
                        .addBox(-8.0F, -17.0F, 5.0F, 16.0F, 2.0F, 12.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("chimney", CubeListBuilder.create()
                        .texOffs(80, 0)
                        .addBox(-2.0F, -14.0F, -16.0F, 4.0F, 6.0F, 4.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("frontWheel", CubeListBuilder.create()
                        .texOffs(80, 16)
                        .addBox(-7.0F, 0.0F, -14.0F, 14.0F, 3.0F, 3.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("rearWheel", CubeListBuilder.create()
                        .texOffs(80, 22)
                        .addBox(-7.0F, 0.0F, 8.0F, 14.0F, 3.0F, 3.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 256, 256);
    }

    private final ModelPart root;

    public ModelLoco(ModelPart root) {
        this.root = root;
    }

        public void render(VertexConsumer consumer, com.mojang.blaze3d.vertex.PoseStack poseStack, int packedLight, int packedOverlay) {
        root.render(poseStack, consumer, packedLight, packedOverlay);
    }
}

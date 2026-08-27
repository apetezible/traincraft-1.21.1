package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelStockCar {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("box",
                CubeListBuilder.create().texOffs(1, 24).addBox(-5.0F, 1.0F, -9.0F, 9.0F, 4.0F, 18.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box0",
                CubeListBuilder.create().texOffs(2, 66).addBox(-20.0F, -1.0F, -7.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box1",
                CubeListBuilder.create().texOffs(2, 66).addBox(-13.0F, -1.0F, -7.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box2",
                CubeListBuilder.create().texOffs(2, 66).addBox(-13.0F, -1.0F, 6.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box3",
                CubeListBuilder.create().texOffs(2, 66).addBox(-20.0F, -1.0F, 6.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box4",
                CubeListBuilder.create().texOffs(2, 66).addBox(13.0F, -1.0F, -7.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box5",
                CubeListBuilder.create().texOffs(2, 66).addBox(6.0F, -1.0F, -7.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box6",
                CubeListBuilder.create().texOffs(2, 66).addBox(6.0F, -1.0F, 6.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box7",
                CubeListBuilder.create().texOffs(2, 66).addBox(13.0F, -1.0F, 6.0F, 6.0F, 6.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box8",
                CubeListBuilder.create().texOffs(1, 96).addBox(5.0F, 1.0F, -6.0F, 15.0F, 4.0F, 12.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box9",
                CubeListBuilder.create().texOffs(0, 1).addBox(-22.0F, 5.0F, -10.0F, 43.0F, 2.0F, 20.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box10",
                CubeListBuilder.create().texOffs(47, 33).addBox(-22.0F, 29.0F, -7.0F, 1.0F, 2.0F, 14.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box11",
                CubeListBuilder.create().texOffs(78, 30).addBox(-22.0F, 7.0F, -10.0F, 1.0F, 22.0F, 20.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box12",
                CubeListBuilder.create().texOffs(115, 78).addBox(5.0F, 7.0F, -10.0F, 15.0F, 23.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box13",
                CubeListBuilder.create().texOffs(121, 31).addBox(-7.0F, 7.0F, 10.0F, 12.0F, 20.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box14",
                CubeListBuilder.create().texOffs(94, 25).addBox(-8.0F, 27.0F, 10.0F, 26.0F, 3.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box15",
                CubeListBuilder.create().texOffs(1, 96).addBox(-21.0F, 1.0F, -6.0F, 15.0F, 4.0F, 12.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box16",
                CubeListBuilder.create().texOffs(3, 118).addBox(-18.0F, 1.0F, -8.0F, 9.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box17",
                CubeListBuilder.create().texOffs(3, 118).addBox(-18.0F, 1.0F, 7.0F, 9.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box18",
                CubeListBuilder.create().texOffs(3, 118).addBox(8.0F, 1.0F, 7.0F, 9.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box19",
                CubeListBuilder.create().texOffs(3, 118).addBox(8.0F, 1.0F, -8.0F, 9.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box20",
                CubeListBuilder.create().texOffs(110, 4).addBox(-18.0F, 15.0F, 10.0F, 10.0F, 10.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box21",
                CubeListBuilder.create().texOffs(3, 58).addBox(20.0F, 2.0F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box22",
                CubeListBuilder.create().texOffs(115, 78).addBox(-21.0F, 7.0F, -10.0F, 15.0F, 23.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box23",
                CubeListBuilder.create().texOffs(121, 31).addBox(-7.0F, 7.0F, -11.0F, 12.0F, 20.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box24",
                CubeListBuilder.create().texOffs(94, 25).addBox(-8.0F, 27.0F, -11.0F, 26.0F, 3.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box25",
                CubeListBuilder.create().texOffs(3, 58).addBox(-24.0F, 2.0F, -1.0F, 3.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box26",
                CubeListBuilder.create().texOffs(8, 84).addBox(-22.0F, 29.0F, 10.0F, 43.0F, 1.0F, 9.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box27",
                CubeListBuilder.create().texOffs(115, 78).addBox(5.0F, 7.0F, 9.0F, 15.0F, 23.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box28",
                CubeListBuilder.create().texOffs(110, 4).addBox(-18.0F, 15.0F, -11.0F, 10.0F, 10.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box38",
                CubeListBuilder.create().texOffs(47, 33).addBox(20.0F, 29.0F, -7.0F, 1.0F, 2.0F, 14.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box40",
                CubeListBuilder.create().texOffs(14, 75).addBox(-22.0F, 32.0F, -2.0F, 43.0F, 1.0F, 4.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box42",
                CubeListBuilder.create().texOffs(78, 30).addBox(20.0F, 7.0F, -10.0F, 1.0F, 22.0F, 20.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box44",
                CubeListBuilder.create().texOffs(115, 78).addBox(-21.0F, 7.0F, 9.0F, 15.0F, 23.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("box45",
                CubeListBuilder.create().texOffs(8, 84).addBox(-22.0F, 29.0F, -10.0F, 43.0F, 1.0F, 9.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 150, 150);
    }

    private final ModelPart root;

    public ModelStockCar(ModelPart root) {
        this.root = root;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        root.render(poseStack, consumer, packedLight, packedOverlay);
    }
}

package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelPassengerCar1 {
    private static final String[] PARTS = {
            "box|3|52|-18|2|-9|37|2|20",
            "box0|84|215|18|4|10|1|6|1",
            "box1|78|215|18|4|-9|1|6|1",
            "box2|71|215|18|4|1|1|6|1",
            "box3|65|215|-18|4|-9|1|6|1",
            "box4|59|215|-18|4|10|1|6|1",
            "box5|53|215|-18|4|0|1|6|1",
            "box6|3|182|-18|10|-10|1|1|22",
            "box7|3|182|18|10|-10|1|1|22",
            "box8|49|85|-11|4|-7|1|20|16",
            "box9|4|85|11|4|-7|1|20|16",
            "box10|63|155|-11|4|-8|23|20|1",
            "box11|2|155|-11|4|9|23|20|1",
            "box12|1|20|-19|24|-11|39|1|24",
            "box13|85|192|18|11|10|1|13|1",
            "box14|78|192|18|11|-9|1|13|1",
            "box15|70|192|-18|11|-9|1|13|1",
            "box16|62|192|-18|11|10|1|13|1",
            "box17|15|129|-15|25|-7|30|3|16",
            "box18|38|216|-13|-1|-6|4|4|1",
            "box19|26|216|-13|-1|7|4|4|1",
            "box20|14|216|10|-1|7|4|4|1",
            "box21|2|216|10|-1|-6|4|4|1"
    };

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (String part : PARTS) {
            String[] values = part.split("\\|");
            String name = values[0];
            int texU = Integer.parseInt(values[1]);
            int texV = Integer.parseInt(values[2]);
            float posX = Float.parseFloat(values[3]);
            float posY = Float.parseFloat(values[4]);
            float posZ = Float.parseFloat(values[5]);
            int width = Integer.parseInt(values[6]);
            int height = Integer.parseInt(values[7]);
            int depth = Integer.parseInt(values[8]);
            root.addOrReplaceChild(name,
                    CubeListBuilder.create()
                            .texOffs(texU, texV)
                            .addBox(0.0F, 0.0F, 0.0F, width, height, depth),
                    PartPose.offset(posX, posY, posZ));
        }
        return LayerDefinition.create(mesh, 128, 256);
    }

    private final ModelPart root;

    public ModelPassengerCar1(ModelPart root) {
        this.root = root;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        root.render(poseStack, consumer, packedLight, packedOverlay);
    }
}

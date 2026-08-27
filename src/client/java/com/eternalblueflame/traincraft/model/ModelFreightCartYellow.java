package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelFreightCartYellow {
    private static final String[] PARTS = {
            "box|1|24|-5|1|-9|9|4|18", "box0|2|66|-20|-1|-7|6|6|1",
            "box1|2|66|-13|-1|-7|6|6|1", "box2|2|66|-13|-1|6|6|6|1",
            "box3|2|66|-20|-1|6|6|6|1", "box4|2|66|13|-1|-7|6|6|1",
            "box5|2|66|6|-1|-7|6|6|1", "box6|2|66|6|-1|6|6|6|1",
            "box7|2|66|13|-1|6|6|6|1", "box8|1|96|5|1|-6|15|4|12",
            "box9|63|49|-6|27|10|25|3|1", "box10|1|124|-22|5|-10|43|27|20",
            "box11|64|26|-5|10|-11|12|17|1", "box12|64|26|-5|10|10|12|17|1",
            "box13|41|26|-18|15|10|10|10|1", "box14|63|49|-6|7|-11|25|3|1",
            "box15|1|96|-21|1|-6|15|4|12", "box16|3|118|-18|1|-8|9|2|1",
            "box17|3|118|-18|1|7|9|2|1", "box18|3|118|8|1|7|9|2|1",
            "box19|3|118|8|1|-8|9|2|1", "box20|63|49|-6|7|10|25|3|1",
            "box21|7|77|20|2|-1|3|2|2", "box22|7|77|-24|2|-1|3|2|2",
            "box25|41|26|-18|15|-11|10|10|1", "box28|1|96|5|1|-6|15|4|12"
    };

    private final ModelPart root;

    public ModelFreightCartYellow(ModelPart root) {
        this.root = root;
    }

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

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        root.render(poseStack, consumer, packedLight, packedOverlay);
    }
}

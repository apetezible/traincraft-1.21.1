package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelLocoSteam4_4_0 {
    private static final String[] PARTS = {
            "bogey|19|67|-6|0|-24|1|6|14", "bogey0|19|67|5|0|-24|1|6|14",
            "box|121|57|-8|0|-26|16|8|1", "box0|94|45|-5|7|-25|10|2|6",
            "box1|135|67|-1|2|-29|2|6|0", "box10|132|67|-2|2|-28|1|5|0",
            "box11|115|75|-7|1|-26|1|1|8", "box12|127|77|8|1|-27|1|1|8",
            "box13|125|43|5|6|-21|4|4|9", "box14|134|20|6|10|-20|2|2|7",
            "box15|157|7|-5|10|-20|10|11|11", "box16|93|34|-5|5|-19|10|4|6",
            "box17|105|25|-3|9|-19|6|2|5", "box18|153|29|-6|10|-9|12|12|13",
            "box19|102|1|-8|11|-10|4|1|11", "box2|129|67|-4|1|-28|1|6|0",
            "box20|103|13|4|11|-10|4|1|11", "box21|206|2|-4|3|-1|8|8|15",
            "box22|147|119|-5|4|-14|1|1|24", "box23|147|119|4|4|-13|1|1|24",
            "box24|42|79|-6|0|-6|1|9|9", "box25|42|79|-6|0|4|1|9|9",
            "box26|12|101|4|4|11|1|1|9", "box27|12|101|-5|4|11|1|1|9",
            "box28|37|117|5|0|4|1|9|9", "box29|37|117|5|0|-6|1|9|9",
            "box3|126|67|-6|1|-27|1|6|0", "box30|8|28|-9|13|2|1|7|16",
            "box31|43|28|8|13|2|1|7|16", "box32|106|80|8|20|2|1|10|2",
            "box33|96|80|8|20|9|1|10|2", "box34|86|80|8|20|16|1|10|2",
            "box35|148|94|-9|20|2|1|10|2", "box36|138|94|-9|20|9|1|10|2",
            "box37|128|94|-9|20|16|1|10|2", "box38|89|115|-8|13|2|3|10|1",
            "box39|111|100|4|26|2|1|4|1", "box4|121|66|-8|1|-27|1|5|1",
            "box40|114|115|5|13|2|3|10|1", "box41|94|107|-5|21|2|10|5|1",
            "box42|95|100|-5|26|2|1|4|1", "box43|36|54|8|30|2|1|1|21",
            "box44|102|100|-1|26|2|2|4|1", "box45|59|52|-9|30|2|1|1|21",
            "box46|89|97|-8|30|2|16|1|1", "box47|90|94|-7|31|2|14|1|1",
            "box48|37|115|0|32|1|10|1|23", "box49|103|115|0|32|24|10|1|23",
            "box5|145|67|3|1|-28|1|6|0", "box50|161|66|-4|12|-21|8|8|1",
            "box51|179|116|-3|21|-19|6|4|6", "box52|183|104|-2|25|-18|4|7|4",
            "box53|179|94|-3|32|-19|6|3|6", "box54|175|80|-4|35|-20|8|5|8",
            "box55|209|114|-3|22|-7|6|6|6", "box56|156|54|-5|9|-9|10|1|11",
            "box57|69|10|-3|22|-26|6|8|5", "box58|40|14|-2|21|-24|4|1|5",
            "box59|61|3|-2|30|-25|4|3|3", "box6|148|67|5|1|-27|1|6|0",
            "box60|81|4|-2|24|-27|4|4|1", "box61|212|105|-2|28|-6|4|3|4",
            "box62|0|83|2|8|-14|1|1|14", "box63|0|83|-3|8|-14|1|1|14",
            "box64|38|100|6|7|-14|1|1|10", "box65|61|97|6|4|-5|1|1|13",
            "box66|38|100|-7|7|-14|1|1|10", "box67|61|97|-7|6|-5|1|1|13",
            "box68|12|122|-2|6|14|4|2|7", "box69|90|56|-6|6|16|12|5|1",
            "box7|151|66|7|1|-27|1|5|1", "box70|89|64|-7|5|16|14|1|1",
            "box71|0|2|-7|2|-18|14|2|2", "box72|0|0|-6|3|-10|12|1|1",
            "box73|1|6|-6|3|-25|12|1|1", "box74|134|11|-8|10|-20|2|2|7",
            "box75|40|7|-2|14|-22|4|4|1", "box76|41|12|6|7|-25|0|1|11",
            "box77|41|13|-6|7|-25|0|1|11", "box78|1|18|-2|4|-19|4|1|4",
            "box79|21|100|-5|3|-24|1|1|14", "box8|125|30|-9|6|-21|4|4|9",
            "box80|20|100|4|3|-24|1|1|14", "box9|141|67|1|2|-28|1|5|0",
            "frame|182|57|-9|11|1|18|2|19"
    };

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (String part : PARTS) {
            String[] values = part.split("\\|");
            int width = Integer.parseInt(values[6]);
            int height = Integer.parseInt(values[7]);
            int depth = Integer.parseInt(values[8]);
            if (width == 0 || height == 0 || depth == 0) {
                continue;
            }
            float[] rotation = rotation(values[0]);
            root.addOrReplaceChild(values[0], CubeListBuilder.create()
                            .texOffs(Integer.parseInt(values[1]), Integer.parseInt(values[2]))
                            .addBox(0.0F, 0.0F, 0.0F, width, height, depth),
                    PartPose.offsetAndRotation(
                            Float.parseFloat(values[3]),
                            Float.parseFloat(values[4]),
                            Float.parseFloat(values[5]),
                            rotation[0], rotation[1], rotation[2]));
        }
        return LayerDefinition.create(mesh, 256, 256);
    }

    private static float[] rotation(String name) {
        return switch (name) {
            case "box1" -> new float[]{-5.6897734F, 0.0F, 0.0F};
            case "box10" -> new float[]{-5.8643064F, 0.0F, 0.0F};
            case "box11" -> new float[]{0.0F, -4.4156833F, 0.0F};
            case "box12" -> new float[]{0.0F, -1.850049F, 0.0F};
            case "box2", "box5" -> new float[]{-5.9341197F, 0.0F, 0.0F};
            case "box26", "box27" -> new float[]{-0.99483764F, 0.0F, 0.0F};
            case "box3", "box6", "box62" -> new float[]{-6.091199F, 0.0F, 0.0F};
            case "box4", "box7" -> new float[]{-6.0737457F, 0.0F, 0.0F};
            case "box48" -> new float[]{0.0F, 0.0F, -0.13962634F};
            case "box49" -> new float[]{0.0F, 3.1415927F, 0.13962634F};
            case "box63" -> new float[]{-6.1435585F, 0.0F, 0.0F};
            case "box64" -> new float[]{-5.8817596F, 0.0F, 0.0F};
            case "box66" -> new float[]{-6.1784654F, 0.0F, 0.0F};
            case "box76" -> new float[]{-0.9424778F, -0.17453292F, 0.0F};
            case "box77" -> new float[]{-0.9424778F, -6.091199F, 0.0F};
            case "box9" -> new float[]{-5.8643064F, 0.0F, 0.0F};
            default -> new float[]{0.0F, 0.0F, 0.0F};
        };
    }

    private final ModelPart root;

    public ModelLocoSteam4_4_0(ModelPart root) {
        this.root = root;
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}

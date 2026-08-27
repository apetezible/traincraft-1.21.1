package com.eternalblueflame.traincraft.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ModelFlatCarLogsDB {
    private static final String[] PARTS = {
            "box|70|25|-5|2|0|8|4|4",
            "box0|3|27|-23|1|-6|14|5|1",
            "box1|189|12|-20|0|5|8|7|0",
            "box2|96|1|15|2|-7|2|2|14",
            "box3|96|1|-17|2|-7|2|2|14",
            "box4|36|27|-23|1|5|14|5|1",
            "box5|2|49|-25|9|-9|50|6|6",
            "box6|2|49|-24|9|-2|50|6|6",
            "box7|2|49|-26|9|-9|50|6|6",
            "box8|2|49|-24|9|0|50|6|6",
            "box9|104|42|-27|5|-11|54|3|22",
            "box10|213|80|27|6|-8|1|5|16",
            "box11|158|77|28|6|-2|2|3|4",
            "box12|160|73|27|8|10|1|6|20",
            "box13|21|121|27|8|11|54|6|1",
            "box14|21|121|-27|8|-11|54|6|1",
            "box15|0|65|19|5|-12|2|19|1",
            "box16|0|65|6|5|-12|2|19|1",
            "box17|0|65|-8|5|-12|2|19|1",
            "box18|0|65|-21|5|-12|2|19|1",
            "box19|0|65|19|5|11|2|19|1",
            "box20|187|68|-28|6|-8|1|5|16",
            "box21|0|65|6|5|11|2|19|1",
            "box22|7|82|-28|11|-6|1|16|2",
            "box23|7|82|-28|11|4|1|16|2",
            "box24|0|83|-26|8|-10|52|1|20",
            "box25|146|80|28|7|-7|1|3|3",
            "box26|104|42|-27|5|-11|54|3|22",
            "box27|0|65|-8|5|11|2|19|1",
            "box28|7|82|27|11|-6|1|16|2",
            "box29|7|82|27|11|4|1|16|2",
            "box30|0|65|-21|5|11|2|19|1",
            "box31|2|49|-25|15|-9|50|6|6",
            "box32|2|49|-24|15|-2|50|6|6",
            "box33|2|49|-26|15|-9|50|6|6",
            "box34|2|49|-24|15|0|50|6|6",
            "box35|189|12|12|0|5|8|7|0",
            "box36|141|112|-25|15|-9|50|7|7",
            "box37|2|49|-25|15|-16|50|6|6",
            "box38|146|80|28|7|4|1|3|3",
            "box39|2|34|-23|21|-20|46|7|7",
            "box40|138|80|-29|7|-7|1|3|3",
            "box41|12|68|-26|21|1|50|6|6",
            "box42|138|80|-29|7|4|1|3|3",
            "box44|158|77|-27|6|-2|2|3|4"
    };

    private final ModelPart root;

    public ModelFlatCarLogsDB(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (String part : PARTS) {
            String[] values = part.split("\\|");
            root.addOrReplaceChild(values[0],
                    CubeListBuilder.create()
                            .texOffs(Integer.parseInt(values[1]), Integer.parseInt(values[2]))
                            .addBox(0.0F, 0.0F, 0.0F,
                                    Integer.parseInt(values[6]),
                                    Integer.parseInt(values[7]),
                                    Integer.parseInt(values[8])),
                    PartPose.offset(Float.parseFloat(values[3]), Float.parseFloat(values[4]), Float.parseFloat(values[5])));
        }
        return LayerDefinition.create(mesh, 256, 128);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay) {
        root.render(poseStack, consumer, packedLight, packedOverlay);
    }
}

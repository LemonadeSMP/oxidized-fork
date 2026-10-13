package safro.oxidized.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import safro.oxidized.entity.CopperGolemEntity;

public class CopperGolemEntityModel extends EntityModel<CopperGolemEntity> {
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_arm;
    private final ModelPart right_arm;

    public CopperGolemEntityModel(ModelPart root) {
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 0)
                .addBox(-2.2F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(8, 15)
                .addBox(0.2F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-3.0F, -6.0F, -1.5F, 6.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 15)
                .addBox(3.0F, -6.0F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(18, 8)
                .addBox(-4.0F, -6.0F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0F, -10.0F, -2.0F, 6.0F, 4.0F, 4.0F)
                .texOffs(0, 0).addBox(-0.5F, -7.6F, -3.0F, 1.0F, 2.0F, 1.0F)
                .texOffs(6, 15).addBox(-0.5F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F)
                .texOffs(0, 15).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(CopperGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.right_leg.xRot = -1.5F * Mth.positiveModulo(limbSwing, 13.0F) * limbSwingAmount;
        this.left_leg.xRot = 1.5F * Mth.positiveModulo(limbSwing, 13.0F) * limbSwingAmount;
        this.right_leg.yRot = 0.0F;
        this.left_leg.yRot = 0.0F;
        if (entity.isPressingButtons()) {
            this.right_arm.z = -0.5F;
            this.left_arm.z = -0.5F;
            this.right_arm.xRot = 6.1F;
            this.left_arm.xRot = 6.1F;
        } else {
            this.right_arm.z = 0.0F;
            this.left_arm.z = 0.0F;
            this.right_arm.xRot = 0.0F;
            this.left_arm.xRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.left_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.right_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.body.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.left_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.right_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.head.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}

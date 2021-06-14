package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PlayerMaidModel extends BedrockModel<EntityMaid> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer legLeft;
    private final ModelRenderer legRight;
    private final ModelRenderer armLeft;
    private final ModelRenderer armRight;
    private final ModelRenderer backpackPositioningBone;

    public PlayerMaidModel() {
        texWidth = 32;
        texHeight = 32;

        head = new ModelRenderer(this);
        head.setPos(0.0F, 8.0F, 0.0F);
        head.texOffs(0, 0).addBox(-2.0F, -5.75F, -2.0F, 4.0F, 4.0F, 4.0F, 2.0F, false);
        head.texOffs(16, 0).addBox(-2.0F, -5.75F, -2.0F, 4.0F, 4.0F, 4.0F, 2.1F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 17.0F, 0.0F);
        body.texOffs(8, 8).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 2.0F, 1.0F, false);
        body.texOffs(8, 16).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 2.0F, 1.1F, false);

        legLeft = new ModelRenderer(this);
        legLeft.setPos(1.8F, 17.0F, 0.0F);
        legLeft.texOffs(8, 24).addBox(-1.25F, 0.25F, -1.0F, 2.0F, 6.0F, 2.0F, 0.5F, false);
        legLeft.texOffs(0, 24).addBox(-1.25F, 0.25F, -1.0F, 2.0F, 6.0F, 2.0F, 0.6F, false);

        legRight = new ModelRenderer(this);
        legRight.setPos(-1.8F, 17.0F, 0.0F);
        legRight.texOffs(0, 8).addBox(-0.75F, 0.25F, -1.0F, 2.0F, 6.0F, 2.0F, 0.5F, false);
        legRight.texOffs(0, 16).addBox(-0.75F, 0.25F, -1.0F, 2.0F, 6.0F, 2.0F, 0.6F, false);

        armLeft = new ModelRenderer(this);
        armLeft.setPos(3.0F, 9.0F, 0.0F);
        armLeft.texOffs(16, 24).addBox(0.75F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.5F, false);
        armLeft.texOffs(24, 24).addBox(0.75F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.6F, false);

        ModelRenderer armLeftPositioningBone = new ModelRenderer(this);
        armLeftPositioningBone.setPos(1.75F, 5.75F, 0.0F);
        armLeft.addChild(armLeftPositioningBone);

        armRight = new ModelRenderer(this);
        armRight.setPos(-3.0F, 9.0F, 0.0F);
        armRight.texOffs(20, 8).addBox(-2.75F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.5F, false);
        armRight.texOffs(20, 16).addBox(-2.75F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.6F, false);

        ModelRenderer armRightPositioningBone = new ModelRenderer(this);
        armRightPositioningBone.setPos(-1.75F, 5.75F, 0.0F);
        armRight.addChild(armRightPositioningBone);

        backpackPositioningBone = new ModelRenderer(this);
        backpackPositioningBone.setPos(0.0F, 9.0F, 0.0F);

        modelMap.put("head", new ModelRendererWrapper(head));
        modelMap.put("body", new ModelRendererWrapper(body));
        modelMap.put("legLeft", new ModelRendererWrapper(legLeft));
        modelMap.put("legRight", new ModelRendererWrapper(legRight));
        modelMap.put("armLeft", new ModelRendererWrapper(armLeft));
        modelMap.put("armRight", new ModelRendererWrapper(armRight));
        modelMap.put("armLeftPositioningBone", new ModelRendererWrapper(armLeftPositioningBone));
        modelMap.put("armRightPositioningBone", new ModelRendererWrapper(armRightPositioningBone));
        modelMap.put("backpackPositioningBone", new ModelRendererWrapper(backpackPositioningBone));
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        legLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        legRight.render(matrixStack, buffer, packedLight, packedOverlay);
        armLeft.render(matrixStack, buffer, packedLight, packedOverlay);
        armRight.render(matrixStack, buffer, packedLight, packedOverlay);
        backpackPositioningBone.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}

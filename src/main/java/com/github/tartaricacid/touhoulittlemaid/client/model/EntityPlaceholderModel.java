package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class EntityPlaceholderModel extends EntityModel<Entity> {
    private final ModelRenderer bone;

    public EntityPlaceholderModel() {
        texWidth = 16;
        texHeight = 16;
        bone = new ModelRenderer(this);
        bone.setPos(8.0F, 24.0F, -10.0F);
        bone.texOffs(0, 0).addBox(-16.0F, -16.0F, 9.5F, 16.0F, 16.0F, 0.0F, 0.02F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
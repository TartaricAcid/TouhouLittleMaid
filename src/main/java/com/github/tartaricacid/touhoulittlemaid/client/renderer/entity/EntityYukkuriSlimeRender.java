package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityYukkuriModel;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class EntityYukkuriSlimeRender extends MobRenderer<SlimeEntity, EntityYukkuriModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/yukkuri.png");
    private final SlimeRenderer vanillaRender;

    public EntityYukkuriSlimeRender(EntityRendererManager context) {
        super(context, new EntityYukkuriModel(), 0.25F);
        this.vanillaRender = new SlimeRenderer(context);
    }

    @Override
    public void render(SlimeEntity slime, float yaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int packedLight) {
        if (VanillaConfig.REPLACE_SLIME_MODEL.get()) {
            renderYukkuri(slime, yaw, partialTicks, poseStack, buffer, packedLight);
        } else {
            vanillaRender.render(slime, yaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    private void renderYukkuri(SlimeEntity slime, float yaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int packedLight) {
        this.shadowRadius = 0.25F * (float) slime.getSize();
        super.render(slime, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void scale(SlimeEntity slime, MatrixStack poseStack, float partialTicks) {
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float slimeSize = (float) slime.getSize();
        float tmp = MathHelper.lerp(partialTicks, slime.oSquish, slime.squish) / (slimeSize * 0.5F + 1.0F);
        float scale = 1.0F / (tmp + 1.0F);
        poseStack.scale(scale * slimeSize, 1.0F / scale * slimeSize, scale * slimeSize);
    }

    public ResourceLocation getTextureLocation(SlimeEntity slime) {
        return TEXTURE;
    }
}

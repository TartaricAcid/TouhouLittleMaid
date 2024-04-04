package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityMarisaYukkuriModel;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;

public class EntityMarisaYukkuriSlimeRender extends MobRenderer<MagmaCube, EntityMarisaYukkuriModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/marisa_yukkuri.png");
    private final MagmaCubeRenderer vanillaRender;

    public EntityMarisaYukkuriSlimeRender(EntityRendererProvider.Context context) {
        super(context, new EntityMarisaYukkuriModel(context.bakeLayer(EntityMarisaYukkuriModel.LAYER)), 0.25F);
        this.vanillaRender = new MagmaCubeRenderer(context);
    }

    @Override
    public void render(MagmaCube slime, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (VanillaConfig.REPLACE_SLIME_MODEL.get()) {
            renderYukkuri(slime, yaw, partialTicks, poseStack, buffer, packedLight);
        } else {
            vanillaRender.render(slime, yaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    protected int getBlockLightLevel(MagmaCube pEntity, BlockPos pPos) {
        return 15;
    }

    private void renderYukkuri(MagmaCube slime, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.shadowRadius = 0.25F * (float) slime.getSize();
        super.render(slime, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(MagmaCube slime, PoseStack poseStack, float partialTicks) {
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float slimeSize = (float) slime.getSize();
        float tmp = Mth.lerp(partialTicks, slime.oSquish, slime.squish) / (slimeSize * 0.5F + 1.0F);
        float scale = 1.0F / (tmp + 1.0F);
        poseStack.scale(scale * slimeSize, 1.0F / scale * slimeSize, scale * slimeSize);
    }

    @Override
    public ResourceLocation getTextureLocation(MagmaCube slime) {
        return TEXTURE;
    }
}

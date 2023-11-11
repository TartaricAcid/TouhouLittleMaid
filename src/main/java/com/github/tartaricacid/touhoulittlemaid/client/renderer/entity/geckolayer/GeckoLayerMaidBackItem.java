package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;

public class GeckoLayerMaidBackItem<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private final GeckoEntityMaidRenderer renderer;

    public GeckoLayerMaidBackItem(GeckoEntityMaidRenderer entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityRenderer.getGeoModel() == null) {
            return;
        }
        if (entityLivingBaseIn instanceof EntityMaid maid) {
            ItemStack stack = maid.getMaidInv().getStackInSlot(5);
            if (stack.getItem() instanceof Vanishable) {
                if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                    return;
                }
                matrixStack.pushPose();
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
                matrixStack.translate(0, 0.5, -0.25);
                switch (maid.getBackpackLevel()) {
                    default:
                    case BackpackLevel.EMPTY:
                        matrixStack.translate(0, 0.625, 0.2);
                        break;
                    case BackpackLevel.SMALL:
                        matrixStack.translate(0, 0.625, -0.05);
                        break;
                    case BackpackLevel.MIDDLE:
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-7.5F));
                        matrixStack.translate(0, 0.625, -0.25);
                        break;
                    case BackpackLevel.BIG:
                        matrixStack.translate(0, 0, -0.4);
                        break;
                }
                if (SlashBladeCompat.isSlashBladeItem(stack)) {
                    SlashBladeRender.renderGeckoMaidBackSlashBlade(matrixStack, bufferIn, packedLightIn, stack);
                } else {
                    Minecraft.getInstance().getItemRenderer().renderStatic(maid, stack, ItemTransforms.TransformType.FIXED, false, matrixStack, bufferIn, maid.level, packedLightIn, OverlayTexture.NO_OVERLAY, maid.getId());
                }
                matrixStack.popPose();
            }
        }
    }
}

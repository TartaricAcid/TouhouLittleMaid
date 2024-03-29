package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
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
            ItemStack stack = maid.getBackpackShowItem();
            if (stack.getItem() instanceof Vanishable) {
                if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackItem() || maid.isSleeping() || maid.isInvisible() || RenderFixer.isCarryOnRender(stack, bufferIn)) {
                    return;
                }
                matrixStack.pushPose();
                matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                matrixStack.translate(0, 0.5, -0.25);
                if (InGameMaidConfig.INSTANCE.isShowBackpack()) {
                    maid.getMaidBackpackType().offsetBackpackItem(matrixStack);
                } else {
                    BackpackManager.getEmptyBackpack().offsetBackpackItem(matrixStack);
                }
                if (SlashBladeCompat.isSlashBladeItem(stack)) {
                    SlashBladeRender.renderGeckoMaidBackSlashBlade(matrixStack, bufferIn, packedLightIn, stack);
                } else {
                    Minecraft.getInstance().getItemRenderer().renderStatic(maid, stack, ItemDisplayContext.FIXED, false, matrixStack, bufferIn, maid.level(), packedLightIn, OverlayTexture.NO_OVERLAY, maid.getId());
                }
                matrixStack.popPose();
            }
        }
    }
}

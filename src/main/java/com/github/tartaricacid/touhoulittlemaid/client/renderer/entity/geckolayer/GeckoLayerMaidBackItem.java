package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.slashblade.SlashBladeRender;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class GeckoLayerMaidBackItem<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private final GeckoEntityMaidRenderer renderer;

    public GeckoLayerMaidBackItem(GeckoEntityMaidRenderer entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityRenderer.getGeoModel() == null) {
            return;
        }
        if (entityLivingBaseIn instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) entityLivingBaseIn;
            ItemStack stack = maid.getBackpackShowItem();
            if (stack.getItem() instanceof IVanishable) {
                if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                    return;
                }
                matrixStack.pushPose();
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
                matrixStack.translate(0, 0.5, -0.25);
                maid.getMaidBackpackType().offsetBackpackItem(matrixStack);
                if (SlashBladeCompat.isSlashBladeItem(stack)) {
                    SlashBladeRender.renderGeckoMaidBackSlashBlade(matrixStack, bufferIn, packedLightIn, stack);
                } else {
                    Minecraft.getInstance().getItemInHandRenderer().renderItem(maid, stack, ItemCameraTransforms.TransformType.FIXED, false, matrixStack, bufferIn, packedLightIn);
                }
                matrixStack.popPose();
            }
        }
    }
}

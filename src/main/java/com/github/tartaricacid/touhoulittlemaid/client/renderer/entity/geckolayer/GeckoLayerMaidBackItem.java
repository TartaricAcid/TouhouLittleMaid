package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

public class GeckoLayerMaidBackItem<T extends Mob> extends GeoLayerRenderer<T, GeckoEntityMaidRenderer<T>> {
    public GeckoLayerMaidBackItem(GeckoEntityMaidRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(entity);
        if (maid == null) {
            return;
        }
        var model = this.entityRenderer.getAnimatableEntity(entity).getCurrentModel();
        if (model == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (!this.entityRenderer.getAnimatableEntity(entity).getMaidInfo().isShowBackpack() || entity.isSleeping() || entity.isInvisible() || RenderFixer.isCarryOnRender(stack, bufferIn)) {
            return;
        }
        if (entity instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
            return;
        }
        if (stack.getItem() instanceof TieredItem) {
            matrixStack.pushPose();

            if (!model.backpackBones().isEmpty()) {
                RenderUtils.prepMatrixForLocator(matrixStack, model.backpackBones());
            }
            matrixStack.translate(0, 1, 0.25);

            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0.5, -0.25);
            if (entity instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack()) {
                maid.getMaidBackpackType().offsetBackpackItem(matrixStack);
            } else {
                BackpackManager.getEmptyBackpack().offsetBackpackItem(matrixStack);
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(entity, stack, ItemDisplayContext.FIXED, false, matrixStack, bufferIn, entity.level(), packedLightIn, OverlayTexture.NO_OVERLAY, entity.getId());
            matrixStack.popPose();
        }
    }
}

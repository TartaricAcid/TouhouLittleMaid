package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.carryon.RenderFixer;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

public class LayerMaidBackItem extends RenderLayer<Mob, BedrockModel<Mob>> {
    private final EntityMaidRenderer renderer;

    public LayerMaidBackItem(EntityMaidRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IMaid maid = IMaid.convert(mob);
        if (maid == null) {
            return;
        }
        ItemStack stack = maid.getBackpackShowItem();
        if (!renderer.getMainInfo().isShowBackpack() || mob.isSleeping()
            || mob.isInvisible() || RenderFixer.isCarryOnRender(stack, bufferIn)) {
            return;
        }
        if (maid instanceof EntityMaid entityMaid && !entityMaid.getConfigManager().isShowBackItem()) {
            return;
        }

        if (stack.getItem() instanceof TieredItem) {
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0.5, -0.25);
            if (maid instanceof EntityMaid entityMaid && entityMaid.getConfigManager().isShowBackpack()) {
                maid.getMaidBackpackType().offsetBackpackItem(matrixStack);
            } else {
                BackpackManager.getEmptyBackpack().offsetBackpackItem(matrixStack);
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(mob, stack, ItemDisplayContext.FIXED, false, matrixStack, bufferIn, mob.level(), packedLightIn, OverlayTexture.NO_OVERLAY, mob.getId());
            matrixStack.popPose();
        }
    }
}
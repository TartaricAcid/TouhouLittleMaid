package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Map;

public class LayerMaidBipedHead extends RenderLayer<Mob, BedrockModel<Mob>> {
    private final EntityMaidRenderer maidRenderer;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    public LayerMaidBipedHead(EntityMaidRenderer maidRenderer, EntityModelSet modelSet) {
        super(maidRenderer);
        this.maidRenderer = maidRenderer;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, Mob mob, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean allowRenderHead = maidRenderer.getMainInfo().isShowCustomHead() && getParentModel().hasHead();
        if (!allowRenderHead) {
            return;
        }

        // 渲染头盔栏的
        ItemStack head = mob.getItemBySlot(EquipmentSlot.HEAD);
        if (!head.isEmpty()) {
            Item item = head.getItem();
            poseStack.pushPose();
            this.getParentModel().getHead().translateAndRotate(poseStack);
            if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock skullBlock) {
                poseStack.scale(1.1875F, -1.1875F, -1.1875F);
                ResolvableProfile resolvableProfile = head.get(DataComponents.PROFILE);
                poseStack.translate(-0.5D, 0.0D, -0.5D);
                SkullBlock.Type type = skullBlock.getType();
                SkullModelBase modelBase = this.skullModels.get(type);
                RenderType rendertype = SkullBlockRenderer.getRenderType(type, resolvableProfile);
                SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, bufferIn, packedLightIn, modelBase, rendertype);
            }
            poseStack.popPose();
        }

        IMaid maid = IMaid.convert(mob);
        if (maid == null) {
            return;
        }

        // 渲染女仆背部的
        ItemStack stack = maid.getBackpackShowItem();
        // 不做限制，任意方块都可以显示
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState blockState = blockItem.getBlock().defaultBlockState();
            poseStack.pushPose();
            this.getParentModel().getHead().translateAndRotate(poseStack);
            poseStack.scale(0.8F, -0.8F, -0.8F);
            poseStack.translate(-0.5, 0.625, -0.5);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            poseStack.popPose();
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.concurrent.ExecutionException;

public class TileEntityItemStackChairRenderer extends ItemStackTileEntityRenderer {
    @Override
    public void renderByItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        EntityChair entityChair;
        ItemChair.Data data = ItemChair.getData(itemStackIn);
        float renderItemScale = CustomPackLoader.CHAIR_MODELS.getModelRenderItemScale(data.getModelId());
        try {
            entityChair = (EntityChair) EntityCacheUtil.ENTITY_CACHE.get(EntityChair.TYPE, () -> {
                Entity e = EntityChair.TYPE.create(world);
                if (e == null) {
                    return new EntityChair(world);
                } else {
                    return e;
                }
            });
        } catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }
        entityChair.setModelId(data.getModelId());
        matrixStack.pushPose();
        matrixStack.scale(renderItemScale, renderItemScale, renderItemScale);
        EntityRendererManager render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        RenderSystem.runAsFancy(() -> render.render(entityChair,
                1 / renderItemScale - 0.125, 0.25, 0.75, 0, 0,
                matrixStack, buffer, combinedLight));
        render.setRenderHitBoxes(isShowHitBox);
        matrixStack.popPose();
    }
}

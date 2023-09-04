package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityChairRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class TileEntityItemStackChairRenderer extends BlockEntityWithoutLevelRenderer {
    public TileEntityItemStackChairRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        EntityChair entityChair;
        ItemChair.Data data = ItemChair.getData(itemStackIn);
        float renderItemScale = CustomPackLoader.CHAIR_MODELS.getModelRenderItemScale(data.getModelId());
        try {
            entityChair = (EntityChair) EntityCacheUtil.ENTITY_CACHE.get(EntityChair.TYPE, () -> {
                Entity e = EntityChair.TYPE.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityChair(world));
            });
        } catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }
        entityChair.setModelId(data.getModelId());
        poseStack.pushPose();
        poseStack.scale(renderItemScale, renderItemScale, renderItemScale);
        EntityRenderDispatcher render = Minecraft.getInstance().getEntityRenderDispatcher();
        boolean isShowHitBox = render.shouldRenderHitBoxes();
        render.setRenderHitBoxes(false);
        EntityChairRenderer.renderHitBox = false;
        RenderSystem.runAsFancy(() -> render.render(entityChair,
                1 / renderItemScale - 0.125, 0.25, 0.75, 0, 0,
                poseStack, buffer, combinedLight));
        EntityChairRenderer.renderHitBox = true;
        render.setRenderHitBoxes(isShowHitBox);
        poseStack.popPose();
    }
}

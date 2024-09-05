package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.detail.ChairModelDetailsGui;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ChairModelMessage;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ChairModelGui extends AbstractModelGui<EntityChair, ChairModelInfo> {
    private static int PAGE_INDEX = 0;
    private static int PACK_INDEX = 0;
    private static int ROW_INDEX = 0;

    public ChairModelGui(EntityChair entity) {
        super(entity, CustomPackLoader.CHAIR_MODELS.getPackList());
    }

    @Override
    protected void drawLeftEntity(int middleX, int middleY, float mouseX, float mouseY) {
        float renderItemScale = CustomPackLoader.CHAIR_MODELS.getModelRenderItemScale(entity.getModelId());
        InventoryScreen.renderEntityInInventory((middleX - 256 / 2) / 2, middleY + 80, (int) (45 * renderItemScale), -25, -20, entity);
    }

    @Override
    protected void drawRightEntity(PoseStack poseStack, int posX, int posY, ChairModelInfo modelItem) {
        ResourceLocation cacheIconId = modelItem.getCacheIconId();
        var allTextures = Minecraft.getInstance().textureManager.byPath;
        if (allTextures.containsKey(cacheIconId)) {
            int textureSize = 24;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, cacheIconId);
            blit(poseStack, posX - textureSize / 2, posY - textureSize, textureSize, textureSize, 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            drawEntity(poseStack, posX, posY, modelItem);
        }
    }

    @Override
    protected void openDetailsGui(EntityChair entity, ChairModelInfo modelInfo) {
        if (minecraft != null) {
            minecraft.setScreen(new ChairModelDetailsGui(entity, modelInfo));
        }
    }

    @Override
    protected void notifyModelChange(EntityChair entity, ChairModelInfo modelInfo) {
        NetworkHandler.CHANNEL.sendToServer(new ChairModelMessage(entity.getId(), modelInfo.getModelId(), modelInfo.getMountedYOffset(),
                modelInfo.isTameableCanRide(), modelInfo.isNoGravity()));
    }

    @Override
    protected void addModelCustomTips(ChairModelInfo modelItem, List<Component> tooltips) {
    }

    @Override
    protected int getPageIndex() {
        return PAGE_INDEX;
    }

    @Override
    protected void setPageIndex(int pageIndex) {
        PAGE_INDEX = pageIndex;
    }

    @Override
    protected int getPackIndex() {
        return PACK_INDEX;
    }

    @Override
    protected void setPackIndex(int packIndex) {
        PACK_INDEX = packIndex;
    }

    @Override
    protected int getRowIndex() {
        return ROW_INDEX;
    }

    @Override
    protected void setRowIndex(int rowIndex) {
        ROW_INDEX = rowIndex;
    }

    private void drawEntity(PoseStack poseStack, int posX, int posY, ChairModelInfo modelItem) {
        Level world = getMinecraft().level;
        if (world == null) {
            return;
        }

        EntityChair chair;
        try {
            chair = (EntityChair) EntityCacheUtil.ENTITY_CACHE.get(EntityChair.TYPE, () -> {
                Entity e = EntityChair.TYPE.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityChair(world));
            });
        } catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }

        chair.setModelId(modelItem.getModelId().toString());
        InventoryScreen.renderEntityInInventory(posX, posY, (int) (12 * modelItem.getRenderItemScale()), -25, -20, chair);
    }
}

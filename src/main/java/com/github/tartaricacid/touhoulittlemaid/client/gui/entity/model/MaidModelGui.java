package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.detail.MaidModelDetailsGui;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.MaidModelMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetMaidSoundIdMessage;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.client.event.SpecialMaidRenderEvent.EASTER_EGG_MODEL;
import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class MaidModelGui extends AbstractModelGui<EntityMaid, MaidModelInfo> {
    private static int PAGE_INDEX = 0;
    private static int PACK_INDEX = 0;
    private static int ROW_INDEX = 0;

    public MaidModelGui(EntityMaid maid) {
        super(maid, CustomPackLoader.MAID_MODELS.getPackList());
    }

    @Override
    protected void drawLeftEntity(int middleX, int middleY, float mouseX, float mouseY) {
        float renderItemScale = CustomPackLoader.MAID_MODELS.getModelRenderItemScale(entity.getModelId());
        InventoryScreen.renderEntityInInventory((middleX - 256 / 2) / 2, middleY + 90, (int) (45 * renderItemScale), (middleX - 256 / 2f) / 2 - mouseX, middleY + 80 - 40 - mouseY, entity);
    }

    @Override
    protected void drawRightEntity(PoseStack poseStack, int posX, int posY, MaidModelInfo modelItem) {
        ResourceLocation cacheIconId = modelItem.getCacheIconId();
        var allTextures = Minecraft.getInstance().textureManager.byPath;
        if (allTextures.containsKey(cacheIconId)) {
            int textureSize = 24;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, cacheIconId);
            RenderSystem.disableDepthTest();
            blit(poseStack, posX - textureSize / 2, posY - textureSize, textureSize, textureSize, 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            drawEntity(poseStack, posX, posY, modelItem);
        }
    }

    @Override
    protected void openDetailsGui(EntityMaid maid, MaidModelInfo modelInfo) {
        if (minecraft != null && modelInfo.getEasterEgg() == null) {
            minecraft.setScreen(new MaidModelDetailsGui(maid, modelInfo));
        }
    }

    @Override
    protected void notifyModelChange(EntityMaid maid, MaidModelInfo info) {
        if (info.getEasterEgg() == null) {
            NetworkHandler.CHANNEL.sendToServer(new MaidModelMessage(maid.getId(), info.getModelId()));
            String useSoundPackId = info.getUseSoundPackId();
            if (StringUtils.isNotBlank(useSoundPackId)) {
                NetworkHandler.CHANNEL.sendToServer(new SetMaidSoundIdMessage(maid.getId(), useSoundPackId));
            }
            // 切换模型时，重置手部动作
            maid.handItemsForAnimation[0] = ItemStack.EMPTY;
            maid.handItemsForAnimation[1] = ItemStack.EMPTY;
        }
    }

    @Override
    protected void addModelCustomTips(MaidModelInfo modelItem, List<Component> tooltips) {
        String useSoundPackId = modelItem.getUseSoundPackId();
        if (StringUtils.isNotBlank(useSoundPackId)) {
            tooltips.add(new TranslatableComponent("gui.touhou_little_maid.skin.tooltips.maid_use_sound_pack_id", useSoundPackId).withStyle(ChatFormatting.GOLD));
        }
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

    private void drawEntity(PoseStack poseStack, int posX, int posY, MaidModelInfo modelItem) {
        Level world = getMinecraft().level;
        if (world == null) {
            return;
        }

        EntityMaid maid;
        try {
            maid = (EntityMaid) EntityCacheUtil.ENTITY_CACHE.get(EntityMaid.TYPE, () -> {
                Entity e = EntityMaid.TYPE.create(world);
                return Objects.requireNonNullElseGet(e, () -> new EntityMaid(world));
            });
        } catch (ExecutionException | ClassCastException e) {
            e.fillInStackTrace();
            return;
        }

        clearMaidDataResidue(maid, false);
        if (modelItem.getEasterEgg() != null) {
            maid.setModelId(EASTER_EGG_MODEL);
        } else {
            maid.setModelId(modelItem.getModelId().toString());
        }
        InventoryScreen.renderEntityInInventory(posX, posY, (int) (12 * modelItem.getRenderItemScale()), -25, -20, maid);
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.detail.ChairModelDetailsGui;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ChairModelMessage;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
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
    protected void drawRightEntity(int posX, int posY, ChairModelInfo modelItem) {
        World world = getMinecraft().level;
        if (world == null) {
            return;
        }

        EntityChair chair;
        try {
            chair = (EntityChair) EntityCacheUtil.ENTITY_CACHE.get(EntityChair.TYPE, () -> {
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

        chair.setModelId(modelItem.getModelId().toString());
        InventoryScreen.renderEntityInInventory(posX, posY, (int) (12 * modelItem.getRenderItemScale()), -25, -20, chair);
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
    protected void addModelCustomTips(ChairModelInfo modelItem, List<ITextComponent> tooltips) {
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
}

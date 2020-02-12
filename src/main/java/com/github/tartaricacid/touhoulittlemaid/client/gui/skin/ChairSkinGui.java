package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ChairModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ApplyChairSkinDataMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.concurrent.ExecutionException;

/**
 * @author TartaricAcid
 * @date 2019/7/12 12:27
 **/
@SideOnly(Side.CLIENT)
public class ChairSkinGui extends AbstractSkinGui<EntityChair, ChairModelItem> {
    private static int PAGE_INDEX = 0;
    private static int PACK_INDEX = 0;
    private static int ROW_INDEX = 0;

    public ChairSkinGui(EntityChair chair) {
        super(chair, CustomModelLoader.CHAIR_MODEL.getPackList(), "touhou_little_maid:entity.item.chair");
    }

    @Override
    void drawLeftEntity(int middleX, int middleY, float mouseX, float mouseY) {
        float renderItemScale = CustomModelLoader.CHAIR_MODEL.getModelRenderItemScale(entity.getModelId());
        GuiInventory.drawEntityOnScreen(middleX - 190, middleY + 80, (int) (45 * renderItemScale), -25, -20, entity);
    }

    @Override
    void drawRightEntity(int posX, int posY, ChairModelItem modelItem) {
        EntityChair chair;
        try {
            chair = (EntityChair) ClientProxy.ENTITY_CACHE.get(ENTITY_ID, () -> {
                Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(ENTITY_ID), mc.world);
                if (e == null) {
                    return new EntityChair(mc.world);
                } else {
                    return e;
                }
            });
        } catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }
        chair.setModelId(modelItem.getModelId().toString());
        GuiInventory.drawEntityOnScreen(posX, posY, (int) (12 * modelItem.getRenderItemScale()), -25, -20, chair);
    }

    @Override
    void openDetailsGui(EntityChair chair, ResourceLocation modelId) {
        mc.addScheduledTask(() -> mc.displayGuiScreen(new ChairSkinDetailsGui(chair, modelId)));
    }

    @Override
    void notifyModelChange(EntityChair chair, ResourceLocation modelId) {
        float mountedYOffset = CustomModelLoader.CHAIR_MODEL.getModelMountedYOffset(modelId.toString());
        boolean isTameableCanRide = CustomModelLoader.CHAIR_MODEL.getModelTameableCanRide(modelId.toString());
        boolean isNoGravity = CustomModelLoader.CHAIR_MODEL.getModelNoGravity(modelId.toString());
        CommonProxy.INSTANCE.sendToServer(new ApplyChairSkinDataMessage(chair.getUniqueID(), modelId, mountedYOffset, isTameableCanRide, isNoGravity));
    }

    @Override
    int getPageIndex() {
        return PAGE_INDEX;
    }

    @Override
    void setPageIndex(int pageIndex) {
        PAGE_INDEX = pageIndex;
    }

    @Override
    int getPackIndex() {
        return PACK_INDEX;
    }

    @Override
    void setPackIndex(int packIndex) {
        PACK_INDEX = packIndex;
    }

    @Override
    int getRowIndex() {
        return ROW_INDEX;
    }

    @Override
    void setRowIndex(int rowIndex) {
        ROW_INDEX = rowIndex;
    }
}

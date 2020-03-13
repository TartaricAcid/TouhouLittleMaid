package com.github.tartaricacid.touhoulittlemaid.client.gui.skin;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomModelLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ApplyMaidSkinDataMessage;
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
public class MaidSkinGui extends AbstractSkinGui<EntityMaid, MaidModelItem> {
    private static int PAGE_INDEX = 0;
    private static int PACK_INDEX = 0;
    private static int ROW_INDEX = 0;

    public MaidSkinGui(EntityMaid maid) {
        super(maid, CustomModelLoader.MAID_MODEL.getPackList(), "touhou_little_maid:entity.passive.maid");
    }

    @Override
    void drawLeftEntity(int middleX, int middleY, float mouseX, float mouseY) {
        float renderItemScale = CustomModelLoader.MAID_MODEL.getModelRenderItemScale(entity.getModelId());
        GuiInventory.drawEntityOnScreen(middleX - 190, middleY + 90, (int) (45 * renderItemScale), middleX - 190 - mouseX, middleY + 80 - 40 - mouseY, entity);
    }

    @Override
    void drawRightEntity(int posX, int posY, MaidModelItem modelItem) {
        EntityMaid maid;
        try {
            maid = (EntityMaid) ClientProxy.ENTITY_CACHE.get(ENTITY_ID, () -> {
                Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(ENTITY_ID), mc.world);
                if (e == null) {
                    return new EntityMaid(mc.world);
                } else {
                    return e;
                }
            });
        } catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }
        // 缓存的对象往往有一些奇怪的东西，一并清除
        maid.setModelId(modelItem.getModelId().toString());
        maid.setShowSasimono(false);
        maid.hurtResistantTime = 0;
        maid.hurtTime = 0;
        maid.deathTime = 0;
        maid.setSitting(false);
        GuiInventory.drawEntityOnScreen(posX, posY, (int) (12 * modelItem.getRenderItemScale()), -25, -20, maid);
    }

    @Override
    void openDetailsGui(EntityMaid maid, ResourceLocation modelId) {
        mc.addScheduledTask(() -> mc.displayGuiScreen(new MaidSkinDetailsGui(maid, modelId)));
    }

    @Override
    void notifyModelChange(EntityMaid maid, MaidModelItem info) {
        CommonProxy.INSTANCE.sendToServer(new ApplyMaidSkinDataMessage(
                maid.getUniqueID(), info.getModelId(),
                info.isCanHoldTrolley(), info.isCanHoldVehicle(),
                info.isCanRidingBroom()));
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

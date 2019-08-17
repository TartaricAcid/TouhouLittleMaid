package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.MaidBaubleGuiContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.MaidInventoryGuiContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.MaidMainGuiContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.ChairSkinGui;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidBaubleContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventoryContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidMainContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class MaidGuiHandler implements IGuiHandler {
    /**
     * @param guiId     MAIN_GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param entityId  实体 ID
     * @param taskIndex 打开 MAIN_GUI 时缓存的 taskIndex
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int entityId, int taskIndex, int z) {
        if (guiId == MAIN_GUI.MAIN.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidMainContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == MAIN_GUI.INVENTORY.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidInventoryContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == MAIN_GUI.BAUBLE.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidBaubleContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == SKIN_GUI.CHAIR.getId() && world.getEntityByID(entityId) instanceof EntityChair) {
            // 服务端什么也不做
            return null;
        }
        return null;
    }

    /**
     * @param guiId     MAIN_GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param entityId  实体 ID
     * @param taskIndex 打开 MAIN_GUI 时缓存的 taskIndex
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int entityId, int taskIndex, int z) {
        if (guiId == MAIN_GUI.MAIN.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidMainGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == MAIN_GUI.INVENTORY.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidInventoryGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == MAIN_GUI.BAUBLE.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidBaubleGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == SKIN_GUI.CHAIR.getId() && world.getEntityByID(entityId) instanceof EntityChair) {
            return new ChairSkinGui((EntityChair) world.getEntityByID(entityId));
        }
        return null;
    }

    /**
     * 女仆主界面上部标签页 GUI
     */
    public enum MAIN_GUI {
        // 女仆主界面
        MAIN(1),
        // 女仆物品栏界面
        INVENTORY(2),
        // 女仆饰品栏
        BAUBLE(3);

        private int id;

        MAIN_GUI(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 无 GUI 的占位符
     */
    public enum NONE_GUI {
        // 无 GUI
        NONE(-1);

        private int id;

        NONE_GUI(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 皮肤界面的 GUI ID 枚举
     */
    public enum SKIN_GUI {
        // 椅子的 GUI
        CHAIR(4);

        private int id;

        SKIN_GUI(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}

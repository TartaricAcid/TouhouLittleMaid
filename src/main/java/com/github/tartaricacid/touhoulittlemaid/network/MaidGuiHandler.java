package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.MaidBaubleGuiContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.MaidInventoryGuiContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.MaidMainGuiContainer;
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
     * @param guiId     GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param entityId  实体 ID
     * @param taskIndex 打开 GUI 时缓存的 taskIndex
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int entityId, int taskIndex, int z) {
        if (guiId == GUI.MAIN.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidMainContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == GUI.INVENTORY.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidInventoryContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == GUI.BAUBLE.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidBaubleContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        return null;
    }

    /**
     * @param guiId     GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param entityId  实体 ID
     * @param taskIndex 打开 GUI 时缓存的 taskIndex
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int entityId, int taskIndex, int z) {
        if (guiId == GUI.MAIN.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidMainGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == GUI.INVENTORY.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidInventoryGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        if (guiId == GUI.BAUBLE.getId() && world.getEntityByID(entityId) instanceof EntityMaid) {
            return new MaidBaubleGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(entityId), taskIndex);
        }
        return null;
    }

    public enum GUI {
        // 女仆主界面
        MAIN(1),
        // 女仆物品栏界面
        INVENTORY(2),
        // 女仆饰品栏
        BAUBLE(3),
        // 无 GUI
        NONE(-1);

        private int id;

        GUI(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}

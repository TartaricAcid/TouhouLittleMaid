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
     * @param ID     GUI 的 ID
     * @param player 玩家
     * @param world  世界
     * @param x      实体 ID
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // FIXME: 2019/7/24 对应的数字 ID 需要后续与 GUI 的枚举类型进行对接
        if (ID == 1 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidMainContainer(player.inventory, (EntityMaid) world.getEntityByID(x), y);
        }
        if (ID == 2 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidInventoryContainer(player.inventory, (EntityMaid) world.getEntityByID(x), y);
        }
        if (ID == 3 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidBaubleContainer(player.inventory, (EntityMaid) world.getEntityByID(x), y);
        }
        return null;
    }

    /**
     * @param ID     GUI 的 ID
     * @param player 玩家
     * @param world  世界
     * @param x      实体 ID
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        // FIXME: 2019/7/24 对应的数字 ID 需要后续与 GUI 的枚举类型进行对接
        if (ID == 1 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidMainGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(x), y);
        }
        if (ID == 2 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidInventoryGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(x), y);
        }
        if (ID == 3 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidBaubleGuiContainer(player.inventory, (EntityMaid) world.getEntityByID(x), y);
        }
        return null;
    }
}

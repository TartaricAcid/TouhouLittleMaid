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
        if (ID == 1 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidMainContainer(player.inventory, (EntityMaid) world.getEntityByID(x));
        }
        if (ID == 2 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidInventoryContainer(player.inventory, (EntityMaid) world.getEntityByID(x));
        }
        if (ID == 3 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidBaubleContainer(player.inventory, (EntityMaid) world.getEntityByID(x));
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
        if (ID == 1 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidMainGuiContainer(new MaidMainContainer(player.inventory, (EntityMaid) world.getEntityByID(x)),
                    (EntityMaid) world.getEntityByID(x));
        }
        if (ID == 2 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidInventoryGuiContainer(new MaidInventoryContainer(player.inventory, (EntityMaid) world.getEntityByID(x)),
                    (EntityMaid) world.getEntityByID(x));
        }
        if (ID == 3 && world.getEntityByID(x) instanceof EntityMaid) {
            return new MaidBaubleGuiContainer(new MaidBaubleContainer(player.inventory, (EntityMaid) world.getEntityByID(x)),
                    (EntityMaid) world.getEntityByID(x));
        }
        return null;
    }
}

package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGuiContainer;
import com.github.tartaricacid.touhoulittlemaid.client.gui.inventory.*;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.ChairSkinGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.skin.NpcMaidSkinGui;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySuitcase;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.*;
import com.github.tartaricacid.touhoulittlemaid.item.ItemAlbum;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.IGuiHandler;
import noppes.npcs.entity.EntityCustomNpc;

import javax.annotation.Nullable;

public class MaidGuiHandler implements IGuiHandler {
    /**
     * @param guiId     MAIN_GUI 的 ID
     * @param player    玩家
     * @param world     世界
     * @param entityId  实体 ID
     * @param taskIndex 打开 MAIN_GUI 时缓存的 taskIndex
     * @param startRow  滚动式 GUI 的滚动数据
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int entityId, int taskIndex, int startRow) {
        Entity entity = world.getEntityByID(entityId);
        if (guiId == MAIN_GUI.MAIN.getId() && entity instanceof EntityMaid) {
            return new MaidMainContainer(player.inventory, (EntityMaid) entity, taskIndex);
        }
        if (guiId == MAIN_GUI.INVENTORY.getId() && entity instanceof EntityMaid) {
            return new MaidInventoryContainer(player.inventory, (EntityMaid) entity, taskIndex, startRow);
        }
        if (guiId == MAIN_GUI.BAUBLE.getId() && entity instanceof EntityMaid) {
            return new MaidBaubleContainer(player.inventory, (EntityMaid) entity, taskIndex);
        }
        if (guiId == OTHER_GUI.CHAIR.getId() && entity instanceof EntityChair) {
            // 服务端什么也不做
            return null;
        }
        if (guiId == OTHER_GUI.ALBUM.getId() && player.getHeldItemMainhand().getItem() instanceof ItemAlbum) {
            return new AlbumContainer(player.inventory, player.getHeldItemMainhand());
        }
        // 此时的 entityId, taskIndex, startRow 代表的是方块的 x y z 值
        if (guiId == OTHER_GUI.MAID_BEACON.getId() && world.getTileEntity(new BlockPos(entityId, taskIndex, startRow)) instanceof TileEntityMaidBeacon) {
            return new MaidBeaconContainer((TileEntityMaidBeacon) world.getTileEntity(new BlockPos(entityId, taskIndex, startRow)));
        }
        if (guiId == OTHER_GUI.SUITCASE.getId() && entity instanceof EntitySuitcase) {
            return new SuitcaseContainer(player.inventory, (EntitySuitcase) entity);
        }
        if (CommonProxy.isNpcModLoad() && guiId == OTHER_GUI.NPC_MAID_TOOL.getId() && entity instanceof EntityCustomNpc) {
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
     * @param startRow  滚动式 GUI 的滚动数据
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int entityId, int taskIndex, int startRow) {
        Entity entity = world.getEntityByID(entityId);
        if (guiId == MAIN_GUI.MAIN.getId() && entity instanceof EntityMaid) {
            return new MaidMainGuiContainer(player.inventory, (EntityMaid) entity, taskIndex);
        }
        if (guiId == MAIN_GUI.INVENTORY.getId() && entity instanceof EntityMaid) {
            return new MaidInventoryGuiContainer(player.inventory, (EntityMaid) entity, taskIndex, startRow);
        }
        if (guiId == MAIN_GUI.BAUBLE.getId() && entity instanceof EntityMaid) {
            return new MaidBaubleGuiContainer(player.inventory, (EntityMaid) entity, taskIndex);
        }
        if (guiId == OTHER_GUI.CHAIR.getId() && entity instanceof EntityChair) {
            return new ChairSkinGui((EntityChair) entity);
        }
        if (guiId == OTHER_GUI.ALBUM.getId() && player.getHeldItemMainhand().getItem() instanceof ItemAlbum) {
            return new AlbumGuiContainer(player.inventory, player.getHeldItemMainhand());
        }
        // 此时的 entityId, taskIndex, startRow 代表的是方块的 x y z 值
        if (guiId == OTHER_GUI.MAID_BEACON.getId() && world.getTileEntity(new BlockPos(entityId, taskIndex, startRow)) instanceof TileEntityMaidBeacon) {
            return new MaidBeaconGuiContainer(new MaidBeaconContainer((TileEntityMaidBeacon) world.getTileEntity(new BlockPos(entityId, taskIndex, startRow))));
        }
        if (guiId == OTHER_GUI.SUITCASE.getId() && entity instanceof EntitySuitcase) {
            return new SuitcaseGuiContainer(player.inventory, (EntitySuitcase) entity);
        }
        if (CommonProxy.isNpcModLoad() && guiId == OTHER_GUI.NPC_MAID_TOOL.getId() && entity instanceof EntityCustomNpc) {
            return returnNpcClientGui(entity);
        }
        return null;
    }

    @Optional.Method(modid = "customnpcs")
    private Object returnNpcClientGui(Entity entity) {
        EntityCustomNpc npc = (EntityCustomNpc) entity;
        EntityLivingBase entityInNpc = npc.modelData.getEntity(npc);
        if (entityInNpc instanceof EntityMaid) {
            return new NpcMaidSkinGui((EntityCustomNpc) entity, (EntityMaid) entityInNpc);
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
        // 技能栏
        // SKILL(4);

        private int id;

        MAIN_GUI(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 其他界面的 GUI ID 枚举
     */
    public enum OTHER_GUI {
        // 无 GUI 的占位符
        NONE(-1),
        // 椅子的 GUI
        CHAIR(5),
        // 相册
        ALBUM(6),
        // 女仆信标
        MAID_BEACON(7),
        // NPC 模组的模型切换工具界面
        NPC_MAID_TOOL(8),
        // 行李箱
        SUITCASE(9);

        private int id;

        OTHER_GUI(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}

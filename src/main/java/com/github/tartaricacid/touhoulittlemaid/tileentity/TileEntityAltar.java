package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/31 21:59
 **/
public class TileEntityAltar extends TileEntity {
    public final ItemStackHandler handler = new ItemStackHandler() {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private boolean isRender = false;
    private boolean canPlaceItem = false;
    private IBlockState blockState = Blocks.AIR.getDefaultState();
    private List<BlockPos> blockPosList = Lists.newArrayList();
    private List<BlockPos> canPlaceItemPosList = Lists.newArrayList();
    private EnumFacing facing = EnumFacing.SOUTH;

    public void setForgeData(IBlockState blockState, boolean isRender, boolean canPlaceItem, EnumFacing facing,
                             List<BlockPos> blockPosList, List<BlockPos> canPlaceItemPosList) {
        this.isRender = isRender;
        this.canPlaceItem = canPlaceItem;
        this.blockState = blockState;
        this.facing = facing;
        this.blockPosList = blockPosList;
        this.canPlaceItemPosList = canPlaceItemPosList;
        refresh();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        getTileData().setBoolean(NBT.IS_RENDER.getName(), isRender);
        getTileData().setBoolean(NBT.CAN_PLACE_ITEM.getName(), canPlaceItem);
        getTileData().setInteger(NBT.STORAGE_STATE_ID.getName(), Block.getStateId(blockState));
        getTileData().setTag(NBT.STORAGE_ITEM.getName(), handler.serializeNBT());
        getTileData().setInteger(NBT.FACING.getName(), facing.getIndex());

        NBTTagList blockList = new NBTTagList();
        for (BlockPos pos : blockPosList) {
            blockList.appendTag(NBTUtil.createPosTag(pos));
        }
        getTileData().setTag(NBT.STORAGE_BLOCK_LIST.getName(), blockList);

        NBTTagList itemList = new NBTTagList();
        for (BlockPos pos : canPlaceItemPosList) {
            itemList.appendTag(NBTUtil.createPosTag(pos));
        }
        getTileData().setTag(NBT.CAN_PLACE_ITEM_POS_LIST.getName(), itemList);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        isRender = getTileData().getBoolean(NBT.IS_RENDER.getName());
        canPlaceItem = getTileData().getBoolean(NBT.CAN_PLACE_ITEM.getName());
        blockState = Block.getStateById(getTileData().getInteger(NBT.STORAGE_STATE_ID.getName()));
        handler.deserializeNBT(getTileData().getCompoundTag(NBT.STORAGE_ITEM.getName()));
        facing = EnumFacing.byIndex(getTileData().getInteger(NBT.FACING.getName()));

        NBTTagList blockList = getTileData().getTagList(NBT.STORAGE_BLOCK_LIST.getName(), Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < blockList.tagCount(); i++) {
            blockPosList.add(NBTUtil.getPosFromTag(blockList.getCompoundTagAt(i)));
        }

        NBTTagList itemList = getTileData().getTagList(NBT.CAN_PLACE_ITEM_POS_LIST.getName(), Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.tagCount(); i++) {
            canPlaceItemPosList.add(NBTUtil.getPosFromTag(itemList.getCompoundTagAt(i)));
        }
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-9, -5, -9), pos.add(9, 5, 9));
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    public void refresh() {
        markDirty();
        if (world != null) {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    public boolean isRender() {
        return isRender;
    }

    public boolean isCanPlaceItem() {
        return canPlaceItem;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public List<BlockPos> getBlockPosList() {
        return blockPosList;
    }

    public List<BlockPos> getCanPlaceItemPosList() {
        return canPlaceItemPosList;
    }

    public enum NBT {
        // 存储的物品
        STORAGE_ITEM("StorageItem"),
        // 是否进行渲染（一个多方块结构，只有中心才会启用它）
        IS_RENDER("IsRender"),
        // 能否放置物品（只有特定位置才允许放置物品）
        CAN_PLACE_ITEM("CanPlaceItem"),
        // 方块 ID
        STORAGE_STATE_ID("StorageBlockStateId"),
        // 朝向
        FACING("Facing"),
        // 存储的方块列表
        STORAGE_BLOCK_LIST("StorageBlockList"),
        // 可以放置物品的方块
        CAN_PLACE_ITEM_POS_LIST("CanPlaceItemPosList");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

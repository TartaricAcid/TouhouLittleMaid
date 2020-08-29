package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityMaidJoy extends TileEntity {
    private static final String TYPE = "MaidJoyBlockType";
    private static final String CORE_BLOCK_TAG = "CoreBlock";
    private static final String CORE_BLOCK_POS_TAG = "CoreBlockPos";
    private static final String ALL_BLOCKS_TAG = "AllBlocks";

    private String type = "reading";
    private boolean isCoreBlock = false;
    private BlockPos coreBlockPos = BlockPos.ORIGIN;
    private List<BlockPos> allBlocks = Lists.newArrayList();

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -1, writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        type = compound.getString(TYPE);
        isCoreBlock = compound.getBoolean(CORE_BLOCK_TAG);
        coreBlockPos = NBTUtil.getPosFromTag(compound.getCompoundTag(CORE_BLOCK_POS_TAG));
        allBlocks.clear();
        NBTTagList blockList = compound.getTagList(ALL_BLOCKS_TAG, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < blockList.tagCount(); i++) {
            allBlocks.add(NBTUtil.getPosFromTag(blockList.getCompoundTagAt(i)));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString(TYPE, type);
        compound.setBoolean(CORE_BLOCK_TAG, isCoreBlock);
        compound.setTag(CORE_BLOCK_POS_TAG, NBTUtil.createPosTag(coreBlockPos));
        NBTTagList blockList = new NBTTagList();
        for (BlockPos pos : allBlocks) {
            blockList.appendTag(NBTUtil.createPosTag(pos));
        }
        compound.setTag(ALL_BLOCKS_TAG, blockList);
        return super.writeToNBT(compound);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().add(-1, -1, -1), getPos().add(2, 2, 2));
    }

    public void setForgeData(String type, boolean isCoreBlock, BlockPos coreBlockPos, List<BlockPos> allBlocks) {
        this.type = type;
        this.isCoreBlock = isCoreBlock;
        this.coreBlockPos = coreBlockPos;
        this.allBlocks = allBlocks;
        // 确保数据已经存入
        markDirty();
        // 通知 world 更新方块数据
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    public String getType() {
        return type;
    }

    public boolean isCoreBlock() {
        return isCoreBlock;
    }

    public BlockPos getCoreBlockPos() {
        return coreBlockPos;
    }

    public List<BlockPos> getAllBlocks() {
        return allBlocks;
    }
}

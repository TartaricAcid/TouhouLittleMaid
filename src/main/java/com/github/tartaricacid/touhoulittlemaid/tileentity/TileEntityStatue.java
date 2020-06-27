package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityStatue extends TileEntity {
    private static final String STATUE_SIZE_TAG = "StatueSize";
    private static final String CORE_BLOCK_TAG = "CoreBlock";
    private static final String CORE_BLOCK_POS_TAG = "CoreBlockPos";
    private static final String STATUE_FACING_TAG = "StatueFacing";
    private static final String ALL_BLOCKS_TAG = "AllBlocks";
    private static final String EXTRA_MAID_DATA = "ExtraMaidData";

    private Size size = Size.SMALL;
    private boolean isCoreBlock = false;
    private BlockPos coreBlockPos = BlockPos.ORIGIN;
    private EnumFacing facing = EnumFacing.NORTH;
    private List<BlockPos> allBlocks = Lists.newArrayList();
    @Nullable
    private NBTTagCompound extraMaidData = null;

    public void setForgeData(Size size, boolean isCoreBlock, BlockPos coreBlockPos, EnumFacing facing,
                             List<BlockPos> allBlocks, @Nullable NBTTagCompound extraData) {
        this.size = size;
        this.isCoreBlock = isCoreBlock;
        this.coreBlockPos = coreBlockPos;
        this.facing = facing;
        this.allBlocks = allBlocks;
        this.extraMaidData = extraData;
        refresh();
    }

    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        getTileData().setInteger(STATUE_SIZE_TAG, size.ordinal());
        getTileData().setBoolean(CORE_BLOCK_TAG, isCoreBlock);
        getTileData().setTag(CORE_BLOCK_POS_TAG, NBTUtil.createPosTag(coreBlockPos));
        getTileData().setInteger(STATUE_FACING_TAG, facing.getHorizontalIndex());
        NBTTagList blockList = new NBTTagList();
        for (BlockPos pos : allBlocks) {
            blockList.appendTag(NBTUtil.createPosTag(pos));
        }
        getTileData().setTag(ALL_BLOCKS_TAG, blockList);
        if (extraMaidData != null) {
            getTileData().setTag(EXTRA_MAID_DATA, extraMaidData);
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        super.readFromNBT(compound);
        size = Size.getSizeByIndex(getTileData().getInteger(STATUE_SIZE_TAG));
        isCoreBlock = getTileData().getBoolean(CORE_BLOCK_TAG);
        coreBlockPos = NBTUtil.getPosFromTag(getTileData().getCompoundTag(CORE_BLOCK_POS_TAG));
        facing = EnumFacing.byHorizontalIndex(getTileData().getInteger(STATUE_FACING_TAG));
        allBlocks.clear();
        NBTTagList blockList = getTileData().getTagList(ALL_BLOCKS_TAG, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < blockList.tagCount(); i++) {
            allBlocks.add(NBTUtil.getPosFromTag(blockList.getCompoundTagAt(i)));
        }
        if (getTileData().hasKey(EXTRA_MAID_DATA, Constants.NBT.TAG_COMPOUND)) {
            extraMaidData = getTileData().getCompoundTag(EXTRA_MAID_DATA);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-5, -1, -5), pos.add(5, 10, 5));
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -1, writeToNBT(new NBTTagCompound()));
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

    public Size getSize() {
        return size;
    }

    public boolean isCoreBlock() {
        return isCoreBlock;
    }

    public BlockPos getCoreBlockPos() {
        return coreBlockPos;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public List<BlockPos> getAllBlocks() {
        return allBlocks;
    }

    @Nullable
    public NBTTagCompound getExtraMaidData() {
        return extraMaidData;
    }

    public enum Size {
        // 雕像的尺寸
        TINY(0.5f, new Vec3i(1, 1, 1)),
        SMALL(1.0f, new Vec3i(1, 2, 1)),
        MIDDLE(2.0f, new Vec3i(2, 4, 2)),
        BIG(3.0f, new Vec3i(3, 6, 3));

        private final float scale;
        private final Vec3i dimension;

        Size(float scale, Vec3i dimension) {
            this.scale = scale;
            this.dimension = dimension;
        }

        public static Size getSizeByIndex(int index) {
            return Size.values()[MathHelper.clamp(index, 0, Size.values().length - 1)];
        }

        public float getScale() {
            return scale;
        }

        public Vec3i getDimension() {
            return dimension;
        }
    }
}

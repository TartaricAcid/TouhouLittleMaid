package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityStatue extends TileEntity {
    private static final String STATUE_SIZE_TAG = "StatueSize";    public static final TileEntityType<TileEntityStatue> TYPE = TileEntityType.Builder.of(TileEntityStatue::new, InitBlocks.STATUE.get()).build(null);
    private static final String CORE_BLOCK_TAG = "CoreBlock";
    private static final String CORE_BLOCK_POS_TAG = "CoreBlockPos";
    private static final String STATUE_FACING_TAG = "StatueFacing";
    private static final String ALL_BLOCKS_TAG = "AllBlocks";
    private static final String EXTRA_MAID_DATA = "ExtraMaidData";
    private Size size = Size.SMALL;
    private boolean isCoreBlock = false;
    private BlockPos coreBlockPos = BlockPos.ZERO;
    private Direction facing = Direction.NORTH;
    private List<BlockPos> allBlocks = Lists.newArrayList();
    @Nullable
    private CompoundNBT extraMaidData = null;
    public TileEntityStatue() {
        super(TYPE);
    }

    public void setForgeData(Size size, boolean isCoreBlock, BlockPos coreBlockPos, Direction facing,
                             List<BlockPos> allBlocks, @Nullable CompoundNBT extraData) {
        this.size = size;
        this.isCoreBlock = isCoreBlock;
        this.coreBlockPos = coreBlockPos;
        this.facing = facing;
        this.allBlocks = allBlocks;
        this.extraMaidData = extraData;
        refresh();
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        getTileData().putInt(STATUE_SIZE_TAG, size.ordinal());
        getTileData().putBoolean(CORE_BLOCK_TAG, isCoreBlock);
        getTileData().put(CORE_BLOCK_POS_TAG, NBTUtil.writeBlockPos(coreBlockPos));
        getTileData().putString(STATUE_FACING_TAG, facing.getSerializedName());
        ListNBT blockList = new ListNBT();
        for (BlockPos pos : allBlocks) {
            blockList.add(NBTUtil.writeBlockPos(pos));
        }
        getTileData().put(ALL_BLOCKS_TAG, blockList);
        if (extraMaidData != null) {
            getTileData().put(EXTRA_MAID_DATA, extraMaidData);
        }
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        size = Size.getSizeByIndex(getTileData().getInt(STATUE_SIZE_TAG));
        isCoreBlock = getTileData().getBoolean(CORE_BLOCK_TAG);
        coreBlockPos = NBTUtil.readBlockPos(getTileData().getCompound(CORE_BLOCK_POS_TAG));
        facing = Direction.byName(getTileData().getString(STATUE_FACING_TAG));
        allBlocks.clear();
        ListNBT blockList = getTileData().getList(ALL_BLOCKS_TAG, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < blockList.size(); i++) {
            allBlocks.add(NBTUtil.readBlockPos(blockList.getCompound(i)));
        }
        if (getTileData().contains(EXTRA_MAID_DATA, Constants.NBT.TAG_COMPOUND)) {
            extraMaidData = getTileData().getCompound(EXTRA_MAID_DATA);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition.offset(-5, -1, -5), worldPosition.offset(5, 10, 5));
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), -1, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (level != null) {
            this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
        }
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.DEFAULT);
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

    public Direction getFacing() {
        return facing;
    }

    public List<BlockPos> getAllBlocks() {
        return allBlocks;
    }

    @Nullable
    public CompoundNBT getExtraMaidData() {
        return extraMaidData;
    }

    public enum Size {
        // 雕像的尺寸
        TINY(0.5f, new Vector3i(1, 1, 1)),
        SMALL(1.0f, new Vector3i(1, 2, 1)),
        MIDDLE(2.0f, new Vector3i(2, 4, 2)),
        BIG(3.0f, new Vector3i(3, 6, 3));

        private final float scale;
        private final Vector3i dimension;

        Size(float scale, Vector3i dimension) {
            this.scale = scale;
            this.dimension = dimension;
        }

        public static Size getSizeByIndex(int index) {
            return Size.values()[MathHelper.clamp(index, 0, Size.values().length - 1)];
        }

        public float getScale() {
            return scale;
        }

        public Vector3i getDimension() {
            return dimension;
        }
    }


}

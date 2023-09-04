package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityStatue extends BlockEntity {
    private static final String STATUE_SIZE_TAG = "StatueSize";    public static final BlockEntityType<TileEntityStatue> TYPE = BlockEntityType.Builder.of(TileEntityStatue::new, InitBlocks.STATUE.get()).build(null);
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
    private CompoundTag extraMaidData = null;
    public TileEntityStatue(BlockPos blockPos, BlockState blockState) {
        super(TYPE, blockPos, blockState);
    }

    public void setForgeData(Size size, boolean isCoreBlock, BlockPos coreBlockPos, Direction facing,
                             List<BlockPos> allBlocks, @Nullable CompoundTag extraData) {
        this.size = size;
        this.isCoreBlock = isCoreBlock;
        this.coreBlockPos = coreBlockPos;
        this.facing = facing;
        this.allBlocks = allBlocks;
        this.extraMaidData = extraData;
        refresh();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        getPersistentData().putInt(STATUE_SIZE_TAG, size.ordinal());
        getPersistentData().putBoolean(CORE_BLOCK_TAG, isCoreBlock);
        getPersistentData().put(CORE_BLOCK_POS_TAG, NbtUtils.writeBlockPos(coreBlockPos));
        getPersistentData().putString(STATUE_FACING_TAG, facing.getSerializedName());
        ListTag blockList = new ListTag();
        for (BlockPos pos : allBlocks) {
            blockList.add(NbtUtils.writeBlockPos(pos));
        }
        getPersistentData().put(ALL_BLOCKS_TAG, blockList);
        if (extraMaidData != null) {
            getPersistentData().put(EXTRA_MAID_DATA, extraMaidData);
        }
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        size = Size.getSizeByIndex(getPersistentData().getInt(STATUE_SIZE_TAG));
        isCoreBlock = getPersistentData().getBoolean(CORE_BLOCK_TAG);
        coreBlockPos = NbtUtils.readBlockPos(getPersistentData().getCompound(CORE_BLOCK_POS_TAG));
        facing = Direction.byName(getPersistentData().getString(STATUE_FACING_TAG));
        allBlocks.clear();
        ListTag blockList = getPersistentData().getList(ALL_BLOCKS_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < blockList.size(); i++) {
            allBlocks.add(NbtUtils.readBlockPos(blockList.getCompound(i)));
        }
        if (getPersistentData().contains(EXTRA_MAID_DATA, Tag.TAG_COMPOUND)) {
            extraMaidData = getPersistentData().getCompound(EXTRA_MAID_DATA);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-5, -1, -5), worldPosition.offset(5, 10, 5));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
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
    public CompoundTag getExtraMaidData() {
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
            return Size.values()[Mth.clamp(index, 0, Size.values().length - 1)];
        }

        public float getScale() {
            return scale;
        }

        public Vec3i getDimension() {
            return dimension;
        }
    }


}

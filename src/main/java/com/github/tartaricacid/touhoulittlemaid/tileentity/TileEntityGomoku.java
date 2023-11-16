package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class TileEntityGomoku extends BlockEntity {
    public static final BlockEntityType<TileEntityGomoku> TYPE = BlockEntityType.Builder.of(TileEntityGomoku::new, InitBlocks.GOMOKU.get()).build(null);
    private static final String CHESS_DATA = "ChessData";
    private final int[][] chessData = new int[15][15];

    public TileEntityGomoku(BlockPos pPos, BlockState pBlockState) {
        super(TYPE, pPos, pBlockState);
        String str = "7,7,1;6,6,2;5,7,1;8,7,2;8,6,1;6,8,2;6,7,1;4,7,2;5,8,1;7,6,2;5,6,1;5,5,2;4,5,1;7,8,2;5,10,1;5,9,2;3,4,1;2,3,2;5,4,1;6,4,2;2,7,1;3,6,2;4,9,1;3,8,2;3,10,1;2,11,2;2,10,1;4,10,2;3,7,1;9,8,2;10,9,1;9,6,2;10,5,1;3,11,2;2,12,1;7,3,2;8,2,1;9,5,2;9,7,1;10,8,2;8,8,1;10,6,2;8,4,1;8,1,2;7,2,1;8,3,2;9,2,1;6,2,2;10,2,1;11,2,2;6,3,1;9,3,2;10,3,1;10,1,2;9,1,1;4,11,2;1,11,1;11,3,2;3,9,1;4,8,2;2,9,1;2,8,2;1,9,1;0,9,2;1,10,1;1,8,2;0,8,1;6,11,2;5,11,1;6,10,2;1,12,1;1,13,2;6,9,1;7,10,2;8,9,1;7,11,2;8,12,1;7,9,2;7,12,1;9,9,2;8,10,1;";
        String[] chessRecords = str.split(";");
        for (String chessRecord : chessRecords) {
            String[] point = chessRecord.split(",");
            int x = Integer.parseInt(point[0]);
            int y = Integer.parseInt(point[1]);
            int type = Integer.parseInt(point[2]);
            chessData[x][y] = type;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (int[] chessRow : chessData) {
            listTag.add(new IntArrayTag(chessRow));
        }
        getPersistentData().put(CHESS_DATA, listTag);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ListTag listTag = getPersistentData().getList(CHESS_DATA, Tag.TAG_INT_ARRAY);
        for (int i = 0; i < listTag.size(); i++) {
            int[] intArray = listTag.getIntArray(i);
            chessData[i] = intArray;
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
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

    public int[][] getChessData() {
        return chessData;
    }

    public void setChessData(int x, int y, int type) {
        this.chessData[x][y] = type;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.offset(-2, 0, -2), worldPosition.offset(2, 1, 2));
    }
}

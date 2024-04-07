package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGomoku extends TileEntityJoy {
    private static final String CHESS_DATA = "ChessData";    public static final BlockEntityType<TileEntityGomoku> TYPE = BlockEntityType.Builder.of(TileEntityGomoku::new, InitBlocks.GOMOKU.get()).build(null);
    private static final String IN_PROGRESS = "InProgress";
    private static final String PLAYER_TURN = "PlayerTurn";
    private static final String CHESS_COUNTER = "ChessCounter";
    private static final String LATEST_CHESS_POINT = "LatestChessPoint";
    private int[][] chessData = new int[15][15];
    private boolean inProgress = true;
    private boolean playerTurn = true;
    private int chessCounter = 0;
    private Point latestChessPoint = Point.NULL;
    public TileEntityGomoku(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (int[] chessRow : chessData) {
            listTag.add(new IntArrayTag(chessRow));
        }
        getTileData().put(CHESS_DATA, listTag);
        getTileData().putBoolean(IN_PROGRESS, this.inProgress);
        getTileData().putBoolean(PLAYER_TURN, this.playerTurn);
        getTileData().putInt(CHESS_COUNTER, this.chessCounter);
        getTileData().put(LATEST_CHESS_POINT, Point.toTag(this.latestChessPoint));
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ListTag listTag = getTileData().getList(CHESS_DATA, Tag.TAG_INT_ARRAY);
        for (int i = 0; i < listTag.size(); i++) {
            int[] intArray = listTag.getIntArray(i);
            this.chessData[i] = intArray;
        }
        this.inProgress = getTileData().getBoolean(IN_PROGRESS);
        this.playerTurn = getTileData().getBoolean(PLAYER_TURN);
        this.chessCounter = getTileData().getInt(CHESS_COUNTER);
        this.latestChessPoint = Point.fromTag(getTileData().getCompound(LATEST_CHESS_POINT));
    }

    public void reset() {
        this.chessData = new int[15][15];
        this.inProgress = true;
        this.playerTurn = true;
        this.chessCounter = 0;
        this.latestChessPoint = Point.NULL;
    }

    public int[][] getChessData() {
        return chessData;
    }

    public void setChessData(int x, int y, int type) {
        this.chessData[x][y] = type;
        this.latestChessPoint = new Point(x, y, type);
        this.chessCounter += 1;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public int getChessCounter() {
        return chessCounter;
    }

    public Point getLatestChessPoint() {
        return latestChessPoint;
    }


}

package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityGomoku extends TileEntityJoy {
    public static final BlockEntityType<TileEntityGomoku> TYPE = BlockEntityType.Builder.of(TileEntityGomoku::new, InitBlocks.GOMOKU.get()).build(null);

    private static final String CHESS_DATA = "ChessData";
    private static final String IN_PROGRESS = "InProgress";
    private static final String PLAYER_TURN = "PlayerTurn";
    private static final String CHESS_COUNTER = "ChessCounter";
    private static final String LATEST_CHESS_POINT = "LatestChessPoint";

    private byte[][] chessData = new byte[15][15];
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
        for (byte[] chessRow : chessData) {
            listTag.add(new ByteArrayTag(chessRow));
        }
        getPersistentData().put(CHESS_DATA, listTag);
        getPersistentData().putBoolean(IN_PROGRESS, this.inProgress);
        getPersistentData().putBoolean(PLAYER_TURN, this.playerTurn);
        getPersistentData().putInt(CHESS_COUNTER, this.chessCounter);
        getPersistentData().put(LATEST_CHESS_POINT, Point.toTag(this.latestChessPoint));
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ListTag listTag = getPersistentData().getList(CHESS_DATA, Tag.TAG_INT_ARRAY);
        for (int i = 0; i < listTag.size(); i++) {
            int[] intArray = listTag.getIntArray(i);
            byte[] byteArray = new byte[intArray.length];
            for (int j = 0; j < intArray.length; j++) {
                byteArray[j] = (byte) intArray[j];
            }
            this.chessData[i] = byteArray;
        }
        this.inProgress = getPersistentData().getBoolean(IN_PROGRESS);
        this.playerTurn = getPersistentData().getBoolean(PLAYER_TURN);
        this.chessCounter = getPersistentData().getInt(CHESS_COUNTER);
        this.latestChessPoint = Point.fromTag(getPersistentData().getCompound(LATEST_CHESS_POINT));
    }

    public void reset() {
        this.chessData = new byte[15][15];
        this.inProgress = true;
        this.playerTurn = true;
        this.chessCounter = 0;
        this.latestChessPoint = Point.NULL;
    }

    public byte[][] getChessData() {
        return chessData;
    }

    public void setChessData(int x, int y, int type) {
        this.chessData[x][y] = (byte) type;
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

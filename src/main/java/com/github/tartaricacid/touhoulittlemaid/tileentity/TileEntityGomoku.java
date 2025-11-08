package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameEntityBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.GomokuCodec;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TileEntityGomoku extends TileEntityJoy implements IBoardGameEntityBlock {
    public static final BlockEntityType<TileEntityGomoku> TYPE = BlockEntityType.Builder.of(TileEntityGomoku::new, InitBlocks.GOMOKU.get()).build(null);
    private static final String CHESS_DATA = "ChessData";
    private static final String STATUE = "Statue";
    private static final String PLAYER_TURN = "PlayerTurn";
    private static final String CHESS_COUNTER = "ChessCounter";
    private static final String LATEST_CHESS_POINT = "LatestChessPoint";

    private byte[][] chessData = new byte[15][15];
    private int statue = Statue.IN_PROGRESS.ordinal();
    private boolean playerTurn = true;
    private int chessCounter = 0;
    private Point latestChessPoint = Point.NULL;

    public TileEntityGomoku(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        ListTag listTag = new ListTag();
        for (byte[] chessRow : chessData) {
            listTag.add(new ByteArrayTag(chessRow));
        }
        getPersistentData().put(CHESS_DATA, listTag);
        getPersistentData().putInt(STATUE, this.statue);
        getPersistentData().putBoolean(PLAYER_TURN, this.playerTurn);
        getPersistentData().putInt(CHESS_COUNTER, this.chessCounter);
        getPersistentData().put(LATEST_CHESS_POINT, Point.toTag(this.latestChessPoint));
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        ListTag listTag = getPersistentData().getList(CHESS_DATA, Tag.TAG_BYTE_ARRAY);
        for (int i = 0; i < listTag.size(); i++) {
            ByteArrayTag byteArray = (ByteArrayTag) listTag.get(i);
            this.chessData[i] = byteArray.getAsByteArray();
        }
        this.statue = getPersistentData().getInt(STATUE);
        this.playerTurn = getPersistentData().getBoolean(PLAYER_TURN);
        this.chessCounter = getPersistentData().getInt(CHESS_COUNTER);
        this.latestChessPoint = Point.fromTag(getPersistentData().getCompound(LATEST_CHESS_POINT));
    }

    public void reset() {
        this.chessData = new byte[15][15];
        this.statue = Statue.IN_PROGRESS.ordinal();
        this.playerTurn = true;
        this.chessCounter = 0;
        this.latestChessPoint = Point.NULL;
    }

    public byte[][] getChessData() {
        return chessData;
    }

    public void setChessData(List<byte[]> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            this.chessData[i] = arrayList.get(i);
        }
    }

    public void setChessData(int x, int y, int type) {
        this.chessData[x][y] = (byte) type;
        this.latestChessPoint = new Point(x, y, type);
        this.chessCounter += 1;
    }

    // 调试功能，铺满棋盘，只剩三个位置
    public void clickWithDebug() {
        byte[][] drawBoard = new byte[15][15];
        for (int x = 0; x < 15; x++) {
            boolean blackFirst = (x / 2) % 2 == 0;
            for (int y = 0; y < 15; y++) {
                // 14,(12-14) 留空
                if (x == 14 && 12 <= y) {
                    drawBoard[x][y] = Point.EMPTY;
                    continue;
                }
                // 奇偶交替填充，避免出现五连
                if (blackFirst) {
                    drawBoard[x][y] = (y % 2 == 0) ? (byte) Point.BLACK : (byte) Point.WHITE;
                } else {
                    drawBoard[x][y] = (y % 2 == 0) ? (byte) Point.WHITE : (byte) Point.BLACK;
                }
            }
        }
        this.chessData = drawBoard;
        this.latestChessPoint = new Point(14, 10, Point.WHITE);
        this.chessCounter = 15 * 15 - 3;
        this.statue = Statue.IN_PROGRESS.ordinal();
        this.playerTurn = true;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setStatue(Statue statue) {
        this.statue = statue.ordinal();
    }

    public Statue getStatue() {
        return Statue.values()[Mth.clamp(statue, 0, Statue.values().length - 1)];
    }

    public int getChessCounter() {
        return chessCounter;
    }

    public Point getLatestChessPoint() {
        return latestChessPoint;
    }

    public GomokuCodec.StateData getStateData() {
        return new GomokuCodec.StateData(this.chessData, this.chessCounter, this.latestChessPoint);
    }

    public void setStateData(GomokuCodec.StateData stateData) {
        this.chessData = stateData.board();
        this.chessCounter = stateData.turnCount();
        this.latestChessPoint = stateData.latestPoint();
        this.refresh();
    }
}

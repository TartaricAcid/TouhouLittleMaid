package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameEntityBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.GomokuCodec;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TileEntityGomoku extends TileEntityJoy implements IBoardGameEntityBlock {
    public static final BlockEntityType<TileEntityGomoku> TYPE = BlockEntityType.Builder.of(TileEntityGomoku::new, InitBlocks.GOMOKU.get()).build(null);

    private static final String CHESS_DATA = "ChessData";
    private static final String STATUE = "Statue";
    private static final String PLAYER_TURN = "PlayerTurn";
    private static final String CHESS_COUNTER = "ChessCounter";
    private static final String LATEST_CHESS_POINT = "LatestChessPoint";
    private static final String WINNER_TYPE = "WinnerType";

    private int[][] chessData = new int[15][15];
    private int statue = Statue.IN_PROGRESS.ordinal();
    private boolean playerTurn = true;
    private int chessCounter = 0;
    private Point latestChessPoint = Point.NULL;
    private int winnerType = 0;

    private int winStandUpTicks = -1;
    private int scheduledAiMoveId = 0;

    public TileEntityGomoku(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityGomoku te) {
        te.onServerTick((ServerLevel) level);
    }

    private void onServerTick(ServerLevel level) {
        if (this.getStatue() == Statue.IN_PROGRESS && scheduledAiMoveId == 0 && isFullyOccupied(level)) {
            Entity seatEntity = this.playerTurn ? getSeatAEntity(level) : getSeatBEntity(level);
            if (seatEntity instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid) {
                scheduleNextAiMoveIfNeeded(level);
            }
        }
        if (this.getStatue() != Statue.IN_PROGRESS && winStandUpTicks < 0) {
            winStandUpTicks = 100;
        }
        if (winStandUpTicks > 0) {
            winStandUpTicks--;
            if (winStandUpTicks <= 0) {
                if (this.getStatue() == Statue.WIN) {
                    Entity winnerSeat = (this.winnerType == Point.BLACK) ? getSeatAEntity(level) : getSeatBEntity(level);
                    if (winnerSeat instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
                        maid.stopRiding();
                    }
                } else if (this.getStatue() == Statue.DRAW) {
                    Entity seatA = getSeatAEntity(level);
                    Entity seatB = getSeatBEntity(level);
                    if (seatA instanceof EntitySit sitA && sitA.getFirstPassenger() instanceof EntityMaid maidA) {
                        maidA.stopRiding();
                    }
                    if (seatB instanceof EntitySit sitB && sitB.getFirstPassenger() instanceof EntityMaid maidB) {
                        maidB.stopRiding();
                    }
                }
                winStandUpTicks = -1;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (int[] chessRow : chessData) {
            listTag.add(new IntArrayTag(chessRow));
        }
        getPersistentData().put(CHESS_DATA, listTag);
        getPersistentData().putInt(STATUE, this.statue);
        getPersistentData().putBoolean(PLAYER_TURN, this.playerTurn);
        getPersistentData().putInt(CHESS_COUNTER, this.chessCounter);
        getPersistentData().put(LATEST_CHESS_POINT, Point.toTag(this.latestChessPoint));
        getPersistentData().putInt(WINNER_TYPE, this.winnerType);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ListTag listTag = getPersistentData().getList(CHESS_DATA, Tag.TAG_INT_ARRAY);
        for (int i = 0; i < listTag.size(); i++) {
            int[] intArray = listTag.getIntArray(i);
            this.chessData[i] = intArray;
        }
        this.statue = getPersistentData().getInt(STATUE);
        this.playerTurn = getPersistentData().getBoolean(PLAYER_TURN);
        this.chessCounter = getPersistentData().getInt(CHESS_COUNTER);
        this.latestChessPoint = Point.fromTag(getPersistentData().getCompound(LATEST_CHESS_POINT));
        this.winnerType = getPersistentData().getInt(WINNER_TYPE);
    }

    public void reset() {
        this.chessData = new int[15][15];
        this.statue = Statue.IN_PROGRESS.ordinal();
        this.playerTurn = true;
        this.chessCounter = 0;
        this.latestChessPoint = Point.NULL;
        this.winnerType = 0;
        this.scheduledAiMoveId++;
        this.winStandUpTicks = -1;
    }

    public int[][] getChessData() {
        return chessData;
    }

    public void setChessData(int x, int y, int type) {
        this.chessData[x][y] = type;
        this.latestChessPoint = new Point(x, y, type);
        this.chessCounter += 1;
    }

    public void clickWithDebug() {
        int[][] drawBoard = new int[15][15];
        for (int x = 0; x < 15; x++) {
            boolean blackFirst = (x / 2) % 2 == 0;
            for (int y = 0; y < 15; y++) {
                if (x == 14 && 12 <= y) {
                    drawBoard[x][y] = Point.EMPTY;
                    continue;
                }
                if (blackFirst) {
                    drawBoard[x][y] = (y % 2 == 0) ? Point.BLACK : Point.WHITE;
                } else {
                    drawBoard[x][y] = (y % 2 == 0) ? Point.WHITE : Point.BLACK;
                }
            }
        }
        this.chessData = drawBoard;
        this.latestChessPoint = new Point(14, 10, Point.WHITE);
        this.chessCounter = 15 * 15 - 3;
        this.statue = Statue.IN_PROGRESS.ordinal();
        this.playerTurn = true;
        this.winnerType = 0;
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

    public int getWinnerType() {
        return winnerType;
    }

    public void setWinnerType(int winnerType) {
        this.winnerType = winnerType;
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

    public void scheduleNextAiMoveIfNeeded(ServerLevel level) {
        if (this.getStatue() != Statue.IN_PROGRESS) {
            return;
        }
        Entity seatEntity;
        if (this.playerTurn) {
            seatEntity = getSeatAEntity(level);
        } else {
            seatEntity = getSeatBEntity(level);
        }
        if (seatEntity instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
            int delayMs = (int) (Math.random() * 1250) + 250;
            int currentType = this.playerTurn ? Point.BLACK : Point.WHITE;
            int myMoveId = ++this.scheduledAiMoveId;

            int[][] chessDataCopy = deepCopyChessData(this.chessData);
            Point latestPointCopy = this.latestChessPoint;
            int winCount = maid.getGameRecordManager().getGomokuWinCount();

            CompletableFuture.supplyAsync(() -> {
                return MaidGomokuAI.getService(winCount).getPoint(chessDataCopy, latestPointCopy);
            }, CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS, Util.backgroundExecutor()))
            .thenAccept(aiPoint -> {
                if (myMoveId == this.scheduledAiMoveId && this.getStatue() == Statue.IN_PROGRESS) {
                    level.getServer().execute(() -> {
                        if (myMoveId == this.scheduledAiMoveId && this.getStatue() == Statue.IN_PROGRESS) {
                            Entity seat = this.playerTurn ? getSeatAEntity(level) : getSeatBEntity(level);
                            if (seat instanceof EntitySit s && s.getFirstPassenger() instanceof EntityMaid m) {
                                Point point = new Point(aiPoint.x, aiPoint.y, currentType);
                                this.setChessData(point.x, point.y, point.type);
                                Statue result = MaidGomokuAI.getStatue(this.chessData, point);
                                this.setStatue(result);
                                if (result == Statue.WIN) {
                                    this.winnerType = currentType;
                                    m.getGameRecordManager().markStatue(true);
                                    m.getGameRecordManager().increaseGomokuWinCount();
                                    m.swing(InteractionHand.MAIN_HAND);
                                    Entity oppSeat = this.playerTurn ? getSeatBEntity(level) : getSeatAEntity(level);
                                    if (oppSeat instanceof EntitySit os && os.getFirstPassenger() instanceof EntityMaid om) {
                                        om.getGameRecordManager().markStatue(false);
                                    }
                                } else if (result == Statue.IN_PROGRESS) {
                                    this.playerTurn = !this.playerTurn;
                                }
                                level.playSound(null, this.worldPosition, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                                this.refresh();
                                if (result == Statue.IN_PROGRESS) {
                                    scheduleNextAiMoveIfNeeded(level);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private static int[][] deepCopyChessData(int[][] data) {
        int[][] copy = new int[data.length][];
        for (int i = 0; i < data.length; i++) {
            copy[i] = data[i].clone();
        }
        return copy;
    }
}

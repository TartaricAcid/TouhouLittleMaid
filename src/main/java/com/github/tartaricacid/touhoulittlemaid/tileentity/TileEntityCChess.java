package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameEntityBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position;
import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Search;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TileEntityCChess extends TileEntityJoy implements IBoardGameEntityBlock {
    public static final BlockEntityType<TileEntityCChess> TYPE = BlockEntityType.Builder.of(TileEntityCChess::new, InitBlocks.CCHESS.get()).build(null);

    private static final String CHESS_DATA = "ChessData";
    private static final String CHESS_COUNTER = "ChessCounter";
    private static final String SELECT_CHESS_POINT = "SelectChessPoint";
    private static final String CHECKMATE = "Checkmate";
    private static final String REPEAT = "Repeat";
    private static final String MOVE_NUMBER_LIMIT = "MoveNumberLimit";

    private final Position chessData;
    private int chessCounter = 0;
    private int selectChessPoint = 0;
    private boolean checkmate = false;
    private boolean repeat = false;
    private boolean moveNumberLimit = false;
    private int winStandUpTicks = -1;
    private int scheduledAiMoveId = 0;

    public TileEntityCChess(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
        this.chessData = new Position();
        this.chessData.fromFen(CChessUtil.INIT);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityCChess te) {
        te.onServerTick((ServerLevel) level);
    }

    private void onServerTick(ServerLevel level) {
        if (!this.isGameOver() && scheduledAiMoveId == 0 && isFullyOccupied(level)) {
            Entity seatEntity = this.isPlayerTurn() ? getSeatAEntity(level) : getSeatBEntity(level);
            if (seatEntity instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid) {
                scheduleNextAiMoveIfNeeded(level);
            }
        }
        if (this.isGameOver() && winStandUpTicks < 0) {
            winStandUpTicks = 100;
        }
        if (winStandUpTicks > 0) {
            winStandUpTicks--;
            if (winStandUpTicks <= 0) {
                if (this.checkmate) {
                    Entity winnerSeat = this.isPlayerTurn() ? getSeatBEntity(level) : getSeatAEntity(level);
                    if (winnerSeat instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
                        maid.stopRiding();
                    }
                } else {
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

    public boolean isGameOver() {
        return checkmate || repeat || moveNumberLimit;
    }

    public void setEndgame(String endgame) {
        this.chessData.fromFen(endgame);
        this.refresh();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        CompoundTag data = getPersistentData();
        data.putString(CHESS_DATA, chessData.toFen());
        data.putInt(CHESS_COUNTER, chessCounter);
        data.putInt(SELECT_CHESS_POINT, selectChessPoint);
        data.putBoolean(CHECKMATE, checkmate);
        data.putBoolean(REPEAT, repeat);
        data.putBoolean(MOVE_NUMBER_LIMIT, moveNumberLimit);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        CompoundTag data = getPersistentData();
        chessCounter = data.getInt(CHESS_COUNTER);
        selectChessPoint = data.getInt(SELECT_CHESS_POINT);
        chessData.fromFen(data.getString(CHESS_DATA));
        checkmate = data.getBoolean(CHECKMATE);
        repeat = data.getBoolean(REPEAT);
        moveNumberLimit = data.getBoolean(MOVE_NUMBER_LIMIT);
    }

    public void reset() {
        this.chessCounter = 0;
        this.selectChessPoint = 0;
        this.chessData.fromFen(CChessUtil.INIT);
        this.checkmate = false;
        this.repeat = false;
        this.moveNumberLimit = false;
        this.scheduledAiMoveId++;
        this.winStandUpTicks = -1;
    }

    public Position getChessData() {
        return chessData;
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public void setCheckmate(boolean checkmate) {
        this.checkmate = checkmate;
    }

    public boolean isPlayerTurn() {
        return CChessUtil.isPlayer(this.chessData);
    }

    public int getChessCounter() {
        return chessCounter;
    }

    public void addChessCounter() {
        this.chessCounter += 1;
    }

    public int getSelectChessPoint() {
        return selectChessPoint;
    }

    public void setSelectChessPoint(int selectChessPoint) {
        this.selectChessPoint = selectChessPoint;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isMoveNumberLimit() {
        return moveNumberLimit;
    }

    public void setMoveNumberLimit(boolean moveNumberLimit) {
        this.moveNumberLimit = moveNumberLimit;
    }

    public void scheduleNextAiMoveIfNeeded(ServerLevel level) {
        if (this.isGameOver()) {
            return;
        }
        Entity seatEntity;
        if (this.isPlayerTurn()) {
            seatEntity = getSeatAEntity(level);
        } else {
            seatEntity = getSeatBEntity(level);
        }
        if (seatEntity instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
            int delayMs = (int) (Math.random() * 1250) + 250;
            int myMoveId = ++this.scheduledAiMoveId;

            String fenCopy = this.chessData.toFen();
            int depth = getAiDepth(maid);

            CompletableFuture.supplyAsync(() -> {
                Position tempPos = new Position();
                tempPos.fromFen(fenCopy);
                Search search = new Search(tempPos, 16);
                return search.searchMain(depth, 800);
            }, CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS, Util.backgroundExecutor()))
            .thenAccept(move -> {
                if (myMoveId == this.scheduledAiMoveId && !this.isGameOver() && move > 0) {
                    level.getServer().execute(() -> {
                        if (myMoveId == this.scheduledAiMoveId && !this.isGameOver()) {
                            Entity seat = this.isPlayerTurn() ? getSeatAEntity(level) : getSeatBEntity(level);
                            if (seat instanceof EntitySit s && s.getFirstPassenger() instanceof EntityMaid m) {
                                if (this.chessData.makeMove(move)) {
                                    if (this.chessData.captured()) {
                                        this.chessData.setIrrev();
                                    }
                                }
                                this.addChessCounter();
                                this.setSelectChessPoint(Position.DST(move));

                                if (this.chessData.isMate()) {
                                    this.setCheckmate(true);
                                    m.getGameRecordManager().markStatue(true);
                                    Entity oppSeat = this.isPlayerTurn() ? getSeatAEntity(level) : getSeatBEntity(level);
                                    if (oppSeat instanceof EntitySit os && os.getFirstPassenger() instanceof EntityMaid om) {
                                        om.getGameRecordManager().markStatue(false);
                                    }
                                } else {
                                    if (CChessUtil.reachMoveLimit(this.chessData)) {
                                        this.setMoveNumberLimit(true);
                                    } else if (CChessUtil.isRepeat(this.chessData)) {
                                        this.setRepeat(true);
                                    }
                                }
                                m.swing(InteractionHand.MAIN_HAND);
                                level.playSound(null, this.worldPosition, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                                this.refresh();

                                if (!this.isGameOver()) {
                                    scheduleNextAiMoveIfNeeded(level);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private static int getAiDepth(EntityMaid maid) {
        int wins = maid.getGameRecordManager().getGomokuWinCount();
        if (wins <= 2) return 2;
        else if (wins <= 8) return 6;
        else if (wins <= 24) return 10;
        else return 14;
    }
}

package com.github.tartaricacid.touhoulittlemaid.tileentity;

import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameEntityBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCChess extends TileEntityJoy implements IBoardGameEntityBlock {
    public static final BlockEntityType<TileEntityCChess> TYPE = BlockEntityType.Builder.of(TileEntityCChess::new, InitBlocks.CCHESS.get()).build(null);

    private static final String CHESS_DATA = "ChessData";
    private static final String CHESS_COUNTER = "ChessCounter";
    private static final String SELECT_CHESS_POINT = "SelectChessPoint";
    private static final String CHECKMATE = "Checkmate";
    private static final String REPEAT = "Repeat";
    private static final String MOVE_NUMBER_LIMIT = "MoveNumberLimit";

    private final Position chessData;

    // 回合计数器
    private int chessCounter = 0;
    // 当前选中的棋子
    private int selectChessPoint = 0;
    // 将死（依据下棋方，判断谁输谁赢）
    private boolean checkmate = false;
    // 长打（判和）
    private boolean repeat = false;
    // 60 回自然限着（判和）
    private boolean moveNumberLimit = false;

    public TileEntityCChess(BlockPos pos, BlockState blockState) {
        super(TYPE, pos, blockState);
        this.chessData = new Position();
        this.chessData.fromFen(CChessUtil.INIT);
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
}

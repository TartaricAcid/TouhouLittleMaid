package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position;
import com.github.tartaricacid.touhoulittlemaid.block.properties.GomokuPart;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.item.ItemBoardState;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityWChess;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class BlockWChess extends BlockJoy implements IBoardGameBlock {
    public static final EnumProperty<GomokuPart> PART = EnumProperty.create("part", GomokuPart.class);
    public static final VoxelShape AABB = Block.box(0, 0, 0, 16, 2, 16);

    public BlockWChess() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, GomokuPart.CENTER).setValue(FACING, Direction.NORTH));
    }

    public static void maidMove(ServerPlayer player, Level level, BlockPos pos, int move, boolean maidLost, boolean playerLost) {
    }

    private static void handleWChessRemove(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
            BlockEntity te = world.getBlockEntity(centerPos);
            popResource(world, centerPos, InitItems.WCHESS.get().getDefaultInstance());
            if (te instanceof TileEntityWChess) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        world.setBlockAndUpdate(centerPos.offset(i, 0, j), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public void startMaidSit(EntityMaid maid, BlockState state, Level worldIn, BlockPos pos) {
        if (worldIn instanceof ServerLevel serverLevel && worldIn.getBlockEntity(pos) instanceof TileEntityJoy joy) {
            int occupiedCount = joy.getOccupiedSeatCount(serverLevel);
            if (occupiedCount >= 2) {
                return;
            }
            boolean useSeatA = occupiedCount == 0 || serverLevel.getEntity(joy.getSitId()) == null;
            createEntitySit(serverLevel, pos, state, useSeatA, maid);
            if (joy.isFullyOccupied(serverLevel)) {
                if (serverLevel.getBlockEntity(pos) instanceof TileEntityWChess chess) {
                    if (chess.isGameOver()) {
                        chess.reset();
                    }
                    chess.scheduleNextAiMoveIfNeeded(serverLevel);
                }
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(type, TileEntityWChess.TYPE, TileEntityWChess::serverTick);
    }

    private void createEntitySit(ServerLevel level, BlockPos pos, BlockState state, boolean isSeatA, Entity rider) {
        TileEntityJoy joy = (TileEntityJoy) level.getBlockEntity(pos);
        Direction face = isSeatA ? state.getValue(FACING) : state.getValue(FACING).getOpposite();
        Vec3 position = new Vec3(0.5 + face.getStepX() * 2, 0.1, 0.5 + face.getStepZ() * 2);
        EntitySit seat = new EntitySit(level, Vec3.atLowerCornerWithOffset(pos, position.x, position.y, position.z), this.getTypeName(), pos);
        seat.setYRot(face.getOpposite().toYRot() + this.sitYRot());
        level.addFreshEntity(seat);
        if (isSeatA) {
            joy.setSitId(seat.getUUID());
        } else {
            joy.setSitIdB(seat.getUUID());
        }
        joy.setChanged();
        rider.startRiding(seat);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handleWChessRemove(world, pos, state);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        handleWChessRemove(world, pos, state);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos centerPos = context.getClickedPos();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = centerPos.offset(i, 0, j);
                if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = pos.offset(i, 0, j);
                GomokuPart part = GomokuPart.getPartByPos(i, j);
                if (part != null && !part.isCenter()) {
                    worldIn.setBlock(searchPos, state.setValue(PART, part), Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level instanceof ServerLevel serverLevel && hand == InteractionHand.MAIN_HAND) {
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
            BlockEntity te = level.getBlockEntity(centerPos);

            if (!(te instanceof TileEntityWChess chess)) {
                return InteractionResult.FAIL;
            }

            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.is(InitItems.WCHESS_BOARD_STATE.get())) {
                String[] boardState = ItemBoardState.getState(heldItem);
                if (boardState == null) {
                    return InteractionResult.PASS;
                }
                String data = boardState[0];
                if (StringUtils.isEmpty(data)) {
                    return InteractionResult.PASS;
                }
                chess.setEndgame(data);
                level.playSound(null, pos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }

            if (!heldItem.isEmpty()) {
                return InteractionResult.PASS;
            }

            Direction facing = state.getValue(FACING);
            Vec3 clickPos = hit.getLocation()
                    .subtract(pos.getX(), pos.getY(), pos.getZ())
                    .add(part.getPosX() - 0.5, 0, part.getPosY() - 0.5)
                    .yRot(facing.toYRot() * Mth.DEG_TO_RAD);

            boolean clickResetArea = WChessUtil.isClickResetArea(clickPos);
            if (clickResetArea) {
                level.playSound(null, centerPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                chess.reset();
                chess.refresh();

                Entity seatA = serverLevel.getEntity(chess.getSitId());
                if (seatA != null && seatA.isAlive() && seatA.getFirstPassenger() instanceof EntityMaid maidA) {
                    maidA.getGameRecordManager().resetStatue();
                }
                Entity seatB = serverLevel.getEntity(chess.getSitIdB());
                if (seatB != null && seatB.isAlive() && seatB.getFirstPassenger() instanceof EntityMaid maidB) {
                    maidB.getGameRecordManager().resetStatue();
                }

                return InteractionResult.SUCCESS;
            }

            int occupiedCount = chess.getOccupiedSeatCount(serverLevel);
            if (occupiedCount < 2) {
                return trySitPlayer(serverLevel, centerPos, state, player, chess);
            }

            return handleGameMove(serverLevel, centerPos, clickPos, player, chess);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult trySitPlayer(ServerLevel serverLevel, BlockPos pos, BlockState state, Player player, TileEntityWChess chess) {
        boolean seatAEmpty = serverLevel.getEntity(chess.getSitId()) == null;
        boolean seatBEmpty = serverLevel.getEntity(chess.getSitIdB()) == null;
        if (!seatAEmpty && !seatBEmpty) {
            return InteractionResult.PASS;
        }
        boolean useSeatA = seatAEmpty;
        createEntitySit(serverLevel, pos, state, useSeatA, player);
        if (chess.isFullyOccupied(serverLevel)) {
            if (chess.isGameOver()) {
                chess.reset();
            }
            chess.scheduleNextAiMoveIfNeeded(serverLevel);
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult handleGameMove(ServerLevel serverLevel, BlockPos pos, Vec3 clickPos, Player player, TileEntityWChess chess) {
        if (chess.isGameOver()) {
            return InteractionResult.FAIL;
        }

        Entity seatA = chess.getSeatAEntity(serverLevel);
        Entity seatB = chess.getSeatBEntity(serverLevel);
        boolean isPlayerOnSeatA = seatA instanceof EntitySit sitA && sitA.getFirstPassenger() == player;
        boolean isPlayerOnSeatB = seatB instanceof EntitySit sitB && sitB.getFirstPassenger() == player;
        if (!isPlayerOnSeatA && !isPlayerOnSeatB) {
            return InteractionResult.PASS;
        }

        boolean playerIsWhite = isPlayerOnSeatA;
        boolean isWhiteTurn = chess.isPlayerTurn();
        if (isWhiteTurn != playerIsWhite) {
            return InteractionResult.FAIL;
        }

        int nowClick = WChessUtil.getClickPosition(clickPos);
        if (nowClick < 0 || !Position.IN_BOARD(nowClick)) {
            return InteractionResult.PASS;
        }

        Position chessData = chess.getChessData();
        byte[] squares = chessData.squares;
        int preClick = chess.getSelectChessPoint();
        if (preClick < 0 || squares.length <= preClick) {
            preClick = 0;
        }
        byte prePiece = squares[preClick];
        byte nowPiece = squares[nowClick];

        boolean ownPrePiece = prePiece > 0 && (playerIsWhite ? WChessUtil.isWhite(prePiece) : WChessUtil.isBlack(prePiece));
        boolean ownNowPiece = nowPiece > 0 && (playerIsWhite ? WChessUtil.isWhite(nowPiece) : WChessUtil.isBlack(nowPiece));

        if (!ownPrePiece) {
            if (ownNowPiece) {
                chess.setSelectChessPoint(nowClick);
                chess.refresh();
                serverLevel.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + serverLevel.random.nextFloat() * 0.4F);
            }
            return InteractionResult.SUCCESS;
        }

        if (ownNowPiece && preClick != nowClick) {
            chess.setSelectChessPoint(nowClick);
            chess.refresh();
            serverLevel.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + serverLevel.random.nextFloat() * 0.4F);
            return InteractionResult.SUCCESS;
        }

        int move = Position.MOVE(preClick, nowClick);
        if (!chessData.legalMove(move)) {
            return InteractionResult.FAIL;
        }

        boolean notChecked = chessData.makeMove(move);
        if (!notChecked) {
            player.sendSystemMessage(Component.translatable("message.touhou_little_maid.cchess.check"));
            serverLevel.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.get(), SoundSource.BLOCKS, 1.0f, 0.8F + serverLevel.random.nextFloat() * 0.4F);
            return InteractionResult.FAIL;
        }

        int pcSrc = chessData.squares[Position.SRC(move)];
        if (chessData.captured() || Position.PIECE_TYPE(pcSrc) == Position.PIECE_PAWN) {
            chessData.setIrrev();
        }
        chess.addChessCounter();
        chess.setSelectChessPoint(nowClick);

        if (chessData.isMate()) {
            chess.setCheckmate(true);
            Entity opponentSeat = playerIsWhite ? seatB : seatA;
            if (opponentSeat instanceof EntitySit oppSit && oppSit.getFirstPassenger() instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                maid.getFavorabilityManager().apply(Type.WCHESS_WIN);
                maid.getGameRecordManager().markStatue(false);
                if (player instanceof ServerPlayer serverPlayer) {
                    InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.WIN_WCHESS);
                }
            }
        } else {
            if (WChessUtil.reachMoveLimit(chessData)) {
                chess.setMoveNumberLimit(true);
            } else if (WChessUtil.isRepeat(chessData)) {
                chess.setRepeat(true);
            }
        }

        chess.refresh();
        serverLevel.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + serverLevel.random.nextFloat() * 0.4F);

        if (!chess.isGameOver()) {
            chess.scheduleNextAiMoveIfNeeded(serverLevel);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected Vec3 sitPosition() {
        return Vec3.ZERO;
    }

    @Override
    protected String getTypeName() {
        return Type.GOMOKU.getTypeName();
    }

    @Override
    protected int sitYRot() {
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).isCenter()) {
            return new TileEntityWChess(pos, state);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AABB;
    }
}

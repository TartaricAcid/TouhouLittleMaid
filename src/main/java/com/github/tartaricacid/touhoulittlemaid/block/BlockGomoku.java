package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.GomokuCodec;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.block.properties.GomokuPart;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.item.ItemBoardState;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class BlockGomoku extends BlockJoy implements IBoardGameBlock {
    public static final EnumProperty<GomokuPart> PART = EnumProperty.create("part", GomokuPart.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape LEFT_UP = Block.box(8, 0, 8, 16, 2, 16);
    public static final VoxelShape LEFT_UP_WITH_BOX = Shapes.or(LEFT_UP, Block.box(11, 0, 2, 16, 4, 7));
    public static final VoxelShape UP = Block.box(0, 0, 8, 16, 2, 16);
    public static final VoxelShape RIGHT_UP = Block.box(0, 0, 8, 8, 2, 16);
    public static final VoxelShape RIGHT_UP_WITH_BOX = Shapes.or(RIGHT_UP, Block.box(9, 0, 11, 14, 4, 16));
    public static final VoxelShape LEFT_CENTER = Block.box(8, 0, 0, 16, 2, 16);
    public static final VoxelShape CENTER = Block.box(0, 0, 0, 16, 2, 16);
    public static final VoxelShape RIGHT_CENTER = Block.box(0, 0, 0, 8, 2, 16);
    public static final VoxelShape LEFT_DOWN = Block.box(8, 0, 0, 16, 2, 8);
    public static final VoxelShape LEFT_DOWN_WITH_BOX = Shapes.or(LEFT_DOWN, Block.box(2, 0, 0, 7, 4, 5));
    public static final VoxelShape DOWN = Block.box(0, 0, 0, 16, 2, 8);
    public static final VoxelShape RIGHT_DOWN = Block.box(0, 0, 0, 8, 2, 8);
    public static final VoxelShape RIGHT_DOWN_WITH_BOX = Shapes.or(RIGHT_DOWN, Block.box(0, 0, 9, 5, 4, 14));

    public BlockGomoku() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, GomokuPart.CENTER).setValue(FACING, Direction.NORTH));
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

    private static void handleGomokuRemove(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
            BlockEntity te = world.getBlockEntity(centerPos);
            popResource(world, centerPos, InitItems.GOMOKU.get().getDefaultInstance());
            if (te instanceof TileEntityGomoku) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        world.setBlockAndUpdate(centerPos.offset(i, 0, j), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    @Nullable
    private static int[] getChessPos(double x, double y, GomokuPart part) {
        switch (part) {
            case LEFT_UP -> {
                return getData(x, y, 0.505, 0.505, 0.54, 0.54, 0, 0);
            }
            case UP -> {
                return getData(x, y, 0.037, 0.505, 0.08, 0.54, 4, 0);
            }
            case RIGHT_UP -> {
                return getData(x, y, -0.037, 0.505, -0.01, 0.54, 11, 0);
            }
            case LEFT_CENTER -> {
                return getData(x, y, 0.505, 0.037, 0.54, 0.07, 0, 4);
            }
            case CENTER -> {
                return getData(x, y, 0.037, 0.037, 0.08, 0.07, 4, 4);
            }
            case RIGHT_CENTER -> {
                return getData(x, y, -0.037, 0.037, -0.01, 0.07, 11, 4);
            }
            case LEFT_DOWN -> {
                return getData(x, y, 0.505, 0, 0.54, 0, 0, 11);
            }
            case DOWN -> {
                return getData(x, y, 0.037, 0, 0.08, 0, 4, 11);
            }
            case RIGHT_DOWN -> {
                return getData(x, y, -0.037, 0, -0.01, 0, 11, 11);
            }
            default -> {
                return null;
            }
        }
    }

    private static boolean isClickChessBox(double x, double y, GomokuPart part, Direction direction) {
        if (direction.getAxis() == Direction.Axis.Z) {
            if (part == GomokuPart.RIGHT_UP) {
                return 0.5625 <= x && x <= 0.875 && 0.6875 <= y && y <= 1;
            }
            if (part == GomokuPart.LEFT_DOWN) {
                return 0.125 <= x && x <= 0.4375 && 0 <= y && y <= 0.3125;
            }
        }
        if (direction.getAxis() == Direction.Axis.X) {
            if (part == GomokuPart.LEFT_UP) {
                return 0.6875 <= x && x <= 1 && 0.125 <= y && y <= 0.4375;
            }
            if (part == GomokuPart.RIGHT_DOWN) {
                return 0 <= x && x <= 0.3125 && 0.5625 <= y && y <= 0.875;
            }
        }
        return false;
    }

    @Nullable
    private static int[] getData(double x, double y, double xOffset, double yOffset, double xStartOffset, double yStartOffset, int xIndexOffset, int yIndexOffset) {
        int xIndex = (int) ((x - xOffset) / 0.1316);
        int yIndex = (int) ((y - yOffset) / 0.1316);
        double xStart = xStartOffset + xIndex * 0.1316;
        double xEnd = xStart + 0.07;
        double yStart = yStartOffset + yIndex * 0.1316;
        double yEnd = yStart + 0.07;
        xIndex += xIndexOffset;
        yIndex += yIndexOffset;
        boolean checkIndex = 0 <= xIndex && xIndex <= 14 && 0 <= yIndex && yIndex <= 14;
        boolean checkClick = xStart < x && x < xEnd && yStart < y && y < yEnd;
        if (checkIndex && checkClick) {
            return new int[]{xIndex, yIndex};
        }
        return null;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handleGomokuRemove(world, pos, state);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        handleGomokuRemove(world, pos, state);
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
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
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
            if (!(te instanceof TileEntityGomoku gomoku)) {
                return InteractionResult.FAIL;
            }

            Vec3 location = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
            Direction facing = state.getValue(FACING);

            if (player.getAbilities().instabuild) {
                InteractionResult success = this.onCreativePlayerClick(level, pos, player, gomoku, centerPos, location, part, facing);
                if (success != null) {
                    return success;
                }
            }

            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.is(InitItems.GOMOKU_BOARD_STATE.get())) {
                String[] boardState = ItemBoardState.getState(heldItem);
                if (boardState == null) {
                    return InteractionResult.PASS;
                }
                String data = boardState[0];
                if (StringUtils.isEmpty(data)) {
                    return InteractionResult.PASS;
                }
                gomoku.setStateData(GomokuCodec.decode(data));
                level.playSound(null, pos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }

            if (!player.getMainHandItem().isEmpty()) {
                return InteractionResult.PASS;
            }

            if (isClickChessBox(location.x, location.z, part, facing)) {
                level.playSound(null, centerPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                gomoku.reset();
                gomoku.refresh();

                Entity sitEntityA = serverLevel.getEntity(gomoku.getSitId());
                if (sitEntityA != null && sitEntityA.isAlive() && sitEntityA.getFirstPassenger() instanceof EntityMaid maidA) {
                    maidA.getGameRecordManager().resetStatue();
                }
                Entity sitEntityB = serverLevel.getEntity(gomoku.getSitIdB());
                if (sitEntityB != null && sitEntityB.isAlive() && sitEntityB.getFirstPassenger() instanceof EntityMaid maidB) {
                    maidB.getGameRecordManager().resetStatue();
                }

                return InteractionResult.SUCCESS;
            }

            int occupiedCount = gomoku.getOccupiedSeatCount(serverLevel);

            if (occupiedCount < 2) {
                return trySitPlayer(serverLevel, pos, state, player, gomoku);
            }

            return handleGameMove(serverLevel, pos, state, player, gomoku, location, part);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult trySitPlayer(ServerLevel serverLevel, BlockPos pos, BlockState state, Player player, TileEntityGomoku gomoku) {
        boolean seatAEmpty = serverLevel.getEntity(gomoku.getSitId()) == null;
        boolean seatBEmpty = serverLevel.getEntity(gomoku.getSitIdB()) == null;

        if (!seatAEmpty && !seatBEmpty) {
            return InteractionResult.PASS;
        }

        boolean useSeatA = seatAEmpty;
        createEntitySit(serverLevel, pos, state, useSeatA, player);

        if (gomoku.isFullyOccupied(serverLevel)) {
            if (gomoku.getStatue() != Statue.IN_PROGRESS) {
                gomoku.reset();
            }
            gomoku.scheduleNextAiMoveIfNeeded(serverLevel);
        }

        return InteractionResult.SUCCESS;
    }

    private InteractionResult handleGameMove(ServerLevel serverLevel, BlockPos pos, BlockState state, Player player, TileEntityGomoku gomoku, Vec3 location, GomokuPart part) {
        if (gomoku.getStatue() != Statue.IN_PROGRESS) {
            return InteractionResult.FAIL;
        }

        Entity seatA = gomoku.getSeatAEntity(serverLevel);
        Entity seatB = gomoku.getSeatBEntity(serverLevel);

        boolean isPlayerOnSeatA = seatA instanceof EntitySit sitA && sitA.getFirstPassenger() == player;
        boolean isPlayerOnSeatB = seatB instanceof EntitySit sitB && sitB.getFirstPassenger() == player;

        if (!isPlayerOnSeatA && !isPlayerOnSeatB) {
            return InteractionResult.PASS;
        }

        boolean isBlackTurn = gomoku.isPlayerTurn();
        boolean playerIsBlack = isPlayerOnSeatA;

        if (isBlackTurn != playerIsBlack) {
            return InteractionResult.FAIL;
        }

        int[] clickPos = getChessPos(location.x, location.z, part);
        if (clickPos == null) {
            return InteractionResult.FAIL;
        }

        int pieceType = playerIsBlack ? Point.BLACK : Point.WHITE;
        Point playerPoint = new Point(clickPos[0], clickPos[1], pieceType);
        int[][] chessData = gomoku.getChessData();

        if (gomoku.getStatue() == Statue.IN_PROGRESS && chessData[playerPoint.x][playerPoint.y] == Point.EMPTY) {
            gomoku.setChessData(playerPoint.x, playerPoint.y, playerPoint.type);
            Statue statue = MaidGomokuAI.getStatue(chessData, playerPoint);

            if (statue == Statue.WIN) {
                gomoku.setWinnerType(playerPoint.type);
                // 检查对手是否是自己的女仆
                Entity opponentSeat = playerIsBlack ? seatB : seatA;
                if (opponentSeat instanceof EntitySit oppSit && oppSit.getFirstPassenger() instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                    maid.getFavorabilityManager().apply(Type.GOMOKU_WIN);
                    maid.getGameRecordManager().markStatue(false);
                    int rankBefore = MaidGomokuAI.getRank(maid);
                    maid.getGameRecordManager().increaseGomokuWinCount();
                    int rankAfter = MaidGomokuAI.getRank(maid);
                    if (rankBefore < rankAfter) {
                        NetworkHandler.sendToClientPlayer(new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.RANK_UP), player);
                    }
                    if (player instanceof ServerPlayer serverPlayer) {
                        InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.WIN_GOMOKU);
                    }
                }
            }

            gomoku.setStatue(statue);
            serverLevel.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + serverLevel.random.nextFloat() * 0.4F);

            if (gomoku.getStatue() == Statue.IN_PROGRESS) {
                gomoku.setPlayerTurn(!gomoku.isPlayerTurn());
                gomoku.scheduleNextAiMoveIfNeeded(serverLevel);
            }

            gomoku.refresh();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    private InteractionResult onCreativePlayerClick(Level level, BlockPos pos, Player player, TileEntityGomoku gomoku,
                                                    BlockPos centerPos, Vec3 location, GomokuPart part, Direction facing) {
        Item item = player.getMainHandItem().getItem();

        // 拿着御币点击，那么铺满棋盘，只剩三个位置，用来调试满棋盘
        if (item instanceof ItemHakureiGohei) {
            gomoku.clickWithDebug();
            gomoku.refresh();
            level.playSound(null, centerPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        // 如果是木棍，那么就是预设棋局模式
        if (item == Items.STICK) {
            // 如果玩家点击的是棋盒，导出
            if (isClickChessBox(location.x, location.z, part, facing)) {
                String result = GomokuCodec.encode(gomoku.getStateData());
                MutableComponent component = ComponentUtils.copyOnClickText(result);
                player.sendSystemMessage(component);
                return InteractionResult.SUCCESS;
            }

            // 否则就是预设棋局
            int[] clickPos = getChessPos(location.x, location.z, part);
            if (clickPos == null) {
                return InteractionResult.FAIL;
            }
            int type = gomoku.isPlayerTurn() ? Point.BLACK : Point.WHITE;
            Point playerPoint = new Point(clickPos[0], clickPos[1], type);
            gomoku.setChessData(playerPoint.x, playerPoint.y, playerPoint.type);
            level.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
            gomoku.setPlayerTurn(!gomoku.isPlayerTurn());
            gomoku.refresh();
            return InteractionResult.SUCCESS;
        }

        return null;
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
                if (serverLevel.getBlockEntity(pos) instanceof TileEntityGomoku gomoku) {
                    if (gomoku.getStatue() != Statue.IN_PROGRESS) {
                        gomoku.reset();
                    }
                    gomoku.scheduleNextAiMoveIfNeeded(serverLevel);
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
        return createTickerHelper(type, TileEntityGomoku.TYPE, TileEntityGomoku::serverTick);
    }

    private void createEntitySit(ServerLevel level, BlockPos pos, BlockState state, boolean isSeatA, Entity rider) {
        TileEntityJoy joy = (TileEntityJoy) level.getBlockEntity(pos);
        Direction face = isSeatA ? state.getValue(FACING).getClockWise() : state.getValue(FACING).getCounterClockWise();
        Vec3 position = new Vec3(0.5 + face.getStepX() * 1.5, 0.1, 0.5 + face.getStepZ() * 1.5);
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).isCenter()) {
            return new TileEntityGomoku(pos, state);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch (state.getValue(PART)) {
            case LEFT_UP -> {
                if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
                    return LEFT_UP_WITH_BOX;
                } else {
                    return LEFT_UP;
                }
            }
            case UP -> {
                return UP;
            }
            case RIGHT_UP -> {
                if (state.getValue(FACING).getAxis() == Direction.Axis.Z) {
                    return RIGHT_UP_WITH_BOX;
                } else {
                    return RIGHT_UP;
                }
            }
            case LEFT_CENTER -> {
                return LEFT_CENTER;
            }
            case RIGHT_CENTER -> {
                return RIGHT_CENTER;
            }
            case LEFT_DOWN -> {
                if (state.getValue(FACING).getAxis() == Direction.Axis.Z) {
                    return LEFT_DOWN_WITH_BOX;
                } else {
                    return LEFT_DOWN;
                }
            }
            case DOWN -> {
                return DOWN;
            }
            case RIGHT_DOWN -> {
                if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
                    return RIGHT_DOWN_WITH_BOX;
                } else {
                    return RIGHT_DOWN;
                }
            }
            default -> {
                return CENTER;
            }
        }
    }
}

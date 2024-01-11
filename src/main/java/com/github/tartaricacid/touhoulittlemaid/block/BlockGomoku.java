package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.block.properties.GomokuPart;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ChessDataToClientMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class BlockGomoku extends BlockJoy {
    public static final EnumProperty<GomokuPart> PART = EnumProperty.create("part", GomokuPart.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape LEFT_UP = Block.box(8, 0, 8, 16, 2, 16);
    public static final VoxelShape LEFT_UP_WITH_BOX = VoxelShapes.or(LEFT_UP, Block.box(11, 0, 2, 16, 4, 7));
    public static final VoxelShape UP = Block.box(0, 0, 8, 16, 2, 16);
    public static final VoxelShape RIGHT_UP = Block.box(0, 0, 8, 8, 2, 16);
    public static final VoxelShape RIGHT_UP_WITH_BOX = VoxelShapes.or(RIGHT_UP, Block.box(9, 0, 11, 14, 4, 16));
    public static final VoxelShape LEFT_CENTER = Block.box(8, 0, 0, 16, 2, 16);
    public static final VoxelShape CENTER = Block.box(0, 0, 0, 16, 2, 16);
    public static final VoxelShape RIGHT_CENTER = Block.box(0, 0, 0, 8, 2, 16);
    public static final VoxelShape LEFT_DOWN = Block.box(8, 0, 0, 16, 2, 8);
    public static final VoxelShape LEFT_DOWN_WITH_BOX = VoxelShapes.or(LEFT_DOWN, Block.box(2, 0, 0, 7, 4, 5));
    public static final VoxelShape DOWN = Block.box(0, 0, 0, 16, 2, 8);
    public static final VoxelShape RIGHT_DOWN = Block.box(0, 0, 0, 8, 2, 8);
    public static final VoxelShape RIGHT_DOWN_WITH_BOX = VoxelShapes.or(RIGHT_DOWN, Block.box(0, 0, 9, 5, 4, 14));

    public BlockGomoku() {
        super(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 3.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, GomokuPart.CENTER).setValue(FACING, Direction.NORTH));
    }

    private static void handleGomokuRemove(World world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vector3i(part.getPosX(), 0, part.getPosY()));
            TileEntity te = world.getBlockEntity(centerPos);
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
            case LEFT_UP:
                return getData(x, y, 0.505, 0.505, 0.54, 0.54, 0, 0);
            case UP:
                return getData(x, y, 0.037, 0.505, 0.08, 0.54, 4, 0);
            case RIGHT_UP:
                return getData(x, y, -0.037, 0.505, -0.01, 0.54, 11, 0);
            case LEFT_CENTER:
                return getData(x, y, 0.505, 0.037, 0.54, 0.07, 0, 4);
            case CENTER:
                return getData(x, y, 0.037, 0.037, 0.08, 0.07, 4, 4);
            case RIGHT_CENTER:
                return getData(x, y, -0.037, 0.037, -0.01, 0.07, 11, 4);
            case LEFT_DOWN:
                return getData(x, y, 0.505, 0, 0.54, 0, 0, 11);
            case DOWN:
                return getData(x, y, 0.037, 0, 0.08, 0, 4, 11);
            case RIGHT_DOWN:
                return getData(x, y, -0.037, 0, -0.01, 0, 11, 11);
            default:
                return null;
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
    protected Vector3d sitPosition() {
        return Vector3d.ZERO;
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
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        handleGomokuRemove(world, pos, state);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        handleGomokuRemove(world, pos, state);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
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
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = pos.offset(i, 0, j);
                GomokuPart part = GomokuPart.getPartByPos(i, j);
                if (part != null && !part.isCenter()) {
                    worldIn.setBlock(searchPos, state.setValue(PART, part), Constants.BlockFlags.DEFAULT);
                }
            }
        }
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (level instanceof ServerWorld && hand == Hand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            ServerWorld serverLevel = (ServerWorld) level;
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vector3i(part.getPosX(), 0, part.getPosY()));
            TileEntity te = level.getBlockEntity(centerPos);
            if (!(te instanceof TileEntityGomoku)) {
                return ActionResultType.FAIL;
            }
            TileEntityGomoku gomoku = (TileEntityGomoku) te;
            Vector3d location = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
            Direction facing = state.getValue(FACING);
            if (isClickChessBox(location.x, location.z, part, facing)) {
                level.playSound(null, centerPos, InitSounds.GOMOKU_RESET.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                gomoku.reset();
                gomoku.refresh();
                return ActionResultType.SUCCESS;
            }
            Entity sitEntity = serverLevel.getEntity(gomoku.getSitId());
            if (sitEntity == null || !sitEntity.isAlive() || sitEntity.getPassengers().isEmpty() || !(sitEntity.getPassengers().get(0) instanceof EntityMaid)) {
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.gomoku.no_maid"), Util.NIL_UUID);
                return ActionResultType.FAIL;
            }
            EntityMaid maid = (EntityMaid) sitEntity.getPassengers().get(0);
            // 检查是不是自己的女仆
            if (!maid.isOwnedBy(player)) {
                player.sendMessage(new TranslationTextComponent("message.touhou_little_maid.gomoku.not_owner"), Util.NIL_UUID);
                return ActionResultType.FAIL;
            }
            if (!gomoku.isPlayerTurn()) {
                return ActionResultType.FAIL;
            }
            int[][] chessData = gomoku.getChessData();
            int[] clickPos = getChessPos(location.x, location.z, part);
            if (clickPos == null) {
                return ActionResultType.FAIL;
            }
            Point playerPoint = new Point(clickPos[0], clickPos[1], Point.BLACK);
            if (gomoku.isInProgress() && chessData[playerPoint.x][playerPoint.y] == Point.EMPTY) {
                gomoku.setChessData(playerPoint.x, playerPoint.y, playerPoint.type);
                Statue statue = MaidGomokuAI.getStatue(chessData, playerPoint);
                // 但是和其他人女仆对弈不加好感哦
                if (statue == Statue.WIN) {
                    maid.getFavorabilityManager().apply(Type.GOMOKU_WIN);
                    int rankBefore = MaidGomokuAI.getRank(maid);
                    MaidGomokuAI.addMaidCount(maid);
                    int rankAfter = MaidGomokuAI.getRank(maid);
                    // 女仆升段啦
                    if (rankBefore < rankAfter) {
                        NetworkHandler.sendToClientPlayer(new SpawnParticleMessage(maid.getId(), SpawnParticleMessage.Type.RANK_UP), player);
                    }
                }
                gomoku.setInProgress(statue == Statue.IN_PROGRESS);
                level.playSound(null, pos, InitSounds.GOMOKU.get(), SoundCategory.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                if (gomoku.isInProgress()) {
                    gomoku.setPlayerTurn(false);
                    NetworkHandler.sendToClientPlayer(new ChessDataToClientMessage(centerPos, chessData, playerPoint, MaidGomokuAI.getMaidCount(maid)), player);
                }
                gomoku.refresh();
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void startMaidSit(EntityMaid maid, BlockState state, World worldIn, BlockPos pos) {
        if (worldIn instanceof ServerWorld && worldIn.getBlockEntity(pos) instanceof TileEntityJoy) {
            TileEntityJoy joy = (TileEntityJoy) worldIn.getBlockEntity(pos);
            ServerWorld serverLevel = (ServerWorld) worldIn;
            Entity oldSitEntity = serverLevel.getEntity(joy.getSitId());
            if (oldSitEntity != null && oldSitEntity.isAlive()) {
                return;
            }
            Direction face = state.getValue(FACING).getClockWise();
            Vector3d position = new Vector3d(0.5 + face.getStepX() * 1.5, 0.1, 0.5 + face.getStepZ() * 1.5);
            Vector3d sitPos = new Vector3d(pos.getX() + position.x, pos.getY() + position.y, pos.getZ() + position.z);
            EntitySit newSitEntity = new EntitySit(worldIn, sitPos, this.getTypeName());
            newSitEntity.yRot = face.getOpposite().toYRot() + this.sitYRot();
            worldIn.addFreshEntity(newSitEntity);
            joy.setSitId(newSitEntity.getUUID());
            joy.setChanged();
            maid.startRiding(newSitEntity);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (state.getValue(PART).isCenter()) {
            return new TileEntityGomoku();
        }
        return null;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(PART)) {
            case LEFT_UP:
                if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
                    return LEFT_UP_WITH_BOX;
                } else {
                    return LEFT_UP;
                }
            case UP:
                return UP;
            case RIGHT_UP:
                if (state.getValue(FACING).getAxis() == Direction.Axis.Z) {
                    return RIGHT_UP_WITH_BOX;
                } else {
                    return RIGHT_UP;
                }
            case LEFT_CENTER:
                return LEFT_CENTER;
            case RIGHT_CENTER:
                return RIGHT_CENTER;
            case LEFT_DOWN:
                if (state.getValue(FACING).getAxis() == Direction.Axis.Z) {
                    return LEFT_DOWN_WITH_BOX;
                } else {
                    return LEFT_DOWN;
                }
            case DOWN:
                return DOWN;
            case RIGHT_DOWN:
                if (state.getValue(FACING).getAxis() == Direction.Axis.X) {
                    return RIGHT_DOWN_WITH_BOX;
                } else {
                    return RIGHT_DOWN;
                }
            default:
                return CENTER;
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
}

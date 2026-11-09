package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidBedTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBed;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBed;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockMaidBed extends HorizontalDirectionalBlock implements EntityBlock {
    public static final List<DyeColor> AVAILABLE_COLOR = Util.make(Lists.newArrayList(), list -> {
        list.add(DyeColor.WHITE);
        list.add(DyeColor.BLACK);
        list.add(DyeColor.YELLOW);
        list.add(DyeColor.BLUE);
        list.add(DyeColor.GREEN);
        list.add(DyeColor.PURPLE);
        list.add(DyeColor.PINK);
    });

    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    protected static final VoxelShape BASE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

    public BlockMaidBed() {
        super(BlockBehaviour.Properties.of().sound(SoundType.WOOD).sound(SoundType.WOOD).strength(0.2F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BASE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        // 检查是否是染料物品
        if (!(itemStack.getItem() instanceof DyeItem dyeItem)) {
            InteractionResult sleep = letPlayerSleep(state, level, pos, player);
            if (sleep != null) {
                return sleep;
            }
            return InteractionResult.PASS;
        }
        DyeColor dyeColor = dyeItem.getDyeColor();
        // 检查染料颜色是否在可用颜色列表中
        if (!AVAILABLE_COLOR.contains(dyeColor)) {
            return InteractionResult.PASS;
        }
        // 获取床头位置和方块实体
        BlockPos headPos = headPos(state, pos);
        BlockEntity blockEntity = level.getBlockEntity(headPos);
        if (!(blockEntity instanceof TileEntityMaidBed bed)) {
            return InteractionResult.PASS;
        }
        if (bed.getColor() == dyeColor) {
            return InteractionResult.PASS;
        }
        bed.setColor(dyeColor);
        if (!player.isCreative()) {
            itemStack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 主人能不能睡这张床——开关默认关，关着的时候这里一律返回 null，右键女仆床跟原版一模一样。
     * <p>
     * 判定不在这里重写：白天、下界/末地、够不够得着、头上有没有遮挡、8 格内有没有怪，全在
     * ServerPlayer#startSleepInBed 里（它是 Player 那版的 override，javap 逐条验过），
     * 走它还能顺带把重生点和「几个人睡了」的投票名单设上——自己调 startSleeping 的话这两样都会漏掉，
     * 夜晚跳不过去，玩家也会睡醒在自己脚边而不是床边。
     * <p>
     * BedBlock#use 里那两条只有床自己知道的判定（缺半边、已占用）在这里补上，
     * 因为女仆床不是 BedBlock，那段逻辑不会替我们跑。
     * <p>
     * 传进去的是床头那一格：startSleepInBed 顺着 isBed 把 OCCUPIED 写在传入的这格上，
     * 而 MaidBedTask 只看床头，写头这一格玩家睡着时女仆才不会挤上来；起床时 stopSleeping 走同一个 isBed
     * 把它清掉，所以这个开关不能在她睡着的中途关（见 isBed）。
     */
    @Nullable
    private InteractionResult letPlayerSleep(BlockState state, Level level, BlockPos pos, Player player) {
        if (!WabConfig.playerCanSleepInMaidBed()) {
            return null;
        }
        if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer)) {
            return null;
        }
        // 手上拿着东西：那次右键先归物品用，不能把「睡」这件事抢过来
        if (!serverPlayer.getMainHandItem().isEmpty()) {
            return null;
        }
        if (serverPlayer.isSleeping()) {
            return InteractionResult.PASS;
        }
        BlockPos bedHead = headPos(state, pos);
        BlockState headState = level.getBlockState(bedHead);
        BlockPos footPos = bedHead.relative(headState.getValue(FACING).getOpposite());
        BlockState footState = level.getBlockState(footPos);
        if (!isHalf(headState, BedPart.HEAD) || !isHalf(footState, BedPart.FOOT)) {
            return InteractionResult.CONSUME;
        }
        if (headState.getValue(OCCUPIED) || footState.getValue(OCCUPIED)) {
            serverPlayer.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            return InteractionResult.SUCCESS;
        }
        serverPlayer.startSleepInBed(bedHead).ifLeft(problem -> {
            // OTHER_PROBLEM 在原版只是「她已经睡着了/已经没了」这类兜底，床上躺着人这事上面已经说清了
            if (problem != Player.BedSleepingProblem.OTHER_PROBLEM) {
                serverPlayer.displayClientMessage(problem.getMessage(), true);
            }
        });
        return InteractionResult.SUCCESS;
    }

    private boolean isHalf(BlockState state, BedPart part) {
        return state.is(this) && state.getValue(PART) == part;
    }

    /** 床头：渲染、方块实体和 MaidBedTask 认的都是头这一边。右键脚那一格也归到头。 */
    private static BlockPos headPos(BlockState state, BlockPos pos) {
        return state.getValue(PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(FACING));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == getNeighbourDirection(stateIn.getValue(PART), stateIn.getValue(FACING))) {
            return facingState.is(this) && facingState.getValue(PART) != stateIn.getValue(PART) ? stateIn.setValue(OCCUPIED, facingState.getValue(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide && player.isCreative()) {
            BedPart bedpart = state.getValue(PART);
            if (bedpart == BedPart.FOOT) {
                BlockPos blockpos = pos.relative(getNeighbourDirection(bedpart, state.getValue(FACING)));
                BlockState blockstate = worldIn.getBlockState(blockpos);
                if (blockstate.getBlock() == this && blockstate.getValue(PART) == BedPart.HEAD) {
                    worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
                    worldIn.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos relativePos = context.getClickedPos().relative(direction);
        return context.getLevel().getBlockState(relativePos).canBeReplaced(context) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isClientSide) {
            BlockPos headPos = pos.relative(state.getValue(FACING));
            worldIn.setBlock(headPos, state.setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
            worldIn.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(worldIn, pos, 3);

            if (worldIn.getBlockEntity(headPos) instanceof TileEntityMaidBed bed) {
                bed.setColor(ItemMaidBed.getColor(stack));
            }
        }
    }

    @Override
    public void fallOn(Level worldIn, BlockState blockState, BlockPos pos, Entity entityIn, float fallDistance) {
        super.fallOn(worldIn, blockState, pos, entityIn, fallDistance * 0.5f);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(worldIn, entity);
        } else {
            Vec3 movement = entity.getDeltaMovement();
            if (movement.y < 0) {
                double modulus = entity instanceof LivingEntity ? 1.0D : 0.8D;
                entity.setDeltaMovement(movement.x, -movement.y * (double) 0.66F * modulus, movement.z);
            }
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        BlockPos blockpos = pos.relative(state.getValue(FACING), state.getValue(PART) == BedPart.HEAD ? 0 : 1);
        return Mth.getSeed(blockpos.getX(), pos.getY(), blockpos.getZ());
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter world, BlockPos pos, @Nullable Entity entity) {
        if (entity instanceof EntityMaid) {
            return true;
        }
        // 玩家这边：开关开着才认这是她的床；但既然已经躺下了就一直认。
        // 起床那条路（LivingEntity#stopSleeping）是先问 isBed 才清 OCCUPIED、才把人搬回床边，
        // 睡着时把开关关掉会让她卡在床里，这张床还会永久挂着「已占用」，女仆再也睡不回来。
        if (entity instanceof Player player
                && (WabConfig.playerCanSleepInMaidBed() || sleepingHere(player, pos))) {
            return true;
        }
        return super.isBed(state, world, pos, entity);
    }

    private static boolean sleepingHere(Player player, BlockPos pos) {
        return player.isSleeping() && player.getSleepingPos().filter(pos::equals).isPresent();
    }

    private Direction getNeighbourDirection(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(PART) == BedPart.HEAD) {
            return new TileEntityMaidBed(pPos, pState);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> stacks = super.getDrops(state, params);
        BlockEntity parameter = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (parameter instanceof TileEntityMaidBed bed) {
            stacks.forEach(stack -> {
                if (stack.getItem() instanceof ItemMaidBed) {
                    ItemMaidBed.setColor(bed.getColor(), stack);
                }
            });
        }
        return stacks;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);

        // 获取床的颜色信息
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityMaidBed bed) {
            ItemMaidBed.setColor(bed.getColor(), stack);
        } else {
            // 如果当前是脚部分，尝试获取头部分的方块实体
            if (state.getValue(PART) == BedPart.FOOT) {
                BlockPos headPos = pos.relative(state.getValue(FACING));
                BlockEntity headEntity = level.getBlockEntity(headPos);
                if (headEntity instanceof TileEntityMaidBed bed) {
                    ItemMaidBed.setColor(bed.getColor(), stack);
                }
            }
        }

        return stack;
    }
}

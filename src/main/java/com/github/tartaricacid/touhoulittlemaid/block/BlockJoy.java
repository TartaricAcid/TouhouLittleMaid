package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class BlockJoy extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected BlockJoy(AbstractBlock.Properties properties) {
        super(properties);
    }

    public BlockJoy() {
        this(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 3.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected abstract Vector3d sitPosition();

    protected abstract String getTypeName();

    protected abstract int sitYRot();

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (worldIn instanceof ServerWorld && playerIn.getItemInHand(hand).isEmpty() && worldIn.getBlockEntity(pos) instanceof TileEntityJoy) {
            TileEntityJoy joy = (TileEntityJoy) worldIn.getBlockEntity(pos);
            ServerWorld serverLevel = (ServerWorld) worldIn;
            Entity oldSitEntity = serverLevel.getEntity(joy.getSitId());
            if (oldSitEntity != null && oldSitEntity.isAlive()) {
                return super.use(state, worldIn, pos, playerIn, hand, hit);
            }
            Vector3d sitPos = new Vector3d(pos.getX() + this.sitPosition().x, pos.getY() + this.sitPosition().y, pos.getZ() + this.sitPosition().z);
            EntitySit newSitEntity = new EntitySit(worldIn, sitPos, this.getTypeName());
            newSitEntity.yRot = state.getValue(FACING).getOpposite().toYRot() + this.sitYRot();
            worldIn.addFreshEntity(newSitEntity);
            joy.setSitId(newSitEntity.getUUID());
            joy.setChanged();
            playerIn.startRiding(newSitEntity);
            return ActionResultType.SUCCESS;
        }
        return super.use(state, worldIn, pos, playerIn, hand, hit);
    }

    public void startMaidSit(EntityMaid maid, BlockState state, World worldIn, BlockPos pos) {
        if (worldIn instanceof ServerWorld && worldIn.getBlockEntity(pos) instanceof TileEntityJoy) {
            TileEntityJoy joy = (TileEntityJoy) worldIn.getBlockEntity(pos);
            ServerWorld serverLevel = (ServerWorld) worldIn;
            Entity oldSitEntity = serverLevel.getEntity(joy.getSitId());
            if (oldSitEntity != null && oldSitEntity.isAlive()) {
                return;
            }
            Vector3d sitPos = new Vector3d(pos.getX() + this.sitPosition().x, pos.getY() + this.sitPosition().y, pos.getZ() + this.sitPosition().z);
            EntitySit newSitEntity = new EntitySit(worldIn, sitPos, this.getTypeName());
            newSitEntity.yRot = state.getValue(FACING).getOpposite().toYRot() + this.sitYRot();
            worldIn.addFreshEntity(newSitEntity);
            joy.setSitId(newSitEntity.getUUID());
            joy.setChanged();
            maid.startRiding(newSitEntity);
        }
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityJoy && worldIn instanceof ServerWorld) {
            TileEntityJoy joy = (TileEntityJoy) blockEntity;
            ServerWorld serverLevel = (ServerWorld) worldIn;
            Entity entity = serverLevel.getEntity(joy.getSitId());
            if (entity instanceof EntitySit) {
                entity.remove();
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return true;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}

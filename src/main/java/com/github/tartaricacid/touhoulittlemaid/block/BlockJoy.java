package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;

import java.util.function.Consumer;

public abstract class BlockJoy extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected BlockJoy(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockJoy() {
        this(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected abstract Vec3 sitPosition();

    protected abstract String getTypeName();

    protected abstract int sitYRot();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
        if (worldIn instanceof ServerLevel serverLevel && playerIn.getItemInHand(hand).isEmpty() && worldIn.getBlockEntity(pos) instanceof TileEntityJoy joy) {
            Entity oldSitEntity = serverLevel.getEntity(joy.getSitId());
            if (oldSitEntity != null && oldSitEntity.isAlive()) {
                return super.use(state, worldIn, pos, playerIn, hand, hit);
            }
            EntitySit newSitEntity = new EntitySit(worldIn, Vec3.atLowerCornerWithOffset(pos, this.sitPosition().x, this.sitPosition().y, this.sitPosition().z), this.getTypeName());
            newSitEntity.setYRot(state.getValue(FACING).getOpposite().toYRot() + this.sitYRot());
            worldIn.addFreshEntity(newSitEntity);
            joy.setSitId(newSitEntity.getUUID());
            joy.setChanged();
            playerIn.startRiding(newSitEntity);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, playerIn, hand, hit);
    }

    public void startMaidSit(EntityMaid maid, BlockState state, Level worldIn, BlockPos pos) {
        if (worldIn instanceof ServerLevel serverLevel && worldIn.getBlockEntity(pos) instanceof TileEntityJoy joy) {
            Entity oldSitEntity = serverLevel.getEntity(joy.getSitId());
            if (oldSitEntity != null && oldSitEntity.isAlive()) {
                return;
            }
            EntitySit newSitEntity = new EntitySit(worldIn, Vec3.atLowerCornerWithOffset(pos, this.sitPosition().x, this.sitPosition().y, this.sitPosition().z), this.getTypeName());
            newSitEntity.setYRot(state.getValue(FACING).getOpposite().toYRot() + this.sitYRot());
            worldIn.addFreshEntity(newSitEntity);
            joy.setSitId(newSitEntity.getUUID());
            joy.setChanged();
            maid.startRiding(newSitEntity);
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityJoy joy && worldIn instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(joy.getSitId());
            if (entity instanceof EntitySit) {
                entity.discard();
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

//    @Override
//    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
//        consumer.accept(new IClientBlockExtensions() {
//            @Override
//            public boolean addHitEffects(BlockState state, Level world, HitResult target, ParticleEngine manager) {
//                if (target instanceof BlockHitResult && world instanceof ClientLevel) {
//                    BlockHitResult blockTarget = (BlockHitResult) target;
//                    BlockPos pos = blockTarget.getBlockPos();
//                    ClientLevel clientWorld = (ClientLevel) world;
//                    this.crack(clientWorld, pos, Blocks.OAK_PLANKS.defaultBlockState(), blockTarget.getDirection());
//                }
//                return true;
//            }
//
//            @Override
//            public boolean addDestroyEffects(BlockState state, Level world, BlockPos pos, ParticleEngine manager) {
//                Minecraft.getInstance().particleEngine.destroy(pos, Blocks.OAK_PLANKS.defaultBlockState());
//                return true;
//            }
//
//            @OnlyIn(Dist.CLIENT)
//            private void crack(ClientLevel world, BlockPos pos, BlockState state, Direction side) {
//                if (state.getRenderShape() != RenderShape.INVISIBLE) {
//                    int posX = pos.getX();
//                    int posY = pos.getY();
//                    int posZ = pos.getZ();
//                    AABB aabb = state.getShape(world, pos).bounds();
//                    double x = posX + world.random.nextDouble() * (aabb.maxX - aabb.minX - 0.2) + 0.1 + aabb.minX;
//                    double y = posY + world.random.nextDouble() * (aabb.maxY - aabb.minY - 0.2) + 0.1 + aabb.minY;
//                    double z = posZ + world.random.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2) + 0.1 + aabb.minZ;
//                    if (side == Direction.DOWN) {
//                        y = posY + aabb.minY - 0.1;
//                    }
//                    if (side == Direction.UP) {
//                        y = posY + aabb.maxY + 0.1;
//                    }
//                    if (side == Direction.NORTH) {
//                        z = posZ + aabb.minZ - 0.1;
//                    }
//                    if (side == Direction.SOUTH) {
//                        z = posZ + aabb.maxZ + 0.1;
//                    }
//                    if (side == Direction.WEST) {
//                        x = posX + aabb.minX - 0.1;
//                    }
//                    if (side == Direction.EAST) {
//                        x = posX + aabb.maxX + 0.1;
//                    }
//                    TerrainParticle diggingParticle = new TerrainParticle(world, x, y, z, 0, 0, 0, state);
//                    Minecraft.getInstance().particleEngine.add(diggingParticle.updateSprite(state, pos).setPower(0.2f).scale(0.6f));
//                }
//            }
//        });
//    }
}

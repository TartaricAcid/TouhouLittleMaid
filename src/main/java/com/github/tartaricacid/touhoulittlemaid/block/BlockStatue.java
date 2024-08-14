package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BlockStatue extends Block implements EntityBlock {
    public static final BooleanProperty IS_TINY = BooleanProperty.create("is_tiny");
    public static final IClientBlockExtensions iClientBlockExtensions = FMLEnvironment.dist == Dist.CLIENT? new IClientBlockExtensions() {
        @Override
        public boolean addHitEffects(BlockState state, Level world, HitResult target, ParticleEngine manager) {
            if (target instanceof BlockHitResult blockTarget && world instanceof ClientLevel clientWorld) {
                BlockPos pos = blockTarget.getBlockPos();
                this.crack(clientWorld, pos, Blocks.CLAY.defaultBlockState(), blockTarget.getDirection());
            }
            return true;
        }

        @Override
        public boolean addDestroyEffects(BlockState state, Level world, BlockPos pos, ParticleEngine manager) {
            Minecraft.getInstance().particleEngine.destroy(pos, Blocks.CLAY.defaultBlockState());
            return true;
        }

        @OnlyIn(Dist.CLIENT)
        private void crack(ClientLevel world, BlockPos pos, BlockState state, Direction side) {
            if (state.getRenderShape() != RenderShape.INVISIBLE) {
                int posX = pos.getX();
                int posY = pos.getY();
                int posZ = pos.getZ();
                AABB aabb = state.getShape(world, pos).bounds();
                double x = posX + world.random.nextDouble() * (aabb.maxX - aabb.minX - 0.2) + 0.1 + aabb.minX;
                double y = posY + world.random.nextDouble() * (aabb.maxY - aabb.minY - 0.2) + 0.1 + aabb.minY;
                double z = posZ + world.random.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2) + 0.1 + aabb.minZ;
                if (side == Direction.DOWN) {
                    y = posY + aabb.minY - 0.1;
                }
                if (side == Direction.UP) {
                    y = posY + aabb.maxY + 0.1;
                }
                if (side == Direction.NORTH) {
                    z = posZ + aabb.minZ - 0.1;
                }
                if (side == Direction.SOUTH) {
                    z = posZ + aabb.maxZ + 0.1;
                }
                if (side == Direction.WEST) {
                    x = posX + aabb.minX - 0.1;
                }
                if (side == Direction.EAST) {
                    x = posX + aabb.maxX + 0.1;
                }
                TerrainParticle diggingParticle = new TerrainParticle(world, x, y, z, 0, 0, 0, state);
                Minecraft.getInstance().particleEngine.add(diggingParticle.updateSprite(state, pos).setPower(0.2f).scale(0.6f));
            }
        }
    }: null;

    public BlockStatue() {
        super(BlockBehaviour.Properties.of().sound(SoundType.MUD).strength(1, 2).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_TINY, false));
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide) {
            this.getStatue(worldIn, pos).ifPresent(statue -> {
                this.restoreClayBlock(worldIn, pos, statue);
                if (!player.isCreative()) {
                    Block.popResource(worldIn, pos, new ItemStack(Blocks.CLAY));
                }
            });
        }
        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide) {
            this.getStatue(world, pos).ifPresent(statue -> this.restoreClayBlock(world, pos, statue));
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_TINY);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStatue(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    private Optional<TileEntityStatue> getStatue(BlockGetter world, BlockPos pos) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityStatue) {
            return Optional.of((TileEntityStatue) te);
        }
        return Optional.empty();
    }

    private void restoreClayBlock(@Nonnull Level worldIn, @Nonnull BlockPos pos, TileEntityStatue statue) {
        List<BlockPos> posList = statue.getAllBlocks();
        for (BlockPos storagePos : posList) {
            if (!storagePos.equals(pos)) {
                getStatue(worldIn, storagePos).ifPresent(s -> worldIn.setBlock(storagePos, Blocks.CLAY.defaultBlockState(), Block.UPDATE_ALL));
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(IS_TINY);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (worldIn.getBlockState(pos.below()).is(Blocks.FIRE)) {
            getStatue(worldIn, pos).ifPresent(statue -> {
                worldIn.setBlockAndUpdate(pos, InitBlocks.GARAGE_KIT.get().defaultBlockState());
                {
                    worldIn.levelEvent(LevelEvent.SOUND_EXTINGUISH_FIRE, pos, 0);
                }
                BlockEntity te = worldIn.getBlockEntity(pos);
                if (te instanceof TileEntityGarageKit && statue.getExtraMaidData() != null) {
                    ((TileEntityGarageKit) te).setData(statue.getFacing(), statue.getExtraMaidData());
                }
            });
        }
    }
}

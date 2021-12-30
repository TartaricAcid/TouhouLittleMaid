package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.minecraftforge.common.util.Constants.BlockFlags.DEFAULT;

public class BlockStatue extends Block {
    public BlockStatue() {
        super(AbstractBlock.Properties.of(Material.CLAY).strength(1, 2).noOcclusion());
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isClientSide) {
            this.getStatue(worldIn, pos).ifPresent(statue -> {
                this.restoreClayBlock(worldIn, pos, statue);
                if (!player.isCreative()) {
                    Block.popResource(worldIn, pos, new ItemStack(Blocks.CLAY));
                }
            });
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide) {
            this.getStatue(world, pos).ifPresent(statue -> this.restoreClayBlock(world, pos, statue));
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityStatue();
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Blocks.CLAY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        Minecraft.getInstance().particleEngine.destroy(pos, Blocks.CLAY.defaultBlockState());
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        if (target instanceof BlockRayTraceResult && world instanceof ClientWorld) {
            BlockRayTraceResult blockTarget = (BlockRayTraceResult) target;
            BlockPos pos = blockTarget.getBlockPos();
            ClientWorld clientWorld = (ClientWorld) world;
            this.crack(clientWorld, pos, Blocks.CLAY.defaultBlockState(), blockTarget.getDirection());
        }
        return true;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    private Optional<TileEntityStatue> getStatue(IBlockReader world, BlockPos pos) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof TileEntityStatue) {
            return Optional.of((TileEntityStatue) te);
        }
        return Optional.empty();
    }

    private void restoreClayBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, TileEntityStatue statue) {
        List<BlockPos> posList = statue.getAllBlocks();
        for (BlockPos storagePos : posList) {
            if (!storagePos.equals(pos)) {
                getStatue(worldIn, storagePos).ifPresent(s -> worldIn.setBlock(storagePos, Blocks.CLAY.defaultBlockState(), DEFAULT));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void crack(ClientWorld world, BlockPos pos, BlockState state, Direction side) {
        if (state.getRenderShape() != BlockRenderType.INVISIBLE) {
            int posX = pos.getX();
            int posY = pos.getY();
            int posZ = pos.getZ();
            AxisAlignedBB aabb = state.getShape(world, pos).bounds();
            double x = posX + this.RANDOM.nextDouble() * (aabb.maxX - aabb.minX - 0.2) + 0.1 + aabb.minX;
            double y = posY + this.RANDOM.nextDouble() * (aabb.maxY - aabb.minY - 0.2) + 0.1 + aabb.minY;
            double z = posZ + this.RANDOM.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2) + 0.1 + aabb.minZ;
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
            DiggingParticle diggingParticle = new DiggingParticle(world, x, y, z, 0, 0, 0, state);
            Minecraft.getInstance().particleEngine.add(diggingParticle.init(pos).setPower(0.2f).scale(0.6f));
        }
    }
}

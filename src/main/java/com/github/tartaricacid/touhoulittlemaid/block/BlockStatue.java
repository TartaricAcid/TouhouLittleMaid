package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockStatue extends Block {
    public BlockStatue() {
        super(Material.ROCK);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "statue");
        setHardness(1.0f);
        setResistance(2000.0F);
        setRegistryName("statue");
    }

    @Override
    public boolean hasTileEntity(@Nonnull IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityStatue();
    }

    @Override
    public void onBlockHarvested(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityStatue) {
                restoreClayBlock(worldIn, pos, (TileEntityStatue) te);
                if (!player.isCreative()) {
                    Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.CLAY));
                }
            }
        }
    }

    private void restoreClayBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, TileEntityStatue statue) {
        List<BlockPos> posList = statue.getAllBlocks();
        for (BlockPos storagePos : posList) {
            if (!storagePos.equals(pos)) {
                TileEntity tileEntity = worldIn.getTileEntity(storagePos);
                if (tileEntity instanceof TileEntityStatue) {
                    worldIn.setBlockState(storagePos, Blocks.CLAY.getDefaultState());
                }
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, @Nullable RayTraceResult target, @Nonnull World world,
                                  @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return new ItemStack(Blocks.CLAY);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, @Nonnull BlockPos pos, @Nonnull ParticleManager manager) {
        addBlockDestroyEffects(world, pos, Blocks.CLAY.getDefaultState(), manager);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(@Nonnull IBlockState state, World world, RayTraceResult target, @Nonnull ParticleManager manager) {
        addBlockHitEffects(world, target.getBlockPos(), target.sideHit, Blocks.CLAY.getDefaultState(), manager);
        return true;
    }

    /**
     * 来自原版修改过的方法
     */
    @SideOnly(Side.CLIENT)
    private void addBlockDestroyEffects(World world, BlockPos pos, IBlockState state, ParticleManager manager) {
        if (!state.getBlock().isAir(state, world, pos) && !state.getBlock().addDestroyEffects(world, pos, manager)) {
            for (int j = 0; j < 4; ++j) {
                for (int k = 0; k < 4; ++k) {
                    for (int l = 0; l < 4; ++l) {
                        double d0 = (j + 0.5D) / 4.0D;
                        double d1 = (k + 0.5D) / 4.0D;
                        double d2 = (l + 0.5D) / 4.0D;
                        manager.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(),
                                pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2,
                                d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, Block.getStateId(state));
                    }
                }
            }
        }
    }

    /**
     * 来自原版修改过的方法
     */
    @SideOnly(Side.CLIENT)
    private void addBlockHitEffects(World world, BlockPos pos, EnumFacing side, IBlockState iblockstate, ParticleManager manager) {
        Random rand = Block.RANDOM;
        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(world, pos);
            double d0 = i + rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D)
                    + 0.10000000149011612D + axisalignedbb.minX;
            double d1 = j + rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D)
                    + 0.10000000149011612D + axisalignedbb.minY;
            double d2 = k + rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D)
                    + 0.10000000149011612D + axisalignedbb.minZ;
            if (side == EnumFacing.DOWN) {
                d1 = j + axisalignedbb.minY - 0.10000000149011612D;
            }
            if (side == EnumFacing.UP) {
                d1 = j + axisalignedbb.maxY + 0.10000000149011612D;
            }
            if (side == EnumFacing.NORTH) {
                d2 = k + axisalignedbb.minZ - 0.10000000149011612D;
            }
            if (side == EnumFacing.SOUTH) {
                d2 = k + axisalignedbb.maxZ + 0.10000000149011612D;
            }
            if (side == EnumFacing.WEST) {
                d0 = i + axisalignedbb.minX - 0.10000000149011612D;
            }
            if (side == EnumFacing.EAST) {
                d0 = i + axisalignedbb.maxX + 0.10000000149011612D;
            }
            manager.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(),
                    d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(iblockstate));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(@Nonnull IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(@Nonnull IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isBlockNormalCube(@Nonnull IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }
}

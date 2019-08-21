package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * 在 {@link com.github.tartaricacid.touhoulittlemaid.internal.task.VanillaFarmHandler#isSeed} 方法中充当 IBlockAccess 占位符的类
 * 可以认为是空气方块
 *
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public class EmptyBlockReader implements IBlockAccess {
    public static final EmptyBlockReader INSTANCE = new EmptyBlockReader();

    @Override
    public TileEntity getTileEntity(@Nonnull BlockPos pos) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
        return 0;
    }

    @Nonnull
    @Override
    public IBlockState getBlockState(@Nonnull BlockPos pos) {
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isAirBlock(@Nonnull BlockPos pos) {
        return true;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public Biome getBiome(@Nonnull BlockPos pos) {
        return Biomes.PLAINS;
    }

    @Override
    public int getStrongPower(@Nonnull BlockPos pos, @Nonnull EnumFacing direction) {
        return 0;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public WorldType getWorldType() {
        return WorldType.DEFAULT;
    }

    @Override
    public boolean isSideSolid(@Nonnull BlockPos pos, @Nonnull EnumFacing side, boolean defaultValue) {
        return false;
    }
}

package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

/**
 * @author TartaricAcid
 * @date 2019/10/8 14:54
 **/
public class BlockMaidBeacon extends Block implements ITileEntityProvider {
    public static final PropertyEnum<Position> POSITION = PropertyEnum.create("position", Position.class);
    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 1, 0.875);
    private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.40625, 0, 0.40625, 0.59375, 1, 0.59375);

    public BlockMaidBeacon() {
        super(Material.WOOD);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "maid_beacon");
        setHardness(1.0f);
        setRegistryName("maid_beacon");
        setCreativeTab(MaidItems.TABS);
        setDefaultState(blockState.getBaseState().withProperty(POSITION, Position.DOWN));
        setLightLevel(1.0f);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POSITION, Position.byPositionIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POSITION).getPositionIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POSITION);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        if (Position.byPositionIndex(meta) != Position.DOWN) {
            return new TileEntityMaidBeacon();
        } else {
            return null;
        }
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(MaidItems.MAID_BEACON);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        if (pos.getY() >= worldIn.getHeight() - 1) {
            return false;
        } else {
            return super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos down = pos.down();
        BlockPos up = pos.up();

        boolean isBeaconUp = state.getValue(POSITION) == Position.UP_N_S || state.getValue(POSITION) == Position.UP_W_E;
        if (player.isCreative() && isBeaconUp && worldIn.getBlockState(down).getBlock() == this) {
            worldIn.setBlockToAir(down);
        }

        if (state.getValue(POSITION) == Position.DOWN && worldIn.getBlockState(up).getBlock() == this) {
            if (player.isCreative()) {
                worldIn.setBlockToAir(pos);
            }
            worldIn.setBlockToAir(up);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (state.getValue(POSITION) == Position.DOWN) {
            BlockPos blockPosUp = pos.up();
            IBlockState blockStateUp = worldIn.getBlockState(blockPosUp);
            if (blockStateUp.getBlock() != this) {
                worldIn.setBlockToAir(pos);
                if (!worldIn.isRemote) {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
        } else {
            BlockPos blockPosDown = pos.down();
            IBlockState blockStateDown = worldIn.getBlockState(blockPosDown);
            if (blockStateDown.getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(POSITION) == Position.DOWN) {
            return DOWN_AABB;
        }
        return UP_AABB;
    }

    @Nonnull
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(POSITION) == Position.DOWN ? MaidItems.MAID_BEACON : Items.AIR;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public enum Position implements IStringSerializable {
        // 上下位置
        UP_N_S, UP_W_E, DOWN;

        private static Position byPositionIndex(int meta) {
            if (meta == 0) {
                return UP_N_S;
            } else if (meta == 1) {
                return UP_W_E;
            } else {
                return DOWN;
            }
        }

        @Override
        public String getName() {
            return this.name().toLowerCase(Locale.US);
        }

        private int getPositionIndex() {
            return this.ordinal();
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * @author TartaricAcid
 * @date 2019/10/8 14:54
 **/
public class BlockMaidBeacon extends Block {
    public static final PropertyEnum<Position> POSITION = PropertyEnum.create("position", Position.class);
    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.1875, 0.0625, 0.1875, 0.8125, 1, 0.8125);
    private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.40625, 0, 0.40625, 0.59375, 1.0625, 0.59375);

    public BlockMaidBeacon() {
        super(Material.WOOD);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "maid_beacon");
        setHardness(1.0f);
        setRegistryName("maid_beacon");
        setCreativeTab(MaidItems.TABS);
        setDefaultState(blockState.getBaseState().withProperty(POSITION, Position.DOWN));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityMaidBeacon) {
            playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.MAID_BEACON.getId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
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

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(BlockMaidBeacon.POSITION) != BlockMaidBeacon.Position.DOWN;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMaidBeacon();
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

        boolean isBeaconUp = state.getValue(POSITION) != Position.DOWN;
        if (player.isCreative() && isBeaconUp && worldIn.getBlockState(down).getBlock() == this) {
            if (player.isCreative()) {
                worldIn.setBlockToAir(pos);
            }
            worldIn.setBlockToAir(down);
        }

        if (!isBeaconUp && worldIn.getBlockState(up).getBlock() == this) {
            worldIn.setBlockToAir(up);
        }
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMaidBeacon) {
            Block.spawnAsEntity(worldIn, pos, ItemMaidBeacon.tileEntityToItemStack((TileEntityMaidBeacon) te));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(POSITION) == Position.DOWN) {
            return 0;
        } else {
            return 15;
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (state.getValue(POSITION) == Position.DOWN) {
            BlockPos blockPosUp = pos.up();
            IBlockState blockStateUp = worldIn.getBlockState(blockPosUp);
            if (blockStateUp.getBlock() != this) {
                worldIn.setBlockToAir(pos);
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

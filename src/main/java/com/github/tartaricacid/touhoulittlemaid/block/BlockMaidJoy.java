package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidJoy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidJoy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockMaidJoy extends BlockHorizontal implements ITileEntityProvider {
    public static final PropertyBool CORE = PropertyBool.create("core");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_CORE = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);

    public BlockMaidJoy() {
        super(Material.WOOD);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "maid_joy");
        setHardness(1.0f);
        setRegistryName("maid_joy");
        setCreativeTab(MaidItems.MAIN_TABS);
        setResistance(2000.0F);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CORE, Boolean.FALSE));
        this.hasTileEntity = true;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing)
                .withProperty(CORE, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(CORE)) {
            i |= 4;
        }
        return i;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, CORE);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMaidJoy();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityMaidJoy) {
                String type = ((TileEntityMaidJoy) te).getType();
                ItemStack stack = ItemMaidJoy.setType(new ItemStack(MaidItems.MAID_JOY), type);
                setToAir(worldIn, pos, (TileEntityMaidJoy) te);
                if (!player.isCreative()) {
                    Block.spawnAsEntity(worldIn, pos, stack);
                }
            }
        }
    }

    private void setToAir(@Nonnull World worldIn, @Nonnull BlockPos pos, TileEntityMaidJoy statue) {
        List<BlockPos> posList = statue.getAllBlocks();
        for (BlockPos storagePos : posList) {
            if (!storagePos.equals(pos)) {
                TileEntity tileEntity = worldIn.getTileEntity(storagePos);
                if (tileEntity instanceof TileEntityMaidJoy) {
                    worldIn.setBlockToAir(storagePos);
                }
            }
        }
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(CORE)) {
            return AABB_CORE;
        }
        return AABB;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = new ItemStack(MaidItems.MAID_JOY);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityMaidJoy) {
            TileEntityMaidJoy maidJoy = (TileEntityMaidJoy) te;
            return ItemMaidJoy.setType(stack, maidJoy.getType());
        }
        return stack;
    }
}

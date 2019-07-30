package com.github.tartaricacid.touhoulittlemaid.block;

import java.util.Locale;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import com.github.tartaricacid.touhoulittlemaid.util.MatrixUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockGrid extends Block {

    public static final PropertyEnum<Direction> DIRECTION = PropertyEnum.create("facing", Direction.class);
    public static final PropertyBool INPUT = PropertyBool.create("input");
    public static final PropertyBool BLACKLIST = PropertyBool.create("blacklist");
    protected static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.0625D, 0.9375D, 0.0625D, 0.9375D, 1, 0.9375D);
    protected static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.0625D, 0, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0625D, 0.0625D, 0, 0.9375D, 0.9375D, 0.0625D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.9375D, 0.0625D, 0.0625D, 1.0D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0, 0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D);
    public static final AxisAlignedBB[] AABBS = new AxisAlignedBB[] { AABB_DOWN, AABB_UP, AABB_NORTH, AABB_SOUTH, AABB_WEST, AABB_EAST };

    public BlockGrid() {
        super(Material.IRON);
        setDefaultState(blockState.getBaseState().withProperty(DIRECTION, Direction.UP_NORTH).withProperty(INPUT, true).withProperty(BLACKLIST, false));
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityGrid();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityGrid) {
            TileEntityGrid grid = (TileEntityGrid) tile;
            state = state.withProperty(INPUT, grid.input);
            state = state.withProperty(BLACKLIST, grid.blacklist);
        }
        return state;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof TileEntityGrid)) {
            return false;
        }
        TileEntityGrid grid = (TileEntityGrid) tile;
        if (playerIn.isSneaking()) {
            for (int i = 0; i < grid.handler.getSlots(); i++) {
                grid.handler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        else {
            ItemStack stack = playerIn.getHeldItem(hand);
            Matrix4f matrix = state.getValue(DIRECTION).matrix();
            Point3f point = new Point3f(hitX, hitY, hitZ);
            matrix.transform(point);
            int ix = MathHelper.clamp((int) (point.x * 3), 0, 2);
            int iz = MathHelper.clamp((int) (point.z * 3), 0, 2);
            int i = ix + iz * 3;
            ItemStack copy = stack.isEmpty() ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(stack, 1);
            grid.handler.setStackInSlot(i, copy);
        }
        grid.refresh();
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityGrid) {
            TileEntityGrid grid = (TileEntityGrid) tile;
            grid.updateMode(state);
        }
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABBS[state.getValue(DIRECTION).face.ordinal()];
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DIRECTION, Direction.byFacing(facing, placer.getHorizontalFacing()));
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
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
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION, INPUT, BLACKLIST);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DIRECTION).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DIRECTION, Direction.VALUES[meta % 12]);
    }

    public static enum Direction implements IStringSerializable {

        DOWN_NORTH(EnumFacing.DOWN, EnumFacing.NORTH, 180, 0),
        DOWN_SOUTH(EnumFacing.DOWN, EnumFacing.SOUTH, 180, 180),
        DOWN_WEST(EnumFacing.DOWN, EnumFacing.WEST, 180, 90),
        DOWN_EAST(EnumFacing.DOWN, EnumFacing.EAST, 180, 270),
        UP_NORTH(EnumFacing.UP, EnumFacing.NORTH, 0, 0),
        UP_SOUTH(EnumFacing.UP, EnumFacing.SOUTH, 0, 180),
        UP_WEST(EnumFacing.UP, EnumFacing.WEST, 0, 90),
        UP_EAST(EnumFacing.UP, EnumFacing.EAST, 0, 270),
        NORTH(EnumFacing.NORTH, null, 90, 180),
        SOUTH(EnumFacing.SOUTH, null, 90, 0),
        WEST(EnumFacing.WEST, null, 90, 270),
        EAST(EnumFacing.EAST, null, 90, 90);

        public final EnumFacing face;
        public final EnumFacing rot;
        public float rotX;
        public float rotY;
        private Matrix4f matrix;

        public static final Direction[] VALUES = values();

        private Direction(EnumFacing face, EnumFacing rot, int rotX, int rotY) {
            this.face = face;
            this.rot = rot;
            this.rotX = rotX;
            this.rotY = rotY;
        }

        public static Direction byFacing(EnumFacing face, EnumFacing rot) {
            for (Direction direction : VALUES) {
                if (face == direction.face) {
                    if (face.getHorizontalIndex() >= 0) {
                        return direction;
                    }
                    else if (rot == direction.rot) {
                        return direction;
                    }
                }
            }
            return UP_NORTH;
        }

        public Matrix4f matrix() {
            if (matrix == null) {
                matrix = new Matrix4f();
                matrix.setIdentity();
                matrix.m03 = matrix.m13 = matrix.m23 = -.5f;
                Matrix4f matrix4f = new Matrix4f();
                matrix4f.setIdentity();
                MatrixUtil.rotate(rotX * 0.017453292F, new Vector3f(1, 0, 0), matrix4f, matrix4f);
                Matrix4f matrix4f1 = new Matrix4f();
                matrix4f1.setIdentity();
                MatrixUtil.rotate(rotY * 0.017453292F, new Vector3f(0, 1, 0), matrix4f1, matrix4f1);
                matrix4f.mul(matrix4f1);
                matrix4f.mul(matrix);
                matrix.invert();
                matrix.mul(matrix4f);
            }
            return matrix;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.US);
        }

    }
}

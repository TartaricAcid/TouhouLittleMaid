package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import com.github.tartaricacid.touhoulittlemaid.util.MatrixUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Locale;

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
    public static final AxisAlignedBB[] AABBS = new AxisAlignedBB[]{AABB_DOWN, AABB_UP, AABB_NORTH, AABB_SOUTH, AABB_WEST, AABB_EAST};

    public BlockGrid() {
        super(Material.CLAY);
        setSoundType(SoundType.METAL);
        setDefaultState(blockState.getBaseState().withProperty(DIRECTION, Direction.UP_NORTH).withProperty(INPUT, true).withProperty(BLACKLIST, false));
        setCreativeTab(MaidItems.TABS);
        setHardness(0.25f);
    }

    @SideOnly(Side.CLIENT)
    private static String getModeTooltip(boolean input, boolean blacklist) {
        String s = I18n.format(String.format("tooltips.%s.grid.input.%s", TouhouLittleMaid.MOD_ID, input));
        s += " | ";
        s += I18n.format(String.format("tooltips.%s.grid.blacklist.%s", TouhouLittleMaid.MOD_ID, blacklist));
        return s;
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
        // 先检查 TileEntityGrid
        if (!(tile instanceof TileEntityGrid)) {
            return false;
        }
        TileEntityGrid grid = (TileEntityGrid) tile;

        // 玩家潜行状态右击，清除面板
        if (playerIn.isSneaking()) {
            for (int i = 0; i < grid.handler.getSlots(); i++) {
                grid.handler.setStackInSlot(i, ItemStack.EMPTY);
            }
            grid.clearCraftingResult();
            worldIn.playSound(playerIn, hitX, hitY, hitZ, SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.PLAYERS, 2, 1);
        }

        // 否则，执行物品标记
        else {
            ItemStack stack = playerIn.getHeldItem(hand);
            // 应用矩阵转换坐标
            Matrix4f matrix = state.getValue(DIRECTION).matrix();
            Point3f point = new Point3f(hitX, hitY, hitZ);
            matrix.transform(point);

            // 范围判定
            boolean isInMarkItemRange = point.x > 0.1875 && point.x < 0.8125 && point.z > 0.1875 && point.z < 0.8125;
            boolean isInSwitchListRange = (point.x > 0.3125 && point.x < 0.6875) || (point.z > 0.3125 && point.z < 0.6875);

            // 标记物品
            if (isInMarkItemRange) {
                int ix = MathHelper.clamp((int) ((point.x - 0.1875) / 0.625 * 3), 0, 2);
                int iz = MathHelper.clamp((int) ((point.z - 0.1875) / 0.625 * 3), 0, 2);
                int i = ix + iz * 3;
                ItemStack before = grid.handler.getStackInSlot(i);
                ItemStack copy = stack.isEmpty() ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(stack, 1);
                if (before.isEmpty() && copy.isEmpty()) {
                    return false;
                }
                grid.handler.setStackInSlot(i, copy);
                grid.clearCraftingResult();
                SoundEvent soundEvent = stack.isEmpty() ? SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM : SoundEvents.ENTITY_ITEM_PICKUP;
                worldIn.playSound(playerIn, hitX, hitY, hitZ, soundEvent, SoundCategory.PLAYERS, 1, 1);
            }
            // 切换白名单黑名单
            else if (isInSwitchListRange) {
                grid.blacklist = !grid.blacklist;
                worldIn.playSound(playerIn, hitX, hitY, hitZ, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.PLAYERS, 1, 1);
                if (worldIn.isRemote) {
                    playerIn.sendStatusMessage(new TextComponentTranslation(String.format("message.%s.grid.blacklist.%s", TouhouLittleMaid.MOD_ID, grid.blacklist)), true);
                }
            }
            // 其他位置，切换输入输出
            else {
                grid.input = !grid.input;
                worldIn.playSound(playerIn, hitX, hitY, hitZ, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1, 1);
                if (worldIn.isRemote) {
                    playerIn.sendStatusMessage(new TextComponentTranslation(String.format("message.%s.grid.input.%s", TouhouLittleMaid.MOD_ID, grid.input)), true);
                }
            }
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
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        // NO-OP
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack itemstack = new ItemStack(this);
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityGrid) {
            TileEntityGrid grid = (TileEntityGrid) tile;
            if (!grid.isCleared()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound.setTag("BlockEntityTag", grid.write(nbttagcompound1));
                itemstack.setTagCompound(nbttagcompound);
            }
        }

        spawnAsEntity(worldIn, pos, itemstack);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound == null || !nbttagcompound.hasKey("BlockEntityTag", 10)) {
            tooltip.add(getModeTooltip(true, false));
            return;
        }

        NBTTagCompound tag = nbttagcompound.getCompoundTag("BlockEntityTag");

        tooltip.add(getModeTooltip(tag.getBoolean("Input"), tag.getBoolean("Blacklist")));

        if (tag.hasKey("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tag, nonnulllist);
            int i = 0;
            int j = 0;

            for (ItemStack itemstack : nonnulllist) {
                if (!itemstack.isEmpty()) {
                    ++j;

                    if (i <= 4) {
                        ++i;
                        tooltip.add(itemstack.getDisplayName());
                    }
                }
            }

            if (j - i > 0) {
                tooltip.add(String.format(TextFormatting.ITALIC + I18n.format("container.shulkerBox.more"), j - i));
            }
        }
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

    public enum Direction implements IStringSerializable {
        // 所有的放置方向
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

        public static final Direction[] VALUES = values();
        public final EnumFacing face;
        public final EnumFacing rot;
        public float rotX;
        public float rotY;
        private Matrix4f matrix;

        /**
         * 物品放置的方向枚举
         *
         * @param face 方块所处的方向（X 方向）
         * @param rot  方块所处的方向（Y 方向）
         * @param rotX X 方向旋转角度
         * @param rotY Y 方向旋转角度
         */
        Direction(EnumFacing face, EnumFacing rot, int rotX, int rotY) {
            this.face = face;
            this.rot = rot;
            this.rotX = rotX;
            this.rotY = rotY;
        }

        /**
         * 通过传入两个方向，获取枚举
         */
        public static Direction byFacing(EnumFacing face, EnumFacing rot) {
            for (Direction direction : VALUES) {
                if (face == direction.face) {
                    if (face.getHorizontalIndex() >= 0) {
                        return direction;
                    } else if (rot == direction.rot) {
                        return direction;
                    }
                }
            }
            return UP_NORTH;
        }

        /**
         * 将朝向转换成一个 4x4 矩阵
         */
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

package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/7 10:58
 **/
public class BlockGarageKit extends Block implements ITileEntityProvider {
    private static final String DEFAULT_ID = "touhou_little_maid:entity.passive.maid";
    public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);

    public BlockGarageKit() {
        super(Material.CLAY);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "garage_kit");
        setHardness(0.5f);
        setRegistryName("garage_kit");
        setCreativeTab(MaidItems.TABS);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getItemStackFromBlock(world, pos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_AABB;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(getItemStackWithCharacter(DEFAULT_ID));
    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), getItemStackFromBlock(worldIn, pos)));
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        // 需要留空
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGarageKit();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityGarageKit) {
                ((TileEntityGarageKit) te).setCharacter(getCharacter(stack));
                ((TileEntityGarageKit) te).setFacing(EnumFacing.getDirectionFromEntityLiving(pos, placer));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.garage_kit.desc",
                I18n.format("entity." + EntityList.getTranslationName(new ResourceLocation(getCharacter(stack))) + ".name")));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
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
        // 否则玩家会卡死在方块里面窒息
        return false;
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    /**
     * 通过读取 TileEntityGarageKit 来获得对应 ItemStack
     */
    private ItemStack getItemStackFromBlock(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityGarageKit) {
            return getItemStackWithCharacter(((TileEntityGarageKit) te).getCharacter());
        } else {
            return getItemStackWithCharacter(DEFAULT_ID);
        }
    }

    /**
     * 获取带有指定实体 ID 的 ItemStack
     *
     * @param name 实体 ID
     */
    private ItemStack getItemStackWithCharacter(String name) {
        ItemStack stack = Item.getItemFromBlock(this).getDefaultInstance();
        getTagCompoundSafe(stack).setString(NBT.CHARACTER.getName(), name);
        return stack;
    }

    /**
     * 获取 ItemStack 中的 CHARACTER NBT 数据，如果不存在则返回默认值
     */
    public String getCharacter(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.CHARACTER.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.CHARACTER.getName());
        }
        return DEFAULT_ID;
    }

    /**
     * 安全获取 NBT 实例的方法
     */
    private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }

    public enum NBT {
        // 模型对应的实体 id
        CHARACTER("Character"),
        // 模型的方向
        FACING("Facing");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

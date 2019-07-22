package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGarageKit;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
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
    private static final String DEFAULT_MODEL = "touhou_little_maid:models/entity/hakurei_reimu.json";
    private static final String DEFAULT_TEXTURE = "touhou_little_maid:textures/entity/hakurei_reimu.png";
    private static final String DEFAULT_NAME = "{model.vanilla_touhou_model.hakurei_reimu.name}";
    private static final NBTTagCompound DEFAULT_DATA = new NBTTagCompound();
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
        for (ModelItem modelItem : CommonProxy.MODEL_LIST.getModelList()) {
            items.add(getItemStackWithData(DEFAULT_ID, modelItem.getModel(), modelItem.getTexture(), modelItem.getName(), DEFAULT_DATA));
        }
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
                ((TileEntityGarageKit) te).setData(this.getEntityId(stack), EnumFacing.getDirectionFromEntityLiving(pos, placer),
                        this.getModel(stack), this.getTexture(stack), this.getName(stack), this.getEntityData(stack));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.garage_kit.id.desc",
                I18n.format("entity." + EntityList.getTranslationName(new ResourceLocation(getEntityId(stack))) + ".name")));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.garage_kit.name.desc", ParseI18n.parse(getName(stack))));
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
            TileEntityGarageKit kit = (TileEntityGarageKit) te;
            return getItemStackWithData(kit.getEntityId(), kit.getModel(), kit.getTexture(), kit.getName(), kit.getEntityData());
        } else {
            return getItemStackWithData(DEFAULT_ID, DEFAULT_MODEL, DEFAULT_TEXTURE, DEFAULT_NAME, DEFAULT_DATA);
        }
    }

    /**
     * 获取带有指定数据的 ItemStack
     *
     * @param id         实体 id
     * @param model      模型地址
     * @param texture    材质地址
     * @param name       模型名称
     * @param entityData 模型存储的实体数据
     * @return 带有这些数据的物品堆
     */
    public ItemStack getItemStackWithData(String id, String model, String texture, String name, NBTTagCompound entityData) {
        ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
        getTagCompoundSafe(stack).setString(NBT.ENTITY_ID.getName(), id);
        getTagCompoundSafe(stack).setString(NBT.MODEL_LOCATION.getName(), model);
        getTagCompoundSafe(stack).setString(NBT.MODEL_TEXTURE.getName(), texture);
        getTagCompoundSafe(stack).setString(NBT.MODEL_NAME.getName(), name);
        getTagCompoundSafe(stack).setTag(NBT.MAID_DATA.getName(), entityData);
        return stack;
    }

    /**
     * 获取 ItemStack 中的 ENTITY_ID NBT 数据，如果不存在则返回默认值
     */
    public String getEntityId(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.ENTITY_ID.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.ENTITY_ID.getName());
        }
        return DEFAULT_ID;
    }

    /**
     * 获取 ItemStack 中的 MODEL NBT 数据，如果不存在则返回默认值
     */
    public String getModel(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.MODEL_LOCATION.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.MODEL_LOCATION.getName());
        }
        return DEFAULT_MODEL;
    }

    /**
     * 获取 ItemStack 中的 TEXTURE NBT 数据，如果不存在则返回默认值
     */
    public String getTexture(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.MODEL_TEXTURE.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.MODEL_TEXTURE.getName());
        }
        return DEFAULT_TEXTURE;
    }

    /**
     * 获取 ItemStack 中的 MODEL_NAME NBT 数据，如果不存在则返回默认值
     */
    public String getName(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.MODEL_NAME.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.MODEL_NAME.getName());
        }
        return DEFAULT_NAME;
    }

    /**
     * 获取 ItemStack 中的 MAID_DATA NBT 数据，如果不存在则返回默认值
     */
    public NBTTagCompound getEntityData(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getCompoundTag(NBT.MAID_DATA.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getCompoundTag(NBT.MAID_DATA.getName());
        }
        return DEFAULT_DATA;
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
        ENTITY_ID("EntityId"),
        // 模型的方向
        FACING("Facing"),
        // 模型位置
        MODEL_LOCATION("ModelLocation"),
        // 模型材质
        MODEL_TEXTURE("ModelTexture"),
        // 模型名称
        MODEL_NAME("ModelName"),
        // 女仆 NBT 数据
        MAID_DATA("MaidData");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

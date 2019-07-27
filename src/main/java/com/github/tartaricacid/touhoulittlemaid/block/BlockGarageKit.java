package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
    public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1D, 0.75D);
    private static final String DEFAULT_ENTITY_ID = "touhou_little_maid:entity.passive.maid";
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:hakurei_reimu";
    private static final NBTTagCompound DEFAULT_DATA = new NBTTagCompound();

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
        for (String key : CommonProxy.ID_NAME_MAP.keySet()) {
            items.add(getItemStackWithData(DEFAULT_ENTITY_ID, key, DEFAULT_DATA));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        spawnAsEntity(worldIn, pos, getItemStackFromBlock(worldIn, pos));
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
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityGarageKit) {
            ((TileEntityGarageKit) te).setData(this.getEntityId(stack), placer.getHorizontalFacing().getOpposite(),
                    this.getModelId(stack), this.getEntityData(stack));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String id = getEntityId(stack);
        tooltip.add(I18n.format("tooltips.touhou_little_maid.garage_kit.id.desc",
                I18n.format("entity." + EntityList.getTranslationName(new ResourceLocation(id)) + ".name")));
        if (id.equals("touhou_little_maid:entity.passive.maid")) {
            ModelItem modelItem = ClientProxy.ID_INFO_MAP.get(getModelId(stack));
            if (modelItem != null) {
                tooltip.add(I18n.format("tooltips.touhou_little_maid.garage_kit.name.desc", ParseI18n.parse(modelItem.getName())));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN;
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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (worldIn.isRemote || stack.getItem() != Items.SPAWN_EGG) {
            return false;
        }
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof TileEntityGarageKit)) {
            return false;
        }
        ResourceLocation id = ItemMonsterPlacer.getNamedIdFrom(stack);
        if (id == null || !EntityList.ENTITY_EGGS.containsKey(id)) {
            return false;
        }
        TileEntityGarageKit garageKit = (TileEntityGarageKit) tile;
        Entity entity = EntityList.createEntityByIDFromName(id, worldIn);
        if (entity instanceof EntityLiving)
        {
            ((EntityLiving) entity).onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
            if (entity instanceof EntityMaid) {
                garageKit.setData(id.toString(), garageKit.getFacing(), ((EntityMaid) entity).getModelId(), new NBTTagCompound());
                return true;
            }
        }
        garageKit.setData(id.toString(), garageKit.getFacing(), null, entity.writeToNBT(new NBTTagCompound()));
        return true;
    }

    // ------------------------------- 所有的 Get 和 Set 方法 ------------------------------- //

    /**
     * 通过读取 TileEntityGarageKit 来获得对应 ItemStack
     */
    private ItemStack getItemStackFromBlock(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityGarageKit) {
            TileEntityGarageKit kit = (TileEntityGarageKit) te;
            return getItemStackWithData(kit.getEntityId(), kit.getModelId(), kit.getMaidData());
        } else {
            return getItemStackWithData(DEFAULT_ENTITY_ID, DEFAULT_MODEL_ID, DEFAULT_DATA);
        }
    }

    /**
     * 获取带有指定数据的 ItemStack
     *
     * @param entityId   实体 id
     * @param modelId    模型的 id
     * @param entityData 模型存储的实体数据
     * @return 带有这些数据的物品堆
     */
    public ItemStack getItemStackWithData(String entityId, String modelId, NBTTagCompound entityData) {
        ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
        NBTTagCompound data = getTagCompoundSafe(stack);
        data.setString(NBT.ENTITY_ID.getName(), entityId);
        if (modelId != null) {
            data.setString(NBT.MODEL_ID.getName(), modelId);
        }
        if (!entityData.isEmpty()) {
            data.setTag(NBT.MAID_DATA.getName(), entityData);
        }
        return stack;
    }

    /**
     * 获取 ItemStack 中的 ENTITY_ID NBT 数据，如果不存在则返回默认值
     */
    public String getEntityId(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.ENTITY_ID.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.ENTITY_ID.getName());
        }
        return DEFAULT_ENTITY_ID;
    }

    /**
     * 获取 ItemStack 中的 MODEL_ID NBT 数据，如果不存在则返回默认值
     */
    public String getModelId(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getString(NBT.MODEL_ID.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getString(NBT.MODEL_ID.getName());
        }
        return DEFAULT_MODEL_ID;
    }

    /**
     * 获取 ItemStack 中的 MAID_DATA NBT 数据，如果不存在则返回默认值
     */
    public static NBTTagCompound getEntityData(ItemStack stack) {
        if (!getTagCompoundSafe(stack).getCompoundTag(NBT.MAID_DATA.getName()).isEmpty()) {
            return getTagCompoundSafe(stack).getCompoundTag(NBT.MAID_DATA.getName());
        }
        return DEFAULT_DATA;
    }


    /**
     * 安全获取 NBT 实例的方法
     */
    private static NBTTagCompound getTagCompoundSafe(ItemStack stack) {
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
        MODEL_ID("ModelId"),
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

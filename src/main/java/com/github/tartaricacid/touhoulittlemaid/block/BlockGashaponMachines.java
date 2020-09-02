package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.draw.DrawManger;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2020/1/3 14:54
 **/
public class BlockGashaponMachines extends BlockHorizontal {
    public BlockGashaponMachines() {
        super(Material.ROCK);
        setTranslationKey(TouhouLittleMaid.MOD_ID + "." + "gashapon_machines");
        setHardness(1.0f);
        setRegistryName("gashapon_machines");
        setCreativeTab(MaidItems.MODEL_COUPON_TABS);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        if (GeneralConfig.MAID_CONFIG.maidCannotChangeModel) {
            items.add(new ItemStack(this));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item purchaseItem = Item.getByNameOrId(GeneralConfig.GASHAPON_CONFIG.purchaseGashaponItem) == null ? Items.EMERALD : Item.getByNameOrId(GeneralConfig.GASHAPON_CONFIG.purchaseGashaponItem);
        boolean itemIsMatch = playerIn.getHeldItemMainhand().getItem() == purchaseItem || playerIn.getHeldItemMainhand().getItem() == MaidItems.GASHAPON_COIN;
        boolean amountIsMatch = playerIn.getHeldItemMainhand().getCount() >= GeneralConfig.GASHAPON_CONFIG.purchaseGashaponPrice;
        if (itemIsMatch && amountIsMatch) {
            playerIn.getHeldItemMainhand().shrink(GeneralConfig.GASHAPON_CONFIG.purchaseGashaponPrice);
            if (!worldIn.isRemote) {
                EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
                        new ItemStack(MaidItems.GASHAPON, 1, DrawManger.getGashaponLevel()));
                worldIn.spawnEntity(item);
            }
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.gashapon_machines.desc"));
    }
}

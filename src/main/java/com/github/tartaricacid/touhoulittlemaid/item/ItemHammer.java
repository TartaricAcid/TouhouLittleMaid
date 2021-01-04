package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemHammer extends Item {
    private static final String STORE_BLOCK_POS_TAG = "StoreBlockPos";

    public ItemHammer() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".hammer");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
        setMaxDamage(64);
    }

    public static void setStoreBlockPos(ItemStack hammer, BlockPos pos) {
        NBTTagCompound tag = hammer.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setTag(STORE_BLOCK_POS_TAG, NBTUtil.createPosTag(pos));
        hammer.setTagCompound(tag);
    }

    @Nullable
    public static BlockPos getStoreBlockPos(ItemStack hammer) {
        if (hammer.getItem() != MaidItems.HAMMER) {
            return null;
        }
        NBTTagCompound tag = hammer.getTagCompound();
        if (tag == null) {
            return null;
        }
        NBTTagCompound posTag = tag.getCompoundTag(STORE_BLOCK_POS_TAG);
        return NBTUtil.getPosFromTag(posTag);
    }

    @Override
    @ParametersAreNonnullByDefault
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.MAIN_HAND) {
            setStoreBlockPos(player.getHeldItemMainhand(), pos);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.hammer.desc"));
        BlockPos pos = getStoreBlockPos(stack);
        if (pos == null) {
            tooltip.add(I18n.format("tooltips.touhou_little_maid.hammer.binding_pos.none"));
        } else {
            tooltip.add(I18n.format("tooltips.touhou_little_maid.hammer.binding_pos.has", pos.getX(), pos.getY(), pos.getZ()));
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemPotionGuide extends Item {
    public static final int GUIDE_INV_SIZE = 6;
    public static final String GUIDE_INV_TAG = "PotionGuideInv";
    public static final String GUIDE_INDEX_TAG = "PotionIndex";
    public static final String GUIDE_POS_TAG = "BrewingStandPos";

    public ItemPotionGuide() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".potion_guide");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    public static ItemStackHandler getGuideInv(ItemStack guide) {
        PotionGuideItemStackHandler handler = new PotionGuideItemStackHandler(GUIDE_INV_SIZE);
        if (guide.getItem() instanceof ItemPotionGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag != null && tag.hasKey(GUIDE_INV_TAG, Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound tagInv = tag.getCompoundTag(GUIDE_INV_TAG);
                handler.deserializeNBT(tagInv);
            }
        }
        return handler;
    }

    public static void setGuideInv(ItemStack guide, ItemStackHandler itemStackHandler) {
        if (guide.getItem() instanceof ItemPotionGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(GUIDE_INV_TAG, itemStackHandler.serializeNBT());
            guide.setTagCompound(tag);
        }
    }

    public static int getGuideIndex(ItemStack guide) {
        if (guide.getItem() instanceof ItemPotionGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag != null && tag.hasKey(GUIDE_INDEX_TAG, Constants.NBT.TAG_INT)) {
                return tag.getInteger(GUIDE_INDEX_TAG);
            }
        }
        return 0;
    }

    public static void setGuideIndex(ItemStack guide, int index) {
        if (guide.getItem() instanceof ItemPotionGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setInteger(GUIDE_INDEX_TAG, MathHelper.clamp(index, 0, GUIDE_INV_SIZE - 1));
            guide.setTagCompound(tag);
        }
    }

    @Nullable
    public static BlockPos getGuidePos(ItemStack guide) {
        if (guide.getItem() instanceof ItemPotionGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag != null && tag.hasKey(GUIDE_POS_TAG, Constants.NBT.TAG_COMPOUND)) {
                return NBTUtil.getPosFromTag(tag.getCompoundTag(GUIDE_POS_TAG));
            }
        }
        return null;
    }

    public static void setGuidePos(ItemStack guide, BlockPos pos) {
        if (guide.getItem() instanceof ItemPotionGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(GUIDE_POS_TAG, NBTUtil.createPosTag(pos));
            guide.setTagCompound(tag);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND && playerIn.getHeldItem(handIn).getItem() instanceof ItemPotionGuide) {
            playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.POTION_GUIDE.getId(), worldIn, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ItemStackHandler handler = ItemPotionGuide.getGuideInv(stack);
        boolean hasItem = false;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                hasItem = true;
            }
        }
        if (hasItem) {
            tooltip.add("                        ");
            tooltip.add("                        ");
        }
        tooltip.add(I18n.format("tooltips.touhou_little_maid.potion_guide.desc"));
    }

    private static class PotionGuideItemStackHandler extends ItemStackHandler {
        private PotionGuideItemStackHandler(int size) {
            super(size);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }
}

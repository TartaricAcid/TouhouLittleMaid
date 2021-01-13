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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemFurnaceGuide extends Item {
    public static final int GUIDE_INV_SIZE = 16;
    public static final String GUIDE_INV_TAG = "FurnaceGuideInv";

    public ItemFurnaceGuide() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".furnace_guide");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    public static ItemStackHandler getGuideInv(ItemStack guide) {
        FurnaceGuideItemStackHandler handler = new FurnaceGuideItemStackHandler(GUIDE_INV_SIZE);
        if (guide.getItem() instanceof ItemFurnaceGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag != null && tag.hasKey(GUIDE_INV_TAG, Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound tagInv = tag.getCompoundTag(GUIDE_INV_TAG);
                handler.deserializeNBT(tagInv);
            }
        }
        return handler;
    }

    public static void setGuideInv(ItemStack guide, ItemStackHandler itemStackHandler) {
        if (guide.getItem() instanceof ItemFurnaceGuide) {
            NBTTagCompound tag = guide.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(GUIDE_INV_TAG, itemStackHandler.serializeNBT());
            guide.setTagCompound(tag);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND && playerIn.getHeldItem(handIn).getItem() instanceof ItemFurnaceGuide) {
            playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.FURNACE_GUIDE.getId(), worldIn, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ItemStackHandler handler = ItemFurnaceGuide.getGuideInv(stack);
        boolean hasItem = false;
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                hasItem = true;
            }
        }
        if (hasItem) {
            tooltip.add("                                ");
            tooltip.add("                                ");
            tooltip.add("                                ");
            tooltip.add("                                ");
        }
        tooltip.add(I18n.format("tooltips.touhou_little_maid.furnace_guide.desc"));
    }

    private static class FurnaceGuideItemStackHandler extends ItemStackHandler {
        private FurnaceGuideItemStackHandler(int size) {
            super(size);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }
}

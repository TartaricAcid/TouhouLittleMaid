package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWirelessIO extends Item {
    private static final int FILTER_LIST_SIZE = 9;
    private static final String FILTER_LIST_TAG = "ItemFilterList";
    private static final String FILTER_MODE_TAG = "ItemFilterMode";
    private static final String IO_MODE_TAG = "ItemIOMode";
    private static final String BINDING_POS = "BindingPos";
    private static final String SLOT_CONFIG_TAG = "SlotConfigData";

    private static final String TOOLTIPS_PREFIX = "\u00a7\u0061\u258d\u00a0\u00a7\u0037";

    public ItemWirelessIO() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".wireless_io");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.MAIN_TABS);
    }

    public static void setIOMode(ItemStack stack, boolean isInput) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
            }
            nbt.setBoolean(IO_MODE_TAG, isInput);
            stack.setTagCompound(nbt);
        }
    }

    /**
     * Input 代表女仆向箱子输入物品
     */
    public static boolean isInputMode(ItemStack stack) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            if (stack.hasTagCompound()) {
                NBTTagCompound nbt = stack.getTagCompound();
                return nbt.getBoolean(IO_MODE_TAG);
            }
        }
        return false;
    }

    public static void setFilterMode(ItemStack stack, boolean isBlacklist) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) {
                nbt = new NBTTagCompound();
            }
            nbt.setBoolean(FILTER_MODE_TAG, isBlacklist);
            stack.setTagCompound(nbt);
        }
    }

    public static boolean isBlacklist(ItemStack stack) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            if (stack.hasTagCompound()) {
                NBTTagCompound nbt = stack.getTagCompound();
                return nbt.getBoolean(FILTER_MODE_TAG);
            }
        }
        return false;
    }

    public static ItemStackHandler getFilterList(ItemStack stack) {
        WirelessIOHandler handler = new WirelessIOHandler(FILTER_LIST_SIZE);
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey(FILTER_LIST_TAG, Constants.NBT.TAG_COMPOUND)) {
                handler.deserializeNBT(tag.getCompoundTag(FILTER_LIST_TAG));
            }
        }
        return handler;
    }

    public static void setFilterList(ItemStack stack, ItemStackHandler itemStackHandler) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(FILTER_LIST_TAG, itemStackHandler.serializeNBT());
            stack.setTagCompound(tag);
        }
    }

    @Nullable
    public static BlockPos getBindingPos(ItemStack stack) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey(BINDING_POS, Constants.NBT.TAG_COMPOUND)) {
                return NBTUtil.getPosFromTag(tag.getCompoundTag(BINDING_POS));
            }
        }
        return null;
    }

    public static void setBindingPos(ItemStack stack, BlockPos pos) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(BINDING_POS, NBTUtil.createPosTag(pos));
            stack.setTagCompound(tag);
        }
    }

    public static void setSlotConfig(ItemStack stack, byte[] config) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            tag.setTag(SLOT_CONFIG_TAG, new NBTTagByteArray(config));
            stack.setTagCompound(tag);
        }
    }

    @Nullable
    public static byte[] getSlotConfig(ItemStack stack) {
        if (stack.getItem() == MaidItems.WIRELESS_IO) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey(SLOT_CONFIG_TAG, Constants.NBT.TAG_BYTE_ARRAY)) {
                return tag.getByteArray(SLOT_CONFIG_TAG);
            }
        }
        return null;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            if (chest.isLocked() && !player.canOpen(chest.getLockCode()) && !player.isSpectator()) {
                return EnumActionResult.FAIL;
            }
            if (hand == EnumHand.MAIN_HAND) {
                ItemStack stack = player.getHeldItemMainhand();
                setBindingPos(stack, pos);
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND) {
            playerIn.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.WIRELESS_IO.getId(), worldIn, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean isInput = isInputMode(stack);
        boolean isBlacklist = isBlacklist(stack);
        BlockPos pos = getBindingPos(stack);

        String ioModeText = isInput ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.io_mode.input") :
                I18n.format("tooltips.touhou_little_maid.wireless_io.io_mode.output");
        String filterModeText = isBlacklist ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.filter_mode.blacklist") :
                I18n.format("tooltips.touhou_little_maid.wireless_io.filter_mode.whitelist");
        String hasPos = (pos != null) ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.binding_pos.has",
                        pos.getX(), pos.getY(), pos.getZ()) :
                I18n.format("tooltips.touhou_little_maid.wireless_io.binding_pos.none");

        tooltip.add("                                    ");
        tooltip.add("                                    ");
        tooltip.add(I18n.format("tooltips.touhou_little_maid.wireless_io.usage.1"));
        tooltip.add(I18n.format("tooltips.touhou_little_maid.wireless_io.usage.2"));
        tooltip.add(TOOLTIPS_PREFIX + ioModeText);
        tooltip.add(TOOLTIPS_PREFIX + filterModeText);
        tooltip.add(TOOLTIPS_PREFIX + hasPos);
    }

    private static class WirelessIOHandler extends ItemStackHandler {
        private WirelessIOHandler(int size) {
            super(size);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }
}

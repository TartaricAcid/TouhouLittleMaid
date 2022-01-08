package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemWirelessIO extends Item implements INamedContainerProvider {
    private static final int FILTER_LIST_SIZE = 9;
    private static final String FILTER_LIST_TAG = "ItemFilterList";
    private static final String FILTER_MODE_TAG = "ItemFilterMode";
    private static final String IO_MODE_TAG = "ItemIOMode";
    private static final String BINDING_POS = "BindingPos";
    private static final String SLOT_CONFIG_TAG = "SlotConfigData";
    private static final String TOOLTIPS_PREFIX = "§a▍ §7";

    public ItemWirelessIO() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    public static void setMode(ItemStack stack, boolean maidToChest) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().putBoolean(IO_MODE_TAG, maidToChest);
        }
    }

    public static boolean isMaidToChest(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                return Objects.requireNonNull(nbt).getBoolean(IO_MODE_TAG);
            }
        }
        return false;
    }

    public static void setFilterMode(ItemStack stack, boolean isBlacklist) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().putBoolean(FILTER_MODE_TAG, isBlacklist);
        }
    }

    public static boolean isBlacklist(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                return Objects.requireNonNull(nbt).getBoolean(FILTER_MODE_TAG);
            }
        }
        return false;
    }

    public static ItemStackHandler getFilterList(ItemStack stack) {
        WirelessIOHandler handler = new WirelessIOHandler(FILTER_LIST_SIZE);
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null && tag.contains(FILTER_LIST_TAG, Constants.NBT.TAG_COMPOUND)) {
                handler.deserializeNBT(tag.getCompound(FILTER_LIST_TAG));
            }
        }
        return handler;
    }

    public static void setFilterList(ItemStack stack, ItemStackHandler itemStackHandler) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().put(FILTER_LIST_TAG, itemStackHandler.serializeNBT());
        }
    }

    @Nullable
    public static BlockPos getBindingPos(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null && tag.contains(BINDING_POS, Constants.NBT.TAG_COMPOUND)) {
                return NBTUtil.readBlockPos(tag.getCompound(BINDING_POS));
            }
        }
        return null;
    }

    public static void setBindingPos(ItemStack stack, BlockPos pos) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().put(BINDING_POS, NBTUtil.writeBlockPos(pos));
        }
    }

    public static void setSlotConfig(ItemStack stack, byte[] config) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().put(SLOT_CONFIG_TAG, new ByteArrayNBT(config));
        }
    }

    @Nullable
    public static byte[] getSlotConfig(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            CompoundNBT tag = stack.getTag();
            if (tag != null && tag.contains(SLOT_CONFIG_TAG, Constants.NBT.TAG_BYTE_ARRAY)) {
                return tag.getByteArray(SLOT_CONFIG_TAG);
            }
        }
        return null;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        TileEntity te = worldIn.getBlockEntity(pos);

        if (te instanceof ChestTileEntity && player != null) {
            ChestTileEntity chest = (ChestTileEntity) te;
            if (chest.canOpen(player) && hand == Hand.MAIN_HAND) {
                ItemStack stack = player.getMainHandItem();
                setBindingPos(stack, pos);
                return ActionResultType.sidedSuccess(worldIn.isClientSide);
            }
        }
        return super.useOn(context);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (handIn == Hand.MAIN_HAND && playerIn instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, this, (buffer) -> buffer.writeItem(playerIn.getMainHandItem()));
            return ActionResult.success(playerIn.getMainHandItem());
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        boolean maidToChest = isMaidToChest(stack);
        boolean isBlacklist = isBlacklist(stack);
        BlockPos pos = getBindingPos(stack);

        String ioModeText = maidToChest ?
                I18n.get("tooltips.touhou_little_maid.wireless_io.io_mode.input") :
                I18n.get("tooltips.touhou_little_maid.wireless_io.io_mode.output");
        String filterModeText = isBlacklist ?
                I18n.get("tooltips.touhou_little_maid.wireless_io.filter_mode.blacklist") :
                I18n.get("tooltips.touhou_little_maid.wireless_io.filter_mode.whitelist");
        String hasPos = (pos != null) ?
                I18n.get("tooltips.touhou_little_maid.wireless_io.binding_pos.has",
                        pos.getX(), pos.getY(), pos.getZ()) :
                I18n.get("tooltips.touhou_little_maid.wireless_io.binding_pos.none");

        tooltip.add(new StringTextComponent(TOOLTIPS_PREFIX + ioModeText));
        tooltip.add(new StringTextComponent(TOOLTIPS_PREFIX + filterModeText));
        tooltip.add(new StringTextComponent(TOOLTIPS_PREFIX + hasPos));
        tooltip.add(new StringTextComponent(" "));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.wireless_io.usage.1").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("tooltips.touhou_little_maid.wireless_io.usage.2").withStyle(TextFormatting.GRAY));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("item.touhou_little_maid.wireless_io");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new WirelessIOContainer(id, inventory, player.getMainHandItem());
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

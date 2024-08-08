package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemWirelessIO extends Item implements MenuProvider {
    private static final int FILTER_LIST_SIZE = 9;
    private static final String FILTER_LIST_TAG = "ItemFilterList";
    private static final DataComponentType<Boolean> FILTER_MODE_TAG = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            "ItemFilterMode",
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );
    private static final DataComponentType<Boolean> IO_MODE_TAG = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            "ItemIOMode",
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );
    private static final String BINDING_POS = "BindingPos";
    private static final String SLOT_CONFIG_TAG = "SlotConfigData";
    private static final String TOOLTIPS_PREFIX = "§a▍ §7";

    public ItemWirelessIO() {
        super((new Properties()).stacksTo(1));
    }

    public static void setMode(ItemStack stack, boolean maidToChest) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.set(IO_MODE_TAG, maidToChest);
        }
    }

    public static boolean isMaidToChest(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            if (stack.hasTag()) {
                CompoundTag nbt = stack.getTag();
                return Objects.requireNonNull(nbt).getBoolean(IO_MODE_TAG);
            }
        }
        return false;
    }

    public static void setFilterMode(ItemStack stack, boolean isBlacklist) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.set(FILTER_MODE_TAG, isBlacklist);
        }
    }

    public static boolean isBlacklist(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            if (stack.hasTag()) {
                CompoundTag nbt = stack.getTag();
                return Objects.requireNonNull(nbt).getBoolean(FILTER_MODE_TAG);
            }
        }
        return false;
    }

    public static ItemStackHandler getFilterList(ItemStack stack) {
        WirelessIOHandler handler = new WirelessIOHandler(FILTER_LIST_SIZE);
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(FILTER_LIST_TAG, Tag.TAG_COMPOUND)) {
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
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(BINDING_POS, Tag.TAG_COMPOUND)) {
                return NbtUtils.readBlockPos(tag.getCompound(BINDING_POS));
            }
        }
        return null;
    }

    public static void setBindingPos(ItemStack stack, BlockPos pos) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().put(BINDING_POS, NbtUtils.writeBlockPos(pos));
        }
    }

    public static void setSlotConfig(ItemStack stack, byte[] config) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            stack.getOrCreateTag().put(SLOT_CONFIG_TAG, new ByteArrayTag(config));
        }
    }

    @Nullable
    public static byte[] getSlotConfig(ItemStack stack) {
        if (stack.getItem() == InitItems.WIRELESS_IO.get()) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(SLOT_CONFIG_TAG, Tag.TAG_BYTE_ARRAY)) {
                return tag.getByteArray(SLOT_CONFIG_TAG);
            }
        }
        return null;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        BlockEntity te = worldIn.getBlockEntity(pos);

        if (hand != InteractionHand.MAIN_HAND) {
            return super.useOn(context);
        }
        if (player == null) {
            return super.useOn(context);
        }

        for (IChestType type : ChestManager.getAllChestTypes()) {
            if (!type.isChest(te)) {
                continue;
            }
            if (type.canOpenByPlayer(te, player)) {
                ItemStack stack = player.getMainHandItem();
                setBindingPos(stack, pos);
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND && playerIn instanceof ServerPlayer) {
            NetworkHooks.openScreen((ServerPlayer) playerIn, this, (buffer) -> buffer.writeItem(playerIn.getMainHandItem()));
            return InteractionResultHolder.success(playerIn.getMainHandItem());
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
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

        tooltip.add(Component.literal(TOOLTIPS_PREFIX + ioModeText));
        tooltip.add(Component.literal(TOOLTIPS_PREFIX + filterModeText));
        tooltip.add(Component.literal(TOOLTIPS_PREFIX + hasPos));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.wireless_io.usage.1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.wireless_io.usage.2").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.touhou_little_maid.wireless_io");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
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

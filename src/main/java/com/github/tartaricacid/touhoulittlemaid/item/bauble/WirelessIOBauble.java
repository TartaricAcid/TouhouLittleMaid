package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.util.BytesBooleansConvert.bytes2Booleans;

public class WirelessIOBauble implements IMaidBauble {
    private static final int SLOT_NUM = 38;

    @Nonnull
    public static ItemStack insertItemStacked(IItemHandler inventory, @Nonnull ItemStack stack, boolean simulate, @Nullable boolean[] slotConfig) {
        if (stack.isEmpty()) {
            return stack;
        }
        if (!stack.isStackable()) {
            return insertItem(inventory, stack, simulate, slotConfig);
        }
        int sizeInventory = inventory.getSlots();
        for (int i = 0; i < sizeInventory; i++) {
            ItemStack slot = inventory.getStackInSlot(i);
            if (slotConfig != null && i < slotConfig.length && slotConfig[i]) {
                continue;
            }
            if (canItemStacksStackRelaxed(slot, stack)) {
                stack = inventory.insertItem(i, stack, simulate);
                if (stack.isEmpty()) {
                    break;
                }
            }
        }

        if (!stack.isEmpty()) {
            for (int i = 0; i < sizeInventory; i++) {
                if (slotConfig != null && i < slotConfig.length && slotConfig[i]) {
                    continue;
                }
                if (inventory.getStackInSlot(i).isEmpty()) {
                    stack = inventory.insertItem(i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return stack;
    }

    public static ItemStack insertItem(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, @Nullable boolean[] slotConfig) {
        if (stack.isEmpty()) {
            return stack;
        }
        for (int i = 0; i < dest.getSlots(); i++) {
            if (slotConfig != null && i < slotConfig.length && slotConfig[i]) {
                continue;
            }
            stack = dest.insertItem(i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public static boolean canItemStacksStackRelaxed(@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || b.isEmpty() || a.getItem() != b.getItem()) {
            return false;
        }
        if (!a.isStackable()) {
            return false;
        }
        if (a.hasTag() != b.hasTag()) {
            return false;
        }
        return (!a.hasTag() || Objects.equals(a.getTag(), Objects.requireNonNull(b.getTag()))) && a.areCapsCompatible(b);
    }

    @Override
    public void onTick(EntityMaid maid, ItemStack baubleItem) {
        if (maid.tickCount % 100 == 0 && !maid.guiOpening) {
            BlockPos bindingPos = ItemWirelessIO.getBindingPos(baubleItem);
            if (bindingPos == null) {
                return;
            }
            float maxDistance = maid.getRestrictRadius();
            if (maid.distanceToSqr(bindingPos.getX(), bindingPos.getY(), bindingPos.getZ()) > (maxDistance * maxDistance)) {
                return;
            }
            BlockEntity te = maid.level.getBlockEntity(bindingPos);
            if (te == null) {
                return;
            }
            for (IChestType type : ChestManager.getAllChestTypes()) {
                if (!type.isChest(te)) {
                    continue;
                }
                int openCount = type.getOpenCount(maid.level, bindingPos, te);
                if (openCount > 0) {
                    return;
                }
                te.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(chestInv -> {
                    IItemHandler maidInv = maid.getAvailableInv(false);
                    boolean isMaidToChest = ItemWirelessIO.isMaidToChest(baubleItem);
                    boolean isBlacklist = ItemWirelessIO.isBlacklist(baubleItem);
                    byte[] slotConfigTmp = ItemWirelessIO.getSlotConfig(baubleItem);
                    if (slotConfigTmp != null) {
                        slotConfigTmp[maidInv.getSlots() - 2] = slotConfigTmp[SLOT_NUM - 2];
                        slotConfigTmp[maidInv.getSlots() - 1] = slotConfigTmp[SLOT_NUM - 1];
                    }
                    boolean[] slotConfigData = bytes2Booleans(slotConfigTmp, SLOT_NUM);
                    IItemHandler filterList = ItemWirelessIO.getFilterList(baubleItem);

                    if (isMaidToChest) {
                        maidToChest(maidInv, chestInv, isBlacklist, filterList, slotConfigData);
                    } else {
                        chestToMaid(chestInv, maidInv, isBlacklist, filterList, slotConfigData);
                    }
                });
                return;
            }
        }
    }

    private void maidToChest(IItemHandler maid, IItemHandler chest, boolean isBlacklist, IItemHandler filterList, boolean[] slotConfig) {
        for (int i = 0; i < maid.getSlots(); i++) {
            if (i < slotConfig.length && slotConfig[i]) {
                continue;
            }
            ItemStack maidInvItem = maid.getStackInSlot(i);
            boolean allowMove = isBlacklist;
            for (int j = 0; j < filterList.getSlots(); j++) {
                ItemStack filterItem = filterList.getStackInSlot(j);
                boolean isEqual = ItemStack.isSameItem(maidInvItem, filterItem);
                if (isEqual) {
                    allowMove = !isBlacklist;
                    break;
                }
            }
            if (allowMove) {
                int beforeCount = maidInvItem.getCount();
                ItemStack after = ItemHandlerHelper.insertItemStacked(chest, maidInvItem.copy(), false);
                int afterCount = after.getCount();
                // Sync Client & Server
                if (beforeCount != afterCount) {
                    maid.extractItem(i, beforeCount - afterCount, false);
                }
            }
        }
    }

    private void chestToMaid(IItemHandler chest, IItemHandler maid, boolean isBlacklist, IItemHandler filterList, boolean[] slotConfig) {
        for (int i = 0; i < chest.getSlots(); i++) {
            ItemStack chestInvStack = chest.getStackInSlot(i);
            boolean allowMove = isBlacklist;
            for (int j = 0; j < filterList.getSlots(); j++) {
                ItemStack filterItem = filterList.getStackInSlot(j);
                boolean isEqual = ItemStack.isSameItem(chestInvStack, filterItem);
                if (isEqual) {
                    allowMove = !isBlacklist;
                    break;
                }
            }
            if (allowMove) {
                int beforeCount = chestInvStack.getCount();
                ItemStack after = insertItemStacked(maid, chestInvStack.copy(), false, slotConfig);
                int afterCount = after.getCount();
                // Sync Client & Server
                if (beforeCount != afterCount) {
                    chest.extractItem(i, beforeCount - afterCount, false);
                }
            }
        }
    }
}

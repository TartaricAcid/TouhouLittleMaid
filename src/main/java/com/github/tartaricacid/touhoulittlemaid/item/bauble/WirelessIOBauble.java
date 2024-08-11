package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class WirelessIOBauble implements IMaidBauble {
    private static final int SLOT_NUM = 38;

    @Nonnull
    public static ItemStack insertItemStacked(IItemHandler inventory, @Nonnull ItemStack stack, boolean simulate, @Nullable List<Boolean> slotConfig) {
        if (stack.isEmpty()) {
            return stack;
        }
        if (!stack.isStackable()) {
            return insertItem(inventory, stack, simulate, slotConfig);
        }
        int sizeInventory = inventory.getSlots();
        for (int i = 0; i < sizeInventory; i++) {
            ItemStack slot = inventory.getStackInSlot(i);
            if (slotConfig != null && i < slotConfig.size() && slotConfig.get(i)) {
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
                if (slotConfig != null && i < slotConfig.size() && slotConfig.get(i)) {
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

    public static ItemStack insertItem(IItemHandler dest, @Nonnull ItemStack stack, boolean simulate, @Nullable List<Boolean> slotConfig) {
        if (stack.isEmpty()) {
            return stack;
        }
        for (int i = 0; i < dest.getSlots(); i++) {
            if (slotConfig != null && i < slotConfig.size() && slotConfig.get(i)) {
                continue;
            }
            stack = dest.insertItem(i, stack, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    public static boolean canItemStacksStackRelaxed(@Nonnull ItemStack slot, @Nonnull ItemStack itemStack) {
        if (slot.isEmpty() || itemStack.isEmpty() || slot.getItem() != itemStack.getItem()) {
            return false;
        }
        if (!slot.isStackable()) {
            return false;
        }
        if (slot.hasTag() != itemStack.hasTag()) {
            return false;
        }
        return (!slot.hasTag() || Objects.equals(slot.getTag(), Objects.requireNonNull(itemStack.getTag()))) && slot.areCapsCompatible(itemStack);
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
                IItemHandler chestInv = maid.level.getCapability(Capabilities.ItemHandler.BLOCK, te.getBlockPos(), null);
                if (chestInv != null) {
                    IItemHandler maidInv = maid.getAvailableInv(false);
                    boolean isMaidToChest = ItemWirelessIO.isMaidToChest(baubleItem);
                    boolean isBlacklist = ItemWirelessIO.isBlacklist(baubleItem);
                    List<Boolean> slotConfig = ItemWirelessIO.getSlotConfig(baubleItem);
                    List<Boolean> slotConfigData;
                    if (slotConfig != null) {
                        slotConfigData = new ArrayList<>(slotConfig);
                        slotConfigData.set(maidInv.getSlots() - 2, slotConfig.get(SLOT_NUM - 2));
                        slotConfigData.set(maidInv.getSlots() - 1, slotConfig.get(SLOT_NUM - 1));
                    } else {
                        slotConfigData = new ArrayList<>(Collections.nCopies(SLOT_NUM, false));
                    }
                    IItemHandler filterList = ItemWirelessIO.getFilterList(maid.registryAccess(), baubleItem);

                    if (isMaidToChest) {
                        maidToChest(maidInv, chestInv, isBlacklist, filterList, slotConfigData);
                    } else {
                        chestToMaid(chestInv, maidInv, isBlacklist, filterList, slotConfigData);
                    }
                }
                return;
            }
        }
    }

    private void maidToChest(IItemHandler maid, IItemHandler chest, boolean isBlacklist, IItemHandler filterList, List<Boolean> slotConfig) {
        for (int i = 0; i < maid.getSlots(); i++) {
            if (i < slotConfig.size() && slotConfig.get(i)) {
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

    private void chestToMaid(IItemHandler chest, IItemHandler maid, boolean isBlacklist, IItemHandler filterList, List<Boolean> slotConfig) {
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

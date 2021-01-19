package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.util.BytesBooleansConvert.bytes2Booleans;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class WirelessIOBauble implements IMaidBauble {
    public static final int MAX_DISTANCE = 16;

    @Nonnull
    public static ItemStack insertItemStacked(IItemHandler inventory, @Nonnull ItemStack stack, boolean simulate, @Nullable boolean[] slotConfig) {
        if (inventory == null || stack.isEmpty()) {
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
        if (dest == null || stack.isEmpty()) {
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

        if (a.getHasSubtypes() && a.getMetadata() != b.getMetadata()) {
            return false;
        }

        if (a.hasTagCompound() != b.hasTagCompound()) {
            return false;
        }

        return (!a.hasTagCompound() || Objects.equals(a.getTagCompound(), Objects.requireNonNull(b.getTagCompound()))) && a.areCapsCompatible(b);
    }

    @Override
    public void onTick(AbstractEntityMaid entityMaid, ItemStack baubleItem) {
        if (entityMaid.ticksExisted % 100 == 0) {
            // 判断距离
            BlockPos bindingPos = ItemWirelessIO.getBindingPos(baubleItem);
            if (bindingPos == null) {
                return;
            }
            if (entityMaid.getDistanceSqToCenter(bindingPos) > (MAX_DISTANCE * MAX_DISTANCE)) {
                return;
            }

            // 判断是不是箱子
            TileEntity te = entityMaid.world.getTileEntity(bindingPos);
            if (!(te instanceof TileEntityChest)) {
                return;
            }

            // 锁住的箱子无法放置
            TileEntityChest chest = (TileEntityChest) te;
            if (chest.isLocked()) {
                return;
            }

            IItemHandler chestInv = chest.getCapability(ITEM_HANDLER_CAPABILITY, null);
            if (chestInv == null) {
                return;
            }

            IItemHandler maidInv = entityMaid.getAvailableInv(false);
            boolean isInput = ItemWirelessIO.isInputMode(baubleItem);
            boolean isBlacklist = ItemWirelessIO.isBlacklist(baubleItem);
            byte[] slotConfigTmp = ItemWirelessIO.getSlotConfig(baubleItem);
            if (slotConfigTmp != null) {
                slotConfigTmp[maidInv.getSlots() - 2] = slotConfigTmp[45];
                slotConfigTmp[maidInv.getSlots() - 1] = slotConfigTmp[46];
            }
            boolean[] slotConfigData = bytes2Booleans(slotConfigTmp);
            IItemHandler filterList = ItemWirelessIO.getFilterList(baubleItem);

            if (isInput) {
                maidToChest(maidInv, chestInv, isBlacklist, filterList, slotConfigData);
            } else {
                chestToMaid(chestInv, maidInv, isBlacklist, filterList, slotConfigData);
            }
        }
    }

    private void maidToChest(IItemHandler maid, IItemHandler chest, boolean isBlacklist, IItemHandler filterList, boolean[] slotConfig) {
        for (int i = 0; i < maid.getSlots(); i++) {
            if (slotConfig != null && i < slotConfig.length && slotConfig[i]) {
                continue;
            }
            ItemStack maidInvItem = maid.getStackInSlot(i);
            boolean allowMove = isBlacklist;
            for (int j = 0; j < filterList.getSlots(); j++) {
                ItemStack filterItem = filterList.getStackInSlot(j);
                boolean isEqual = maidInvItem.isItemEqualIgnoreDurability(filterItem);
                if (isEqual) {
                    allowMove = !isBlacklist;
                    break;
                }
            }
            if (allowMove) {
                int beforeCount = maidInvItem.getCount();
                ItemStack after = ItemHandlerHelper.insertItemStacked(chest, maidInvItem.copy(), false);
                int afterCount = after.getCount();
                // 避免客户端服务端不同步
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
                boolean isEqual = chestInvStack.isItemEqualIgnoreDurability(filterItem);
                if (isEqual) {
                    allowMove = !isBlacklist;
                    break;
                }
            }
            if (allowMove) {
                int beforeCount = chestInvStack.getCount();
                ItemStack after = insertItemStacked(maid, chestInvStack.copy(), false, slotConfig);
                int afterCount = after.getCount();
                // 避免客户端服务端不同步
                if (beforeCount != afterCount) {
                    chest.extractItem(i, beforeCount - afterCount, false);
                }
            }
        }
    }
}

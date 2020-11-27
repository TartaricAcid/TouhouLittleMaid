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

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class WirelessIOBauble implements IMaidBauble {
    public static final int MAX_DISTANCE = 16;

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
            IItemHandler filterList = ItemWirelessIO.getFilterList(baubleItem);

            if (isInput) {
                moveItemByFilter(maidInv, chestInv, isBlacklist, filterList);
            } else {
                moveItemByFilter(chestInv, maidInv, isBlacklist, filterList);
            }
        }
    }

    private void moveItemByFilter(IItemHandler fromInv, IItemHandler toInv, boolean isBlacklist, IItemHandler filterList) {
        for (int i = 0; i < fromInv.getSlots(); i++) {
            ItemStack fromInvItem = fromInv.getStackInSlot(i);
            boolean allowMove = isBlacklist;
            for (int j = 0; j < filterList.getSlots(); j++) {
                ItemStack filterItem = filterList.getStackInSlot(j);
                boolean isEqual = fromInvItem.isItemEqualIgnoreDurability(filterItem);
                if (isEqual) {
                    allowMove = !isBlacklist;
                    break;
                }
            }
            if (allowMove) {
                int beforeCount = fromInvItem.getCount();
                ItemStack after = ItemHandlerHelper.insertItemStacked(toInv, fromInvItem.copy(), false);
                int afterCount = after.getCount();
                // 避免客户端服务端不同步
                if (beforeCount != afterCount) {
                    fromInv.extractItem(i, beforeCount - afterCount, false);
                }
            }
        }
    }
}

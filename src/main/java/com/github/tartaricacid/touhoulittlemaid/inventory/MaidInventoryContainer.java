package com.github.tartaricacid.touhoulittlemaid.inventory;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MaidInventoryContainer extends MaidMainContainer {
    public MaidInventoryContainer(IInventory playerInventory, EntityMaid maid, int taskIndex) {
        super(playerInventory, maid, taskIndex);
        addMaidInventorySlots();
    }

    private void addMaidInventorySlots() {
        IItemHandler itemHandler = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // 女仆物品栏
        for (int l = 0; l < 3; ++l) {
            for (int j = 0; j < 5; ++j) {
                // 物品栏占用 6-20
                this.addSlotToContainer(new SlotItemHandler(itemHandler, 6 + j + l * 5, 80 + j * 18, 8 + l * 18));
            }
        }
    }

    /**
     * 处理 Shift 点击情况下的物品逻辑
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 42) {
                if (!this.mergeItemStack(itemstack1, 42, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 42, true)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
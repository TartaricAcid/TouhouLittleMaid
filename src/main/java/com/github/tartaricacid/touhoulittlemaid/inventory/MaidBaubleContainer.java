package com.github.tartaricacid.touhoulittlemaid.inventory;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class MaidBaubleContainer extends MaidMainContainer {
    public MaidBaubleContainer(IInventory playerInventory, EntityMaid entityMaid, int taskIndex) {
        super(playerInventory, entityMaid, taskIndex);
        addMaidBaubleSlots();
    }

    private void addMaidBaubleSlots() {
        IItemHandler itemHandler = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // 女仆饰品栏
        for (int l = 0; l < 2; ++l) {
            for (int j = 0; j < 4; ++j) {
                // 物品栏占用 6-20
                this.addSlotToContainer(new SlotItemHandler(itemHandler, 21 + j + l * 4, 80 + j * 18, 8 + l * 18) {
                    @Override
                    public int getSlotStackLimit() {
                        return 1;
                    }
                });
            }
        }
    }

    /**
     * 处理 Shift 点击情况下的物品逻辑
     */
    @Nonnull
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

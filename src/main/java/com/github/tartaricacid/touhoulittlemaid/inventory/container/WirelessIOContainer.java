package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class WirelessIOContainer extends Container {
    public static final ContainerType<WirelessIOContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new WirelessIOContainer(windowId, inv, data.readItem()));
    private final ItemStack wirelessIO;
    private final ItemStackHandler filterListInv;

    public WirelessIOContainer(int id, PlayerInventory inventory, ItemStack wirelessIO) {
        super(TYPE, id);
        this.wirelessIO = wirelessIO;
        this.filterListInv = ItemWirelessIO.getFilterList(wirelessIO);
        this.addPlayerSlots(inventory);
        this.addWirelessIOSlots();
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return playerIn.getMainHandItem().getItem() == InitItems.WIRELESS_IO.get();
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 27 + player.inventory.selected) {
            return player.inventory.getItem(slotId);
        }
        ItemStack stack = super.clicked(slotId, dragType, clickTypeIn, player);
        ItemWirelessIO.setFilterList(wirelessIO, filterListInv);
        return stack;
    }

    private void addWirelessIOSlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new WirelessIOSlotItemHandler(filterListInv, col + row * 3, 62 + col * 18, 17 + row * 18));
            }
        }
    }

    private void addPlayerSlots(PlayerInventory inventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index < 36) {
                if (!this.moveItemStackTo(stack2, 36, 45, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack1;
    }

    public ItemStack getWirelessIO() {
        return wirelessIO;
    }

    private class WirelessIOSlotItemHandler extends SlotItemHandler {
        private WirelessIOSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}

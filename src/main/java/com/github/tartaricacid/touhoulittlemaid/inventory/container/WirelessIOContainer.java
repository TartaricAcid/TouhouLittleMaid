package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class WirelessIOContainer extends AbstractContainerMenu {
    public static final MenuType<WirelessIOContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new WirelessIOContainer(windowId, inv, data.readItem()));
    private final ItemStack wirelessIO;
    private final ItemStackHandler filterListInv;

    public WirelessIOContainer(int id, Inventory inventory, ItemStack wirelessIO) {
        super(TYPE, id);
        this.wirelessIO = wirelessIO;
        this.filterListInv = ItemWirelessIO.getFilterList(wirelessIO);
        this.addPlayerSlots(inventory);
        this.addWirelessIOSlots();
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.getMainHandItem().getItem() == InitItems.WIRELESS_IO.get();
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickTypeIn, Player player) {
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 27 + player.getInventory().selected) {
            return;
        }
        if (clickTypeIn == ClickType.SWAP) {
            return;
        }
        super.clicked(slotId, button, clickTypeIn, player);
        ItemWirelessIO.setFilterList(wirelessIO, filterListInv);
    }

    private void addWirelessIOSlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new WirelessIOSlotItemHandler(filterListInv, col + row * 3, 62 + col * 18, 17 + row * 18));
            }
        }
    }

    private void addPlayerSlots(Inventory inventory) {
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
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(stack2, 27, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, 27, false)) {
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

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return EntityMaid.canInsertItem(stack) && super.mayPlace(stack);
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.inventory;

import com.github.tartaricacid.touhoulittlemaid.item.ItemAlbum;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/18 21:07
 **/
public class AlbumContainer extends Container {
    private ItemStack album;
    private ItemStackHandler itemStackHandler;

    public AlbumContainer(IInventory playerInventory, ItemStack album) {
        this.album = album;
        this.itemStackHandler = ItemAlbum.getAlbumInv(album);
        addPlayerSlots(playerInventory);
        addAlbumSlots();
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 27 + player.inventory.currentItem) {
            return player.inventory.getStackInSlot(slotId);
        }
        ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
        ItemAlbum.setAlbumInv(album, itemStackHandler);
        return stack;
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // 玩家物品栏，占用 0-35
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 48 + j1 * 18, 132 + l * 18));
            }
        }

        // 玩家快捷栏
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 48 + i1 * 18, 190));
        }
    }

    private void addAlbumSlots() {
        for (int l = 0; l < 4; ++l) {
            for (int j = 0; j < 4; ++j) {
                this.addSlotToContainer(new AlbumSlotSlotItemHandler(itemStackHandler, j + l * 4, 26 + j * 20, 21 + l * 20));
            }
        }
        for (int l = 0; l < 4; ++l) {
            for (int j = 0; j < 4; ++j) {
                this.addSlotToContainer(new AlbumSlotSlotItemHandler(itemStackHandler, 16 + j + l * 4, 154 + j * 20, 21 + l * 20));
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 36) {
                if (!this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 36, true)) {
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

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return playerIn.getHeldItemMainhand().getItem() instanceof ItemAlbum;
    }

    private class AlbumSlotSlotItemHandler extends SlotItemHandler {
        private AlbumSlotSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem() instanceof ItemPhoto && super.isItemValid(stack);
        }
    }
}

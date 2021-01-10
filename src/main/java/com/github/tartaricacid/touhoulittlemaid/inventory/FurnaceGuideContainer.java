package com.github.tartaricacid.touhoulittlemaid.inventory;

import com.github.tartaricacid.touhoulittlemaid.item.ItemFurnaceGuide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class FurnaceGuideContainer extends Container {
    private final ItemStack guide;
    private final ItemStackHandler itemStackHandler;

    public FurnaceGuideContainer(IInventory playerInventory, ItemStack guide) {
        this.guide = guide;
        this.itemStackHandler = ItemFurnaceGuide.getGuideInv(guide);
        addPlayerSlots(playerInventory);
        addGuideSlots();
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 27 + player.inventory.currentItem) {
            return player.inventory.getStackInSlot(slotId);
        }
        ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
        ItemFurnaceGuide.setGuideInv(guide, itemStackHandler);
        return stack;
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // 玩家物品栏，占用 0-35
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 51 + l * 18));
            }
        }

        // 玩家快捷栏
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
    }

    private void addGuideSlots() {
        for (int l = 0; l < 8; ++l) {
            this.addSlotToContainer(new GuideInputSlotItemHandler(itemStackHandler, l, 26 + l * 18, 8));

        }
        for (int l = 8; l < 16; ++l) {
            this.addSlotToContainer(new GuideFuelSlotItemHandler(itemStackHandler, l, 26 + (l - 8) * 18, 30));
        }
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
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
        return playerIn.getHeldItemMainhand().getItem() instanceof ItemFurnaceGuide;
    }

    private static class GuideFuelSlotItemHandler extends SlotItemHandler {
        private GuideFuelSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return TileEntityFurnace.isItemFuel(stack);
        }
    }

    private static class GuideInputSlotItemHandler extends SlotItemHandler {
        private GuideInputSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}

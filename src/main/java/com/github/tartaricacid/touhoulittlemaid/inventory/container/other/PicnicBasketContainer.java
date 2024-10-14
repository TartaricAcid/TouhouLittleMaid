package com.github.tartaricacid.touhoulittlemaid.inventory.container.other;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPicnicBasket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PicnicBasketContainer extends AbstractContainerMenu {
    public static final MenuType<PicnicBasketContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new PicnicBasketContainer(windowId, inv, data.readItem()));
    private final ItemStack picnicBasket;
    private final ItemStackHandler container;

    public PicnicBasketContainer(int id, Inventory inventory, ItemStack picnicBasket) {
        super(TYPE, id);
        this.picnicBasket = picnicBasket;
        this.container = ItemPicnicBasket.getContainer(picnicBasket);
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new SlotItemHandler(container, i, 8 + i * 18, 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.isEdible();
                }
            });
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 49 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 107));
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickTypeIn, Player player) {
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 36 + player.getInventory().selected) {
            return;
        }
        if (clickTypeIn == ClickType.SWAP) {
            return;
        }
        super.clicked(slotId, button, clickTypeIn, player);
        ItemPicnicBasket.setContainer(picnicBasket, container);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack output = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            output = stack.copy();
            if (index < 9) {
                if (!this.moveItemStackTo(stack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return output;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().getItem() == InitItems.PICNIC_BASKET.get();
    }
}

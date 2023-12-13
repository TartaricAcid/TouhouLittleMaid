package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BigBackpackContainer extends MaidMainContainer {
    public static final ContainerType<BigBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new BigBackpackContainer(windowId, inv, data.readInt()));

    public BigBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(PlayerInventory inventory) {
        IItemHandler itemHandler = maid.getMaidInv();
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 6 + i, 143 + 18 * i, 59));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 12 + i, 143 + 18 * i, 82));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 18 + i, 143 + 18 * i, 100));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 24 + i, 143 + 18 * i, 123));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 30 + i, 143 + 18 * i, 141));
        }
    }
}

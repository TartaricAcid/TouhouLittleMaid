package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BigBackpackContainer extends MaidMainContainer {
    public static final MenuType<BigBackpackContainer> TYPE = IMenuTypeExtension.create((windowId, inv, data) -> new BigBackpackContainer(windowId, inv, data.readInt()));

    public BigBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(Inventory inventory) {
        for (int i = 0; i < 6; i++) {
            addSlot(new BackpackSlot(maid, 6 + i, 143 + 18 * i, 59));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BackpackSlot(maid, 12 + i, 143 + 18 * i, 82));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BackpackSlot(maid, 18 + i, 143 + 18 * i, 100));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BackpackSlot(maid, 24 + i, 143 + 18 * i, 123));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new BackpackSlot(maid, 30 + i, 143 + 18 * i, 141));
        }
    }
}

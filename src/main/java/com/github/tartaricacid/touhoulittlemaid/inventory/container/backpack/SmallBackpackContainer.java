package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.SlotItemHandler;

public class SmallBackpackContainer extends MaidMainContainer {
    public static final MenuType<SmallBackpackContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new SmallBackpackContainer(windowId, inv, data.readInt()));

    public SmallBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(Inventory inventory) {
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(maid.getMaidInv(), 6 + i, 143 + 18 * i, 59));
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;

public class SmallBackpackContainer extends MaidMainContainer {
    public static final ContainerType<SmallBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new SmallBackpackContainer(windowId, inv, data.readInt()));

    public SmallBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(PlayerInventory inventory) {
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(maid.getMaidInv(), 6 + i, 143 + 18 * i, 59));
        }
    }
}

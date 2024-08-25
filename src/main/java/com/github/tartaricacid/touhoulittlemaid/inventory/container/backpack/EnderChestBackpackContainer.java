package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class EnderChestBackpackContainer extends MaidMainContainer {
    public static final MenuType<EnderChestBackpackContainer> TYPE = IMenuTypeExtension.create((windowId, inv, data) -> new EnderChestBackpackContainer(windowId, inv, data.readInt()));

    public EnderChestBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(Inventory inventory) {
        PlayerEnderChestContainer enderChestContainer = inventory.player.getEnderChestInventory();
        for (int i = 0; i < 6; i++) {
            addSlot(new Slot(enderChestContainer, i, 143 + 18 * i, 61));
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 5; i++) {
                addSlot(new Slot(enderChestContainer, 6 + 5 * j + i, 161 + 18 * i, 79 + j * 18));
            }
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new Slot(enderChestContainer, 21 + i, 143 + 18 * i, 133));
        }
    }
}

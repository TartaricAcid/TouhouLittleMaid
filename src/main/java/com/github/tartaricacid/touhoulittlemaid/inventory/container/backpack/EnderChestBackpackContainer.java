package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class EnderChestBackpackContainer extends MaidMainContainer {
    public static final MenuType<EnderChestBackpackContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new EnderChestBackpackContainer(windowId, inv, data.readInt()));

    public EnderChestBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(Inventory inventory) {
        PlayerEnderChestContainer enderChestContainer = inventory.player.getEnderChestInventory();
        for (int i = 0; i < 3; i++) {
            addSlot(new Slot(enderChestContainer, i, 143 + 18 * i, 69));
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                addSlot(new Slot(enderChestContainer, 3 + 6 * j + i, 143 + 18 * i, 87 + j * 18));
            }
        }
    }
}

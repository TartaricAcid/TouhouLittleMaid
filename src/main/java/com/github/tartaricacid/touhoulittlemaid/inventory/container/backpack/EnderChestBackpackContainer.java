package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class EnderChestBackpackContainer extends MaidMainContainer {
    public static final ContainerType<EnderChestBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new EnderChestBackpackContainer(windowId, inv, data.readInt()));

    public EnderChestBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(PlayerInventory inventory) {
        EnderChestInventory enderChestContainer = inventory.player.getEnderChestInventory();
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

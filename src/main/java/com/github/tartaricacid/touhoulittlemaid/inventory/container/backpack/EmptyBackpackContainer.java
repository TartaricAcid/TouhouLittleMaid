package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class EmptyBackpackContainer extends MaidMainContainer {
    public static final ContainerType<EmptyBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new EmptyBackpackContainer(windowId, inv, data.readInt()));

    public EmptyBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    @Override
    protected void addBackpackInv(PlayerInventory inventory) {
    }
}
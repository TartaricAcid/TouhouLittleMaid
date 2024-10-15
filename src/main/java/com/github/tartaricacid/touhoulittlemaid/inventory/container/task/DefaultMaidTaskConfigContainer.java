package com.github.tartaricacid.touhoulittlemaid.inventory.container.task;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class DefaultMaidTaskConfigContainer extends TaskConfigContainer {
    public static final MenuType<DefaultMaidTaskConfigContainer> TYPE = IMenuTypeExtension.create(
            (windowId, inv, data) -> new DefaultMaidTaskConfigContainer(windowId, inv, data.readInt()));

    public DefaultMaidTaskConfigContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}

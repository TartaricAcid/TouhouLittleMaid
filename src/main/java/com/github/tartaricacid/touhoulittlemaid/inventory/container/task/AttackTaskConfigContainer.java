package com.github.tartaricacid.touhoulittlemaid.inventory.container.task;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class AttackTaskConfigContainer extends TaskConfigContainer {
    public static final MenuType<AttackTaskConfigContainer> TYPE = IForgeMenuType.create(
            (windowId, inv, data) -> new AttackTaskConfigContainer(windowId, inv, data.readInt()));

    public AttackTaskConfigContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }
}

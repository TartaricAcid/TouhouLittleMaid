package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class MaidConfigContainer extends AbstractMaidContainer {
    public static final MenuType<MaidConfigContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new MaidConfigContainer(windowId, inv, data.readInt()));

    public MaidConfigContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    public static MenuProvider create(int entityId) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TextComponent("Maid Config Container");
            }

            @Override
            public AbstractContainerMenu createMenu(int index, Inventory playerInventory, Player player) {
                return new MaidConfigContainer(index, playerInventory, entityId);
            }
        };
    }
}
package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class MaidConfigContainer extends AbstractMaidContainer {
    public static final ContainerType<MaidConfigContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new MaidConfigContainer(windowId, inv, data.readInt()));

    public MaidConfigContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
    }

    public static INamedContainerProvider create(int entityId) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Maid Config Container");
            }

            @Override
            public Container createMenu(int index, PlayerInventory playerInventory, PlayerEntity player) {
                return new MaidConfigContainer(index, playerInventory, entityId);
            }
        };
    }
}

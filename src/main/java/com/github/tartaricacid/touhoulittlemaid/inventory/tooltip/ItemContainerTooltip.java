package com.github.tartaricacid.touhoulittlemaid.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.items.IItemHandler;

public class ItemContainerTooltip implements TooltipComponent {
    private final IItemHandler handler;

    public ItemContainerTooltip(IItemHandler handler) {
        this.handler = handler;
    }

    public IItemHandler getHandler() {
        return handler;
    }
}

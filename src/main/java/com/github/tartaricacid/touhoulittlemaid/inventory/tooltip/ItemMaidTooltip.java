package com.github.tartaricacid.touhoulittlemaid.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class ItemMaidTooltip implements TooltipComponent {
    private final String modelId;
    private final String customName;

    public ItemMaidTooltip(String modelId, String customName) {
        this.modelId = modelId;
        this.customName = customName;
    }

    public String getModelId() {
        return modelId;
    }

    public String getCustomName() {
        return customName;
    }
}

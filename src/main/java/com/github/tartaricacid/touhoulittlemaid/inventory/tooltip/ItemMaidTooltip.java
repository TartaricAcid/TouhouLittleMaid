package com.github.tartaricacid.touhoulittlemaid.inventory.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record ItemMaidTooltip(String modelId, String customName) implements TooltipComponent {
}

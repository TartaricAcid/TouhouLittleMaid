package com.github.tartaricacid.touhoulittlemaid.client.tooltip;

import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.ItemContainerTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class ClientItemContainerTooltip implements ClientTooltipComponent {
    private final NonNullList<ItemStack> items = NonNullList.create();
    private @Nullable MutableComponent emptyTip = null;

    public ClientItemContainerTooltip(ItemContainerTooltip containerTooltip) {
        IItemHandler handler = containerTooltip.handler();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                this.items.add(stack);
            }
        }
        if (items.isEmpty()) {
            this.emptyTip = Component.translatable("tooltips.touhou_little_maid.item_container.empty");
        }
    }

    @Override
    public int getHeight() {
        if (emptyTip != null) {
            return 10;
        }
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        if (emptyTip != null) {
            return font.width(emptyTip);
        }
        return items.size() * 20;
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        if (emptyTip != null) {
            guiGraphics.drawString(font, emptyTip, pX, pY, ChatFormatting.GRAY.getColor());
        } else {
            int i = 0;
            for (ItemStack stack : this.items) {
                int xOffset = pX + i * 20;
                guiGraphics.renderFakeItem(stack, xOffset, pY);
                guiGraphics.renderItemDecorations(font, stack, xOffset, pY);
                i++;
            }
        }
    }
}

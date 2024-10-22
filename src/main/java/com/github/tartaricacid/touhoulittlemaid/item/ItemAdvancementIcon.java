package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemAdvancementIcon extends Item {
    public ItemAdvancementIcon() {
        super((new Properties()).stacksTo(1));
    }

    @Override
    public String getDescriptionId() {
        return "item.touhou_little_maid.advancement_icon";
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> components, TooltipFlag pIsAdvanced) {
        components.add(Component.translatable("tooltips.touhou_little_maid.advancement_icon.desc").withStyle(ChatFormatting.GRAY));
    }
}
package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChairShow extends Item {
    public ItemChairShow() {
        super((new Properties()).stacksTo(1));
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Item.TooltipContext pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        tooltip.add((Component.translatable("tooltips.touhou_little_maid.chair_show.desc")).withStyle(ChatFormatting.GRAY));
    }
}
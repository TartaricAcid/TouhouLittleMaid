package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemFavorabilityTool extends Item {
    private final String type;

    public ItemFavorabilityTool(String type) {
        super(new Item.Properties().tab(MAIN_TAB).stacksTo(1));
        this.type = type;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable World pLevel, List<ITextComponent> components, ITooltipFlag pIsAdvanced) {
        components.add(new TranslationTextComponent("tooltips.touhou_little_maid.favorability_tool." + this.type).withStyle(TextFormatting.GRAY));
    }
}

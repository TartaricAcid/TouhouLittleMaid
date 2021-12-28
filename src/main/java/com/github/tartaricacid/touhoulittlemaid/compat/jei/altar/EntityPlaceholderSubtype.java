package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EntityPlaceholderSubtype implements ISubtypeInterpreter {
    @Override
    public String apply(ItemStack itemStack) {
        ResourceLocation recipeId = ItemEntityPlaceholder.getRecipeId(itemStack);
        if (recipeId == null) {
            return ISubtypeInterpreter.NONE;
        }
        return recipeId.toString();
    }
}

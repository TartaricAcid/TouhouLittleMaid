package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class EntityPlaceholderSubtype implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public String apply(ItemStack itemStack, UidContext context) {
        ResourceLocation recipeId = ItemEntityPlaceholder.getRecipeId(itemStack);
        if (recipeId == null) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        return recipeId.toString();
    }
}

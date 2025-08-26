package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("removal")
public class EntityPlaceholderSubtype implements mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public String apply(ItemStack itemStack, UidContext context) {
        ResourceLocation recipeId = ItemEntityPlaceholder.getId(itemStack);
        if (recipeId == null) {
            return mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter.NONE;
        }
        return recipeId.toString();
    }
}

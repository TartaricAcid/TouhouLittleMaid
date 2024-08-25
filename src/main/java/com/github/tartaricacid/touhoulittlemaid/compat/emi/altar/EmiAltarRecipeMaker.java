package com.github.tartaricacid.touhoulittlemaid.compat.emi.altar;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.util.JERIUtil;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public final class EmiAltarRecipeMaker {
    public static void registerAltarRecipes(EmiRegistry registry) {
        List<AltarRecipe> allRecipesFor = registry.getRecipeManager().getAllRecipesFor(InitRecipes.ALTAR_CRAFTING.get()).stream().map(RecipeHolder::value).toList();
        JERIUtil.recipeWarp(allRecipesFor, (recipeId, inputs, output, powerCost, langKey) -> {
            List<EmiIngredient> inputs1 = inputs.stream()
                    .filter(it -> !it.isEmpty())
                    .map(EmiIngredient::of)
                    .toList();
            List<EmiStack> outputs = List.of(EmiStack.of(output));
            registry.addRecipe(new EmiAltarRecipe(recipeId, inputs1, outputs, powerCost, langKey));
        });
    }
}
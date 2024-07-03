package com.github.tartaricacid.touhoulittlemaid.compat.rei.altar;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.util.JERIUtil;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.Arrays;
import java.util.List;

public final class ReiAltarRecipeMaker {
    public static void registerAltarRecipes(DisplayRegistry registry) {
        List<AltarRecipe> allRecipesFor = registry.getRecipeManager().getAllRecipesFor(InitRecipes.ALTAR_CRAFTING);
        JERIUtil.recipeWarp(allRecipesFor, ((recipeId, inputs, output, powerCost, langKey) -> {
            List<EntryIngredient> inputs1 = inputs.stream()
                    .filter(it -> !it.isEmpty())
                    .map(ingredient -> {
                        return EntryIngredient.of(Arrays.stream(ingredient.getItems()).map(EntryStacks::of).toList());
                    })
                    .toList();

            List<EntryIngredient> outputs = List.of(EntryIngredient.of(EntryStacks.of(output)));
            registry.add(new ReiAltarRecipeDisplay(recipeId, inputs1, outputs, powerCost, langKey));
        }));
    }
}
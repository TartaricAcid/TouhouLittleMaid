package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.util.JERIUtil;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

public final class AltarRecipeMaker {
    private final RecipeManager recipeManager;

    private AltarRecipeMaker() {
        ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
        this.recipeManager = world.getRecipeManager();
    }

    public static AltarRecipeMaker getInstance() {
        return new AltarRecipeMaker();
    }

    public List<AltarRecipeWrapper> getAltarRecipes() {
        List<RecipeHolder<AltarRecipe>> altarRecipesMap = recipeManager.getAllRecipesFor(InitRecipes.ALTAR_CRAFTING.get());
        List<AltarRecipeWrapper> recipes = Lists.newArrayList();
        JERIUtil.recipeWarpHolder(altarRecipesMap, (recipeId, inputs, output, powerCost, langKey) -> {
            List<List<ItemStack>> inputs1 = inputs.stream()
                    .filter(ingredient -> !ingredient.isEmpty())
                    .map(ingredient -> List.of(ingredient.getItems()))
                    .toList();
            recipes.add(new AltarRecipeWrapper(inputs1, output, powerCost, langKey));
        });

        return recipes;
    }
}
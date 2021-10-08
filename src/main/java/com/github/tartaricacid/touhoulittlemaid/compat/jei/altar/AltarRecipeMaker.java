package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class AltarRecipeMaker {
    private final RecipeManager recipeManager;

    private AltarRecipeMaker() {
        ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().level);
        this.recipeManager = world.getRecipeManager();
    }

    public static AltarRecipeMaker getInstance() {
        return new AltarRecipeMaker();
    }

    public List<AltarRecipeWrapper> getAltarRecipes() {
        List<AltarRecipe> altarRecipesMap = recipeManager.getAllRecipesFor(InitRecipes.ALTAR_CRAFTING);
        List<AltarRecipeWrapper> recipes = Lists.newArrayList();
        for (AltarRecipe recipe : altarRecipesMap) {
            ResourceLocation recipeId = recipe.getId();
            ItemStack output = recipe.getResultItem();
            String namespace = recipeId.getNamespace().toLowerCase(Locale.US);
            String langKey;
            if (recipe.isItemCraft()) {
                langKey = String.format("jei.%s.altar_craft.%s.result", namespace, "item_craft");
            } else {
                Path path = Paths.get(recipeId.getPath().toLowerCase(Locale.US));
                langKey = String.format("jei.%s.altar_craft.%s.result", namespace, path.getFileName());
            }
            recipes.add(new AltarRecipeWrapper(recipe.getIngredients(), output, recipe.getPowerCost(), langKey));
        }
        return recipes;
    }
}

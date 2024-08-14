package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.dataGen.builder.AltarRecipeBuilder;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends RecipeProvider {
    public RecipeGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @SuppressWarnings("all")
    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.BOOKSHELF)
                .power(0.1F)
                .pattern("aaaabc")
                .define('a', ItemTags.PLANKS)
                .define('b', Items.BOOK)
                .define('c', Items.DIAMOND)
                .save(recipeOutput);
    }
}

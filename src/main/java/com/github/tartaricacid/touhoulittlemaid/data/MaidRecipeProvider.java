package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import vazkii.patchouli.common.item.PatchouliItems;

import java.util.function.Consumer;

public class MaidRecipeProvider extends RecipeProvider {
    public MaidRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(InitItems.HAKUREI_GOHEI.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('P', Items.PAPER)
                .pattern("  D")
                .pattern(" SP")
                .pattern("S P")
                .unlockedBy("gohei", has(Blocks.WATER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(InitItems.CHAIR.get())
                .define('W', ItemTags.WOOL)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', ItemTags.PLANKS)
                .pattern("   ")
                .pattern("WWW")
                .pattern("IPI")
                .unlockedBy("chair", has(Blocks.WATER))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(PatchouliItems.BOOK)
                .requires(Tags.Items.DYES_WHITE)
                .requires(Tags.Items.DYES_RED)
                .requires(Items.BOOK)
                .unlockedBy("book", has(Items.BOOK))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(InitItems.CHAIR_SHOW.get())
                .requires(InitItems.CHAIR.get())
                .requires(Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("chair", has(Blocks.WATER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(InitItems.CHAIR_SHOW.get())
                .define('W', ItemTags.WOOL)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', ItemTags.PLANKS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern(" R ")
                .pattern("WWW")
                .pattern("IPI")
                .unlockedBy("chair", has(Blocks.WATER))
                .save(consumer);
    }
}

package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import vazkii.patchouli.common.item.PatchouliItems;

import java.util.function.Consumer;

public class MaidRecipeProvider extends ForgeRecipeProvider {
    public MaidRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
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
        ShapelessRecipeBuilder.shapeless(PatchouliItems.book)
                .requires(Tags.Items.DYES_WHITE).requires(Tags.Items.DYES_RED).requires(Items.BOOK)
                .unlockedBy("book", has(Items.BOOK))
                .save(consumer);
    }
}

package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.dataGen.builder.AltarRecipeBuilder;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.common.item.PatchouliDataComponents;
import vazkii.patchouli.common.item.PatchouliItems;

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
                .define('c', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.BROOM)
                .power(0.2F)
                .pattern("aaabbc")
                .define('a', Items.HAY_BLOCK)
                .define('b', Tags.Items.RODS_WOODEN)
                .define('c', Items.ENDER_EYE)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.CAMERA)
                .power(0.2F)
                .pattern("aaaabb")
                .define('a', Items.QUARTZ_BLOCK)
                .define('b', Tags.Items.OBSIDIANS)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.CHISEL)
                .power(0.2F)
                .pattern("aabbcd")
                .define('a', Tags.Items.RODS_WOODEN)
                .define('b', Tags.Items.INGOTS_IRON)
                .define('c', Tags.Items.DYES_YELLOW)
                .define('d', Tags.Items.DYES_RED)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.COMPUTER)
                .power(0.1F)
                .pattern("aaabcd")
                .define('a', ItemTags.PLANKS)
                .define('b', Items.NOTE_BLOCK)
                .define('c', Items.LEVER)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.CRAFTING_TABLE_BACKPACK)
                .power(0.2F)
                .pattern("ab")
                .define('a', InitItems.MAID_BACKPACK_MIDDLE)
                .define('b', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.DROWN_PROTECT_BAUBLE)
                .power(0.2F)
                .pattern("abcccc")
                .define('a', Tags.Items.CROPS_NETHER_WART)
                .define('b', Tags.Items.DYES_LIME)
                .define('c', ItemTags.FISHES)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.ENDER_CHEST_BACKPACK)
                .power(0.2F)
                .pattern("ab")
                .define('a', InitItems.MAID_BACKPACK_MIDDLE)
                .define('b', Items.ENDER_CHEST)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.EXPLOSION_PROTECT_BAUBLE)
                .power(0.2F)
                .pattern("abcccc")
                .define('a', Tags.Items.CROPS_NETHER_WART)
                .define('b', Tags.Items.DYES_ORANGE)
                .define('c', Tags.Items.OBSIDIANS)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.EXTINGUISHER)
                .power(0.2F)
                .pattern("aaaabc")
                .define('a', Items.CLAY_BALL)
                .define('b', Tags.Items.DYES_ORANGE)
                .define('c', Tags.Items.DYES_RED)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.FALL_PROTECT_BAUBLE)
                .power(0.2F)
                .pattern("abcccc")
                .define('a', Tags.Items.CROPS_NETHER_WART)
                .define('b', Tags.Items.DYES_YELLOW)
                .define('c', Tags.Items.FEATHERS)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.FIRE_PROTECT_BAUBLE)
                .power(0.2F)
                .pattern("abcccc")
                .define('a', Tags.Items.CROPS_NETHER_WART)
                .define('b', Tags.Items.DYES_RED)
                .define('c', Items.BLAZE_POWDER)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.FURNACE_BACKPACK)
                .power(0.2F)
                .pattern("ab")
                .define('a', InitItems.MAID_BACKPACK_MIDDLE)
                .define('b', Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.GOMOKU)
                .power(0.1F)
                .pattern("aaabcd")
                .define('a', ItemTags.PLANKS)
                .define('b', Tags.Items.DYES_BLACK)
                .define('c', Tags.Items.DYES_WHITE)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.HAKUREI_GOHEI)
                .power(0.15F)
                .pattern("aaabbb")
                .define('a', Tags.Items.RODS_WOODEN)
                .define('b', Items.PAPER)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.ITEM_MAGNET_BAUBLE)
                .power(0.2F)
                .pattern("aaabbb")
                .define('a', Tags.Items.DUSTS_REDSTONE)
                .define('b', Tags.Items.INGOTS_IRON)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.KAPPA_COMPASS)
                .power(0.1F)
                .pattern("aaabcc")
                .define('a', Tags.Items.OBSIDIANS)
                .define('b', Tags.Items.DYES_CYAN)
                .define('c', Tags.Items.DUSTS_REDSTONE)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.KEYBOARD)
                .power(0.1F)
                .pattern("aaaabc")
                .define('a', ItemTags.PLANKS)
                .define('b', Items.NOTE_BLOCK)
                .define('c', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MAGIC_PROTECT_BAUBLE)
                .power(0.2F)
                .pattern("abcccc")
                .define('a', Tags.Items.CROPS_NETHER_WART)
                .define('b', Tags.Items.DYES_CYAN)
                .define('c', Items.SUGAR)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MAID_BACKPACK_BIG)
                .power(0.3F)
                .pattern("aaaaba")
                .define('a', Items.GRAY_WOOL)
                .define('b', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MAID_BACKPACK_MIDDLE)
                .power(0.2F)
                .pattern("aaaaba")
                .define('a', Items.PINK_WOOL)
                .define('b', Tags.Items.INGOTS_GOLD)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MAID_BACKPACK_SMALL)
                .power(0.1F)
                .pattern("aaaaba")
                .define('a', Items.RED_WOOL)
                .define('b', Tags.Items.INGOTS_IRON)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MAID_BEACON)
                .power(0.2F)
                .pattern("abacdc")
                .define('a', ItemTags.PLANKS)
                .define('b', Tags.Items.DYES_RED)
                .define('c', Tags.Items.OBSIDIANS)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MAID_BED)
                .power(0.2F)
                .pattern("ab")
                .define('a', Items.PINK_WOOL)
                .define('b', ItemTags.PLANKS)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.MUTE_BAUBLE)
                .power(0.2F)
                .pattern("ab")
                .define('a', ItemTags.WOOL)
                .define('b', Items.CLAY_BALL)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.NIMBLE_FABRIC)
                .power(0.2F)
                .pattern("ab")
                .define('a', Tags.Items.ENDER_PEARLS)
                .define('b', ItemTags.WOOL)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.PICNIC_BASKET)
                .power(0.2F)
                .pattern("abbbbc")
                .define('a', Tags.Items.CHESTS_WOODEN)
                .define('b', Items.BAMBOO)
                .define('c', ItemTags.WOOL_CARPETS)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.PROJECTILE_PROTECT_BAUBLE)
                .power(0.2F)
                .pattern("abcccc")
                .define('a', Tags.Items.CROPS_NETHER_WART)
                .define('b', Tags.Items.DYES_BLUE)
                .define('c', Items.SHIELD)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.RED_FOX_SCROLL)
                .power(0.1F)
                .pattern("aaaabc")
                .define('a', Items.PAPER)
                .define('b', Tags.Items.DYES_RED)
                .define('c', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.SANAE_GOHEI)
                .power(0.15F)
                .pattern("aaaabb")
                .define('a', Tags.Items.RODS_WOODEN)
                .define('b', Items.PAPER)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.TANK_BACKPACK)
                .power(0.2F)
                .pattern("ab")
                .define('a', InitItems.MAID_BACKPACK_MIDDLE)
                .define('b', Items.BUCKET)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.TRUMPET)
                .power(0.2F)
                .pattern("aabbbc")
                .define('a', Tags.Items.INGOTS_GOLD)
                .define('b', Tags.Items.INGOTS_IRON)
                .define('c', Items.NOTE_BLOCK)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.ULTRAMARINE_ORB_ELIXIR)
                .power(0.3F)
                .pattern("abcccc")
                .define('a', Tags.Items.GEMS_EMERALD)
                .define('b', Tags.Items.ENDER_PEARLS)
                .define('c', Tags.Items.DYES_CYAN)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.WHITE_FOX_SCROLL)
                .power(0.1F)
                .pattern("aaaabc")
                .define('a', Items.PAPER)
                .define('b', Tags.Items.DYES_WHITE)
                .define('c', Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.WIRELESS_IO)
                .power(0.2F)
                .pattern("abc")
                .define('a', Tags.Items.ENDER_PEARLS)
                .define('b', Tags.Items.CHESTS_WOODEN)
                .define('c', Items.HOPPER)
                .save(recipeOutput);

        ItemStack entityPlaceholder = new ItemStack(InitItems.ENTITY_PLACEHOLDER.asItem());
        entityPlaceholder.set(InitDataComponent.RECIPES_ID_TAG, "reborn_maid");
        AltarRecipeBuilder.shaped(RecipeCategory.MISC, entityPlaceholder)
                .power(0.5F)
                .pattern("abcdef")
                .define('a', InitItems.FILM)
                .define('b', Tags.Items.GEMS_LAPIS)
                .define('c', Tags.Items.INGOTS_GOLD)
                .define('d', Tags.Items.DUSTS_REDSTONE)
                .define('e', Tags.Items.INGOTS_IRON)
                .define('f', Items.COAL)
                .entity(EntityType.getKey(InitEntities.MAID.get()))
                .save(recipeOutput, "reborn_maid");

        entityPlaceholder.set(InitDataComponent.RECIPES_ID_TAG, "spawn_box");
        AltarRecipeBuilder.shaped(RecipeCategory.MISC, entityPlaceholder)
                .power(0.5F)
                .pattern("abcdef")
                .define('a', Tags.Items.GEMS_DIAMOND)
                .define('b', Tags.Items.GEMS_LAPIS)
                .define('c', Tags.Items.INGOTS_GOLD)
                .define('d', Tags.Items.DUSTS_REDSTONE)
                .define('e', Tags.Items.INGOTS_IRON)
                .define('f', Items.COAL)
                .entity(EntityType.getKey(InitEntities.BOX.get()))
                .save(recipeOutput, "spawn_box");

        entityPlaceholder.set(InitDataComponent.RECIPES_ID_TAG, "spawn_lightning_bolt");
        AltarRecipeBuilder.shaped(RecipeCategory.MISC, entityPlaceholder)
                .power(0.2F)
                .pattern("aaabbb")
                .define('a', Tags.Items.GUNPOWDERS)
                .define('b', Items.BLAZE_POWDER)
                .entity(EntityType.getKey(EntityType.LIGHTNING_BOLT))
                .save(recipeOutput, "spawn_lightning_bolt");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.HAKUREI_GOHEI)
                .pattern("  D")
                .pattern(" SP")
                .pattern("S P")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(Items.DIAMOND), has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.SANAE_GOHEI)
                .pattern(" PD")
                .pattern(" SP")
                .pattern("S  ")
                .define('S', Tags.Items.RODS_WOODEN)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(Items.DIAMOND), has(Tags.Items.GEMS_DIAMOND))
                .save(recipeOutput);

        ItemStack patchouliBook = new ItemStack(PatchouliItems.BOOK);
        patchouliBook.set(PatchouliDataComponents.BOOK, InitItems.MEMORIZABLE_GENSOKYO_LOCATION);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, patchouliBook)
                .requires(Tags.Items.DYES_WHITE)
                .requires(Tags.Items.DYES_RED)
                .requires(Items.BOOK)
                .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
                .save(recipeOutput, InitItems.MEMORIZABLE_GENSOKYO_LOCATION);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.CHAIR)
                .pattern("   ")
                .pattern("WWW")
                .pattern("IPI")
                .define('W', ItemTags.WOOL)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItems.CHAIR_SHOW)
                .pattern(" R ")
                .pattern("WWW")
                .pattern("IPI")
                .define('W', ItemTags.WOOL)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('P', ItemTags.PLANKS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
                .save(recipeOutput);
    }
}

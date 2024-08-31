package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.datagen.builder.AltarRecipeBuilder;
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
import vazkii.patchouli.common.item.PatchouliDataComponents;
import vazkii.patchouli.common.item.PatchouliItems;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends RecipeProvider {
    public RecipeGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @SuppressWarnings("all")
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.BOOKSHELF)
                .power(0.1F)
                .requires(4, ItemTags.PLANKS)
                .requires(Items.BOOK)
                .requires(Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.BROOM)
                .power(0.2F)
                .requires(3, Items.HAY_BLOCK)
                .requires(2, Tags.Items.RODS_WOODEN)
                .requires(Items.ENDER_EYE)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.CAMERA)
                .power(0.2F)
                .requires(4, Items.QUARTZ_BLOCK)
                .requires(2, Tags.Items.OBSIDIANS)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.CHISEL)
                .power(0.2F)
                .requires(2, Tags.Items.RODS_WOODEN)
                .requires(2, Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.DYES_YELLOW)
                .requires(Tags.Items.DYES_RED)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.COMPUTER)
                .power(0.1F)
                .requires(3, ItemTags.PLANKS)
                .requires(Items.NOTE_BLOCK)
                .requires(Items.LEVER)
                .requires(Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.CRAFTING_TABLE_BACKPACK)
                .power(0.2F)
                .requires(InitItems.MAID_BACKPACK_MIDDLE)
                .requires(Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.DROWN_PROTECT_BAUBLE)
                .power(0.2F)
                .requires(Tags.Items.CROPS_NETHER_WART)
                .requires(Tags.Items.DYES_LIME)
                .requires(4, ItemTags.FISHES)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.ENDER_CHEST_BACKPACK)
                .power(0.2F)
                .requires(InitItems.MAID_BACKPACK_MIDDLE)
                .requires(Items.ENDER_CHEST)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.EXPLOSION_PROTECT_BAUBLE)
                .power(0.2F)
                .requires(Tags.Items.CROPS_NETHER_WART)
                .requires(Tags.Items.DYES_ORANGE)
                .requires(4, Tags.Items.OBSIDIANS)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.EXTINGUISHER)
                .power(0.2F)
                .requires(4, Items.CLAY_BALL)
                .requires(Tags.Items.DYES_ORANGE)
                .requires(Tags.Items.DYES_RED)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.FALL_PROTECT_BAUBLE)
                .power(0.2F)
                .requires(Tags.Items.CROPS_NETHER_WART)
                .requires(Tags.Items.DYES_YELLOW)
                .requires(4, Tags.Items.FEATHERS)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.FIRE_PROTECT_BAUBLE)
                .power(0.2F)
                .requires(Tags.Items.CROPS_NETHER_WART)
                .requires(Tags.Items.DYES_RED)
                .requires(4, Items.BLAZE_POWDER)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.FURNACE_BACKPACK)
                .power(0.2F)
                .requires(InitItems.MAID_BACKPACK_MIDDLE)
                .requires(Tags.Items.PLAYER_WORKSTATIONS_FURNACES)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.GOMOKU)
                .power(0.1F)
                .requires(3, ItemTags.PLANKS)
                .requires(Tags.Items.DYES_BLACK)
                .requires(Tags.Items.DYES_WHITE)
                .requires(Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.HAKUREI_GOHEI)
                .power(0.15F)
                .requires(3, Tags.Items.RODS_WOODEN)
                .requires(3, Items.PAPER)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.ITEM_MAGNET_BAUBLE)
                .power(0.2F)
                .requires(3, Tags.Items.DUSTS_REDSTONE)
                .requires(3, Tags.Items.INGOTS_IRON)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.KAPPA_COMPASS)
                .power(0.1F)
                .requires(3, Tags.Items.OBSIDIANS)
                .requires(Tags.Items.DYES_CYAN)
                .requires(2, Tags.Items.DUSTS_REDSTONE)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.KEYBOARD)
                .power(0.1F)
                .requires(4, ItemTags.PLANKS)
                .requires(Items.NOTE_BLOCK)
                .requires(Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MAGIC_PROTECT_BAUBLE)
                .power(0.2F)
                .requires(Tags.Items.CROPS_NETHER_WART)
                .requires(Tags.Items.DYES_CYAN)
                .requires(4, Items.SUGAR)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MAID_BACKPACK_BIG)
                .power(0.3F)
                .requires(4, Items.GRAY_WOOL)
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Items.GRAY_WOOL)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MAID_BACKPACK_MIDDLE)
                .power(0.2F)
                .requires(4, Items.PINK_WOOL)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Items.PINK_WOOL)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MAID_BACKPACK_SMALL)
                .power(0.1F)
                .requires(4, Items.RED_WOOL)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Items.RED_WOOL)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MAID_BEACON)
                .power(0.2F)
                .requires(ItemTags.PLANKS)
                .requires(Tags.Items.DYES_RED)
                .requires(ItemTags.PLANKS)
                .requires(Tags.Items.OBSIDIANS)
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.OBSIDIANS)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MAID_BED)
                .power(0.2F)
                .requires(Items.PINK_WOOL)
                .requires(ItemTags.PLANKS)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.MUTE_BAUBLE)
                .power(0.2F)
                .requires(ItemTags.WOOL)
                .requires(Items.CLAY_BALL)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.NIMBLE_FABRIC)
                .power(0.2F)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(ItemTags.WOOL)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.PICNIC_BASKET)
                .power(0.2F)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(4, Items.BAMBOO)
                .requires(ItemTags.WOOL_CARPETS)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.PROJECTILE_PROTECT_BAUBLE)
                .power(0.2F)
                .requires(Tags.Items.CROPS_NETHER_WART)
                .requires(Tags.Items.DYES_BLUE)
                .requires(4, Items.SHIELD)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.RED_FOX_SCROLL)
                .power(0.1F)
                .requires(4, Items.PAPER)
                .requires(Tags.Items.DYES_RED)
                .requires(Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.SANAE_GOHEI)
                .power(0.15F)
                .requires(4, Tags.Items.RODS_WOODEN)
                .requires(2, Items.PAPER)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.SCARECROW)
                .power(0.2F)
                .requires(4, Items.HAY_BLOCK)
                .requires(2, Tags.Items.DUSTS_REDSTONE)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.TANK_BACKPACK)
                .power(0.2F)
                .requires(InitItems.MAID_BACKPACK_MIDDLE)
                .requires(Items.BUCKET)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.TRUMPET)
                .power(0.2F)
                .requires(2, Tags.Items.INGOTS_GOLD)
                .requires(3, Tags.Items.INGOTS_IRON)
                .requires(Items.NOTE_BLOCK)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.ULTRAMARINE_ORB_ELIXIR)
                .power(0.3F)
                .requires(Tags.Items.GEMS_EMERALD)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(4, Tags.Items.DYES_CYAN)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.WHITE_FOX_SCROLL)
                .power(0.1F)
                .requires(4, Items.PAPER)
                .requires(Tags.Items.DYES_WHITE)
                .requires(Tags.Items.GEMS_DIAMOND)
                .save(recipeOutput);

        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, InitItems.WIRELESS_IO)
                .power(0.2F)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(Items.HOPPER)
                .save(recipeOutput);

        ItemStack entityPlaceholder = new ItemStack(InitItems.ENTITY_PLACEHOLDER.asItem());
        entityPlaceholder.set(InitDataComponent.RECIPES_ID_TAG, "reborn_maid");
        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, entityPlaceholder)
                .power(0.5F)
                .requires(InitItems.FILM)
                .requires(Tags.Items.GEMS_LAPIS)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Items.COAL)
                .entity(EntityType.getKey(InitEntities.MAID.get()))
                .langKey("jei.touhou_little_maid.altar_craft.reborn_maid.result")
                .save(recipeOutput, "reborn_maid");

        entityPlaceholder.set(InitDataComponent.RECIPES_ID_TAG, "spawn_box");
        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, entityPlaceholder)
                .power(0.5F)
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.GEMS_LAPIS)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(Items.COAL)
                .entity(EntityType.getKey(InitEntities.BOX.get()))
                .langKey("jei.touhou_little_maid.altar_craft.spawn_box.result")
                .save(recipeOutput, "spawn_box");

        entityPlaceholder.set(InitDataComponent.RECIPES_ID_TAG, "spawn_lightning_bolt");
        AltarRecipeBuilder.shapeless(RecipeCategory.MISC, entityPlaceholder)
                .power(0.2F)
                .requires(3, Tags.Items.GUNPOWDERS)
                .requires(3, Items.BLAZE_POWDER)
                .entity(EntityType.getKey(EntityType.LIGHTNING_BOLT))
                .langKey("jei.touhou_little_maid.altar_craft.spawn_lightning_bolt.result")
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

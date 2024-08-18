package com.github.tartaricacid.touhoulittlemaid.dataGen.builder;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class AltarRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final ItemStack resultStack; // Neo: add stack result support
    private float power;
    private ResourceLocation entityType;

    public AltarRecipeBuilder(RecipeCategory recipeCategory, ItemLike resultStack, int count) {
        this(recipeCategory, new ItemStack(resultStack, count));
    }

    public AltarRecipeBuilder(RecipeCategory recipeCategory, ItemStack result) {
        this.category = recipeCategory;
        this.power = 0;
        this.result = result.getItem();
        this.resultStack = result;
        this.entityType = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM);
        this.rows = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
    }

    public static AltarRecipeBuilder shaped(RecipeCategory pCategory, ItemStack pResult) {
        return new AltarRecipeBuilder(pCategory, pResult);
    }

    public static AltarRecipeBuilder shaped(RecipeCategory pCategory, ItemLike pResult) {
        return shaped(pCategory, pResult, 1);
    }

    public static AltarRecipeBuilder shaped(RecipeCategory pCategory, ItemLike pResult, int pCount) {
        return new AltarRecipeBuilder(pCategory, pResult, pCount);
    }

    public AltarRecipeBuilder power(float power) {
        this.power = power;
        return this;
    }

    public AltarRecipeBuilder entity(ResourceLocation entityType) {
        this.entityType = entityType;
        return this;
    }

    public AltarRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
        return this.define(pSymbol, Ingredient.of(pTag));
    }

    public AltarRecipeBuilder define(Character pSymbol, ItemLike pItem) {
        return this.define(pSymbol, Ingredient.of(pItem));
    }

    public AltarRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
        if (this.key.containsKey(pSymbol)) {
            throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
        } else if (pSymbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(pSymbol, pIngredient);
            return this;
        }
    }

    public AltarRecipeBuilder pattern(String pPattern) {
        if (!this.rows.isEmpty() && pPattern.length() != this.rows.getFirst().length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pPattern);
            return this;
        }
    }

    public AltarRecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        return this;
    }

    public AltarRecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(RecipeOutput output) {
        this.save(output, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar_recipe/" + RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath()));
    }

    public void save(RecipeOutput output, String recipeId) {
        this.save(output, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "altar_recipe/" + recipeId));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation pId) {
        ShapedRecipePattern.setCraftingSize(6, 1);
        AltarRecipe altarRecipe = new AltarRecipe(
                "alter_recipe",
                RecipeBuilder.determineBookCategory(this.category),
                this.power,
                ShapedRecipePattern.of(this.key, this.rows),
                this.resultStack,
                this.entityType
        );
        recipeOutput.accept(pId, altarRecipe, null);
    }
}

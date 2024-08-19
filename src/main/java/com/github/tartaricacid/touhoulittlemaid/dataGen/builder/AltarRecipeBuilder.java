package com.github.tartaricacid.touhoulittlemaid.dataGen.builder;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;

public class AltarRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final NonNullList<Ingredient> ingredients;
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
        this.ingredients = NonNullList.create();
    }

    public static AltarRecipeBuilder shapeless(RecipeCategory pCategory, ItemStack pResult) {
        return new AltarRecipeBuilder(pCategory, pResult);
    }

    public static AltarRecipeBuilder shapeless(RecipeCategory pCategory, ItemLike pResult) {
        return shapeless(pCategory, pResult, 1);
    }

    public static AltarRecipeBuilder shapeless(RecipeCategory pCategory, ItemLike pResult, int pCount) {
        return new AltarRecipeBuilder(pCategory, pResult, pCount);
    }

    public AltarRecipeBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public AltarRecipeBuilder requires(int pCount, TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag), pCount);
    }

    public AltarRecipeBuilder requires(ItemLike pItem) {
        return this.requires(1, pItem);
    }

    public AltarRecipeBuilder requires(int pQuantity, ItemLike pItem) {
        for (int i = 0; i < pQuantity; ++i) {
            this.requires(Ingredient.of(pItem));
        }

        return this;
    }

    public AltarRecipeBuilder requires(Ingredient pIngredient) {
        return this.requires(pIngredient, 1);
    }

    public AltarRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.ingredients.add(pIngredient);
        }

        return this;
    }


    public AltarRecipeBuilder power(float power) {
        this.power = power;
        return this;
    }

    public AltarRecipeBuilder entity(ResourceLocation entityType) {
        this.entityType = entityType;
        return this;
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
        AltarRecipe altarRecipe = new AltarRecipe(
                "alter_recipe",
                RecipeBuilder.determineBookCategory(this.category),
                this.ingredients,
                this.power,
                this.resultStack,
                this.entityType
        );
        recipeOutput.accept(pId, altarRecipe, null);
    }
}

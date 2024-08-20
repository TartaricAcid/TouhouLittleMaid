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
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;

public class AltarRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "altar_recipe";
    private final RecipeCategory category;
    private final Item result;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack resultStack;
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

    public static AltarRecipeBuilder shapeless(RecipeCategory category, ItemStack result) {
        return new AltarRecipeBuilder(category, result);
    }

    public static AltarRecipeBuilder shapeless(RecipeCategory category, ItemLike result) {
        return shapeless(category, result, 1);
    }

    public static AltarRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count) {
        return new AltarRecipeBuilder(category, result, count);
    }

    public AltarRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(tag));
    }

    public AltarRecipeBuilder requires(int count, TagKey<Item> tag) {
        return this.requires(Ingredient.of(tag), count);
    }

    public AltarRecipeBuilder requires(ItemLike item) {
        return this.requires(1, item);
    }

    public AltarRecipeBuilder requires(int quantity, ItemLike item) {
        for (int i = 0; i < quantity; ++i) {
            this.requires(Ingredient.of(item));
        }
        return this;
    }

    public AltarRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public AltarRecipeBuilder requires(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; ++i) {
            this.ingredients.add(ingredient);
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

    @Override
    public AltarRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public AltarRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput output) {
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, NAME + "/" + path);
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        ResourceLocation filePath = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, NAME + "/" + recipeId);
        this.save(output, filePath);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        CraftingBookCategory bookCategory = RecipeBuilder.determineBookCategory(this.category);
        AltarRecipe altarRecipe = new AltarRecipe(NAME, bookCategory, this.ingredients, this.power, this.resultStack, this.entityType);
        recipeOutput.accept(id, altarRecipe, null);
    }
}

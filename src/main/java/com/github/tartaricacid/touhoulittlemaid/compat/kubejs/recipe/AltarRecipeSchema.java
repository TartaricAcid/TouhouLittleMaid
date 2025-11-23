package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.CraftingBookCategory;

public interface AltarRecipeSchema {
    RecipeKey<?> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<?> INGREDIENTS = ListRecipeComponent.create(IngredientComponent.INGREDIENT.instance(), true, false).inputKey("ingredients");
    RecipeKey<?> ENTITY = StringComponent.ID.outputKey("entity").optional("minecraft:item").alwaysWrite();

    RecipeKey<?> GROUP = StringComponent.STRING.otherKey("group").optional("altar_recipe").alwaysWrite();
    RecipeKey<?> CATEGORY = BookCategoryComponent.CRAFTING_BOOK_CATEGORY.otherKey("category").optional(CraftingBookCategory.MISC).alwaysWrite();
    RecipeKey<?> POWER = NumberComponent.FLOAT.otherKey("power").optional(0.2f).alwaysWrite();
    RecipeKey<?> LANG = StringComponent.STRING.otherKey("lang").optional("jei.touhou_little_maid.altar_craft.item_craft.result").alwaysWrite();

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, POWER, ENTITY, LANG, GROUP, CATEGORY);
}

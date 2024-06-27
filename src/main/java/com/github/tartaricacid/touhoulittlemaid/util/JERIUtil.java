package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemEntityPlaceholder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public final class JERIUtil {

    public static void recipeWarp(List<AltarRecipe> altarRecipes, AltarRecipeMaker maker) {
        for (AltarRecipe altarRecipe : altarRecipes) {
            ResourceLocation recipeId = altarRecipe.getId();
            ItemStack output = altarRecipe.getResultItem(Minecraft.getInstance().level.registryAccess());
            if (!altarRecipe.isItemCraft()) {
                output = InitItems.ENTITY_PLACEHOLDER.get().getDefaultInstance();
                ItemEntityPlaceholder.setRecipeId(output, altarRecipe.getId());
            }
            String namespace = recipeId.getNamespace().toLowerCase(Locale.US);
            String langKey;
            if (altarRecipe.isItemCraft()) {
                langKey = String.format("jei.%s.altar_craft.%s.result", namespace, "item_craft");
            } else {
                Path path = Paths.get(recipeId.getPath().toLowerCase(Locale.US));
                langKey = String.format("jei.%s.altar_craft.%s.result", namespace, path.getFileName());
            }

            maker.accept(recipeId, altarRecipe.getIngredients(), output, altarRecipe.getPowerCost(), langKey);
        }
    }

    public interface AltarRecipeMaker {
        void accept(ResourceLocation recipeId, NonNullList<Ingredient> inputs, ItemStack output, float powerCost, String langKey);

    }

}
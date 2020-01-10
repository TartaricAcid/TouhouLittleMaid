package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipesManager;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author TartaricAcid
 * @date 2019/9/12 21:25
 **/
public final class AltarRecipeMaker {
    private AltarRecipeMaker() {
    }

    @SuppressWarnings("unchecked")
    public static List<AltarRecipeWrapper> getAltarRecipes() {
        Map<ResourceLocation, AltarRecipe> altarRecipesMap = AltarRecipesManager.instance().getAltarRecipesMap();
        List<AltarRecipeWrapper> recipes = Lists.newArrayList();
        for (ResourceLocation id : altarRecipesMap.keySet()) {
            AltarRecipe altarRecipe = altarRecipesMap.get(id);
            String key;
            if (altarRecipe.isItemCraft()) {
                key = String.format("jei.%s.altar_craft.%s.result", id.getNamespace().toLowerCase(Locale.US),
                        "item_craft");
            } else {
                key = String.format("jei.%s.altar_craft.%s.result", id.getNamespace().toLowerCase(Locale.US),
                        id.getPath().toLowerCase(Locale.US));
            }
            ItemStack output = altarRecipe.getOutputItemStack();
            recipes.add(new AltarRecipeWrapper(altarRecipe.getRecipe(), Collections.singletonList(output), altarRecipe.getPowerCost(), key, id.toString()));
        }
        return recipes;
    }
}

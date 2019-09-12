package com.github.tartaricacid.touhoulittlemaid.compat.jei;

import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.AltarRecipeCategory;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.AltarRecipeMaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

/**
 * @author TartaricAcid
 * @date 2019/9/8 22:21
 **/
@JEIPlugin
public class JEIModPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new AltarRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(AltarRecipeMaker.getAltarRecipes(), AltarRecipeCategory.ALTAR_CRAFTING);
    }
}

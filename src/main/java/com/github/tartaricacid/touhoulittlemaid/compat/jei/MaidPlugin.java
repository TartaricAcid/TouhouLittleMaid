package com.github.tartaricacid.touhoulittlemaid.compat.jei;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.AltarRecipeCategory;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.AltarRecipeMaker;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.EntityPlaceholderSubtype;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class MaidPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "jei");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(AltarRecipeCategory.ALTAR, AltarRecipeMaker.getInstance().getAltarRecipes());
        registration.addIngredientInfo(InitItems.GARAGE_KIT.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Component.translatable("jei.touhou_little_maid.garage_kit.info"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(InitItems.HAKUREI_GOHEI.get().getDefaultInstance(), AltarRecipeCategory.ALTAR);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, InitItems.ENTITY_PLACEHOLDER.get(), new EntityPlaceholderSubtype());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}

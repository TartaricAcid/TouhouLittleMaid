package com.github.tartaricacid.touhoulittlemaid.compat.jei;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.CraftingTableBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.FurnaceBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.AltarRecipeCategory;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.AltarRecipeMaker;
import com.github.tartaricacid.touhoulittlemaid.compat.jei.altar.EntityPlaceholderSubtype;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.CraftingTableBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.FurnaceBackpackContainer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.*;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

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
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CraftingTableBackpackContainer.class, CraftingTableBackpackContainer.TYPE, RecipeTypes.CRAFTING, 71, 9, 0, 70);
        registration.addRecipeTransferHandler(FurnaceBackpackContainer.class, FurnaceBackpackContainer.TYPE, RecipeTypes.SMELTING, 70, 1, 0, 70);
        registration.addRecipeTransferHandler(FurnaceBackpackContainer.class, FurnaceBackpackContainer.TYPE, RecipeTypes.FUELING, 71, 1, 0, 70);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CraftingTableBackpackContainerScreen.class, 213, 121, 13, 12, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(FurnaceBackpackContainerScreen.class, 183, 118, 28, 24, RecipeTypes.SMELTING, RecipeTypes.FUELING);
        registerTaskListArea(registration);
    }

    private void registerTaskListArea(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractMaidContainerGui.class, new IGuiContainerHandler<AbstractMaidContainerGui<?>>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(AbstractMaidContainerGui containerScreen) {
                if (containerScreen.isTaskListOpen()) {
                    int[] taskListAreas = containerScreen.getTaskListAreas();
                    return Collections.singletonList(new Rect2i(taskListAreas[0], taskListAreas[1], taskListAreas[2], taskListAreas[3]));
                }
                return Collections.emptyList();
            }
        });
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}

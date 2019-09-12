package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/9/8 22:24
 **/
public class AltarRecipeCategory implements IRecipeCategory<AltarRecipeWrapper> {
    public static final String ALTAR_CRAFTING = String.format("%s.altar", TouhouLittleMaid.MOD_ID);
    private final IDrawableStatic background;
    private final IDrawable slotDrawable;

    public AltarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(160, 125);
        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Nonnull
    @Override
    public String getUid() {
        return ALTAR_CRAFTING;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("jei.touhou_little_maid.altar_craft.title");
    }

    @Nonnull
    @Override
    public String getModName() {
        return TouhouLittleMaid.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull AltarRecipeWrapper altarRecipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 40, 35);
        guiItemStacks.init(1, true, 40, 55);
        guiItemStacks.init(2, true, 60, 15);
        guiItemStacks.init(3, true, 80, 15);
        guiItemStacks.init(4, true, 100, 35);
        guiItemStacks.init(5, true, 100, 55);
        guiItemStacks.init(6, false, 140, 5);
        guiItemStacks.setBackground(0, slotDrawable);
        guiItemStacks.setBackground(1, slotDrawable);
        guiItemStacks.setBackground(2, slotDrawable);
        guiItemStacks.setBackground(3, slotDrawable);
        guiItemStacks.setBackground(4, slotDrawable);
        guiItemStacks.setBackground(5, slotDrawable);
        guiItemStacks.setBackground(6, slotDrawable);
        guiItemStacks.set(ingredients);
    }
}

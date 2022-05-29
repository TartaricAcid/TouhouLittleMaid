package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class AltarRecipeCategory implements IRecipeCategory<AltarRecipeWrapper> {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar");
    private static final ResourceLocation ALTAR_ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/altar_icon.png");
    private static final ResourceLocation POWER_ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");
    private static final TranslatableComponent TITLE = new TranslatableComponent("jei.touhou_little_maid.altar_craft.title");
    private final IDrawableStatic bgDraw;
    private final IDrawable slotDraw;
    private final IDrawableStatic altarDraw;
    private final IDrawableStatic powerDraw;

    public AltarRecipeCategory(IGuiHelper guiHelper) {
        this.bgDraw = guiHelper.createBlankDrawable(160, 125);
        this.slotDraw = guiHelper.getSlotDrawable();
        this.altarDraw = guiHelper.drawableBuilder(ALTAR_ICON, 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.powerDraw = guiHelper.drawableBuilder(POWER_ICON, 32, 0, 16, 16).setTextureSize(64, 64).build();
    }

    @Override
    public void setIngredients(AltarRecipeWrapper recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM_STACK, recipe.getInputs());
        ingredients.setOutput(VanillaTypes.ITEM_STACK, recipe.getOutput());
    }

    @Override
    public void draw(AltarRecipeWrapper recipe, PoseStack poseStack, double mouseX, double mouseY) {
        int darkGray = 0x555555;
        Font font = Minecraft.getInstance().font;
        String result = I18n.get("jei.touhou_little_maid.altar_craft.result", I18n.get(recipe.getLangKey()));

        poseStack.pushPose();
        poseStack.scale(0.8f, 0.8f, 0.8f);
        powerDraw.draw(poseStack, 78, 59);
        poseStack.popPose();

        font.draw(poseStack, String.format("×%.2f", recipe.getPowerCost()), 76, 49, darkGray);
        font.draw(poseStack, result, (bgDraw.getWidth() - font.width(result)) / 2.0f, 85, darkGray);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AltarRecipeWrapper recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 40, 35);
        guiItemStacks.init(1, true, 40, 55);
        guiItemStacks.init(2, true, 60, 15);
        guiItemStacks.init(3, true, 80, 15);
        guiItemStacks.init(4, true, 100, 35);
        guiItemStacks.init(5, true, 100, 55);
        guiItemStacks.init(6, false, 140, 5);
        guiItemStacks.setBackground(0, slotDraw);
        guiItemStacks.setBackground(1, slotDraw);
        guiItemStacks.setBackground(2, slotDraw);
        guiItemStacks.setBackground(3, slotDraw);
        guiItemStacks.setBackground(4, slotDraw);
        guiItemStacks.setBackground(5, slotDraw);
        guiItemStacks.setBackground(6, slotDraw);
        guiItemStacks.set(ingredients);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends AltarRecipeWrapper> getRecipeClass() {
        return AltarRecipeWrapper.class;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public IDrawable getBackground() {
        return bgDraw;
    }

    @Override
    public IDrawable getIcon() {
        return altarDraw;
    }
}

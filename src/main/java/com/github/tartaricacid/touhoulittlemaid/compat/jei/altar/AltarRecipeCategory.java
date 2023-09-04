package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class AltarRecipeCategory implements IRecipeCategory<AltarRecipeWrapper> {
    public static final RecipeType<AltarRecipeWrapper> ALTAR = RecipeType.create(TouhouLittleMaid.MOD_ID, "altar", AltarRecipeWrapper.class);
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
    public void draw(AltarRecipeWrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        int darkGray = 0x555555;
        Font font = Minecraft.getInstance().font;
        String result = I18n.get("jei.touhou_little_maid.altar_craft.result", I18n.get(recipe.getLangKey()));

        poseStack.pushPose();
        poseStack.scale(0.8f, 0.8f, 0.8f);
        powerDraw.draw(poseStack, 90, 50);
        poseStack.popPose();

        font.draw(poseStack, String.format("×%.2f", recipe.getPowerCost()), 65, 55, darkGray);
        font.draw(poseStack, result, (bgDraw.getWidth() - font.width(result)) / 2.0f, 85, darkGray);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AltarRecipeWrapper recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = recipe.getInputs();
        ItemStack output = recipe.getOutput();
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 35).addItemStacks(getInput(inputs, 0)).setBackground(slotDraw, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 55).addItemStacks(getInput(inputs, 1)).setBackground(slotDraw, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 60, 15).addItemStacks(getInput(inputs, 2)).setBackground(slotDraw, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 15).addItemStacks(getInput(inputs, 3)).setBackground(slotDraw, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 100, 35).addItemStacks(getInput(inputs, 4)).setBackground(slotDraw, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 100, 55).addItemStacks(getInput(inputs, 5)).setBackground(slotDraw, -1, -1);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 5).addItemStack(output).setBackground(slotDraw, -1, -1);
    }

    private List<ItemStack> getInput(List<List<ItemStack>> inputs, int index) {
        if (index < inputs.size()) {
            return inputs.get(index);
        }
        return Collections.singletonList(ItemStack.EMPTY);
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return ALTAR.getUid();
    }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends AltarRecipeWrapper> getRecipeClass() {
        return ALTAR.getRecipeClass();
    }

    @Override
    public RecipeType<AltarRecipeWrapper> getRecipeType() {
        return ALTAR;
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

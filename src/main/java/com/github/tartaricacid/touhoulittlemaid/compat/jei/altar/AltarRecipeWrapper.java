package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/9/12 19:18
 **/
public class AltarRecipeWrapper implements IRecipeWrapper {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");
    private final List<List<ItemStack>> inputs;
    private final List<ItemStack> outputs;
    private final float powerCost;
    private final String resultLanguageKey;
    private final String recipeId;

    AltarRecipeWrapper(List<ProcessingInput> inputs, List<ItemStack> outputs, float powerCost, String resultLanguageKey, String recipeId) {
        this.inputs = new ArrayList<>();
        this.outputs = outputs;
        for (ProcessingInput input : inputs) {
            this.inputs.add(input.examples());
        }
        this.powerCost = powerCost;
        this.resultLanguageKey = resultLanguageKey;
        this.recipeId = recipeId;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, outputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.scale(0.8, 0.8, 0.8f);
        Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
        Gui.drawModalRectWithCustomSizedTexture(78, 59, 32, 0, 16, 16, 64, 64);
        GlStateManager.popMatrix();

        FontRenderer fontRenderer = minecraft.fontRenderer;
        String result = I18n.format("jei.touhou_little_maid.altar_craft.result", I18n.format(resultLanguageKey));
        fontRenderer.drawString(String.format("×%.2f", powerCost), 76, 49, Color.gray.getRGB());
        fontRenderer.drawString(result, (recipeWidth - fontRenderer.getStringWidth(result)) / 2, 85, Color.gray.getRGB());
        fontRenderer.drawString(recipeId, (recipeWidth - fontRenderer.getStringWidth(recipeId)) / 2, 96, Color.gray.getRGB());
    }
}

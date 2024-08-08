package com.github.tartaricacid.touhoulittlemaid.compat.emi.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.compat.emi.MaidEmiPlugin;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmiAltarRecipe implements EmiRecipe {
    private static final EmiTexture POWER_ICON = new EmiTexture(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png"), 32, 0, 16, 16, 16, 16, 64, 64);
    private final ResourceLocation id;
    private final List<EmiStack> outputs;
    private final List<EmiIngredient> inputs;
    private final float powerCost;
    private final String langKey;

    public EmiAltarRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, float powerCost, String langKey) {
        this.id = id;
        this.inputs = inputs;
        this.outputs = outputs;
        this.powerCost = powerCost;
        this.langKey = langKey;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return MaidEmiPlugin.ALTAR;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 125;
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int darkGray = 0x555555;
        Font font = Minecraft.getInstance().font;
        String result = I18n.get("jei.touhou_little_maid.altar_craft.result", I18n.get(this.langKey));

        widgets.addSlot(getInput(inputs, 0), 40, 35);
        widgets.addSlot(getInput(inputs, 1), 40, 55);
        widgets.addSlot(getInput(inputs, 2), 60, 15);
        widgets.addSlot(getInput(inputs, 3), 80, 15);
        widgets.addSlot(getInput(inputs, 4), 100, 35);
        widgets.addSlot(getInput(inputs, 5), 100, 55);
        widgets.addSlot(outputs.get(0), 140, 5);

        widgets.addTexture(POWER_ICON, 72, 38);
        widgets.addText(Component.literal(result), (int) ((widgets.getWidth() - font.width(result)) / 2.0f), 85, darkGray, false);
        widgets.addText(Component.literal(String.format("×%.2f", powerCost)), 65, 55, darkGray, false);
    }

    private EmiIngredient getInput(List<EmiIngredient> inputs, int index) {
        if (index < inputs.size()) {
            return inputs.get(index);
        }
        return EmiStack.EMPTY;
    }
}
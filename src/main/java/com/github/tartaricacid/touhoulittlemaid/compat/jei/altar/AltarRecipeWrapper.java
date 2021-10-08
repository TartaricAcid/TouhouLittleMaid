package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class AltarRecipeWrapper {
    public static final ItemStack ENTITY_PLACEHOLDER = InitItems.ENTITY_PLACEHOLDER.get().getDefaultInstance();
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final float powerCost;
    private final String langKey;

    AltarRecipeWrapper(List<Ingredient> inputs, ItemStack output, float powerCost, String langKey) {
        this.inputs = Lists.newArrayList();
        this.output = output.isEmpty() ? ENTITY_PLACEHOLDER : output;
        for (Ingredient input : inputs) {
            if (!input.isEmpty()) {
                this.inputs.add(Lists.newArrayList(input.getItems()));
            }
        }
        this.powerCost = powerCost;
        this.langKey = langKey;
    }

    public List<List<ItemStack>> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public float getPowerCost() {
        return powerCost;
    }

    public String getLangKey() {
        return langKey;
    }
}

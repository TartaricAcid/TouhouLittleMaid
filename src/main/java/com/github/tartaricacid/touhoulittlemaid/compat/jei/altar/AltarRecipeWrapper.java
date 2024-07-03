package com.github.tartaricacid.touhoulittlemaid.compat.jei.altar;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AltarRecipeWrapper {
    private final List<List<ItemStack>> inputs;
    private final ItemStack output;
    private final float powerCost;
    private final String langKey;

    AltarRecipeWrapper(List<List<ItemStack>> inputs, ItemStack output, float powerCost, String langKey) {
        this.inputs = inputs;
        this.output = output;
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
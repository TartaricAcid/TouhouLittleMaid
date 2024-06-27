package com.github.tartaricacid.touhoulittlemaid.compat.rei.altar;

import com.github.tartaricacid.touhoulittlemaid.compat.rei.MaidREIClientPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class ReiAltarRecipeDisplay implements Display {
    private final ResourceLocation id;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final float powerCost;
    private final String langKey;

    public ReiAltarRecipeDisplay(ResourceLocation id, List<EntryIngredient> inputs, List<EntryIngredient> outputs, float powerCost, String langKey) {
        this.id = id;
        this.inputs = inputs;
        this.outputs = outputs;
        this.powerCost = powerCost;
        this.langKey = langKey;
    }

    /**
     * Returns the list of inputs for this display. This only affects the stacks resolving for the display,
     * and not necessarily the stacks that are displayed.
     *
     * @return a list of inputs
     */
    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    /**
     * Returns the list of outputs for this display. This only affects the stacks resolving for the display,
     * and not necessarily the stacks that are displayed.
     *
     * @return a list of outputs
     */
    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    public ResourceLocation getId() {
        return id;
    }

    public float getPowerCost() {
        return powerCost;
    }

    public String getLangKey() {
        return langKey;
    }

    /**
     * Returns the identifier of the category this display belongs to.
     *
     * @return the identifier of the category
     */
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MaidREIClientPlugin.ALTAR;
    }

    /**
     * Returns the display location from data packs.
     *
     * @return the display location
     */
    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return Optional.of(id);
    }
}
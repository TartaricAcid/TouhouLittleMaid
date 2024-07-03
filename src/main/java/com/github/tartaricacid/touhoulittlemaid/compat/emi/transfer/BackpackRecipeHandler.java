package com.github.tartaricacid.touhoulittlemaid.compat.emi.transfer;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class BackpackRecipeHandler<C extends MaidMainContainer> implements StandardRecipeHandler<C> {
    private final EmiRecipeCategory recipeCategory;
    private final int recipeSlotStart;
    private final int recipeSlotCount;
    private final int inventorySlotStart;
    private final int inventorySlotCount;

    public BackpackRecipeHandler(EmiRecipeCategory categoryIdentifier, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
        this.recipeCategory = categoryIdentifier;
        this.recipeSlotStart = recipeSlotStart;
        this.recipeSlotCount = recipeSlotCount;
        this.inventorySlotStart = inventorySlotStart;
        this.inventorySlotCount = inventorySlotCount;
    }

    @Override
    public List<Slot> getInputSources(C handler) {
        List<Slot> slots = new ArrayList<>();
        for (int i = inventorySlotStart; i < inventorySlotStart + inventorySlotCount; i++) {
            slots.add(handler.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getCraftingSlots(C handler) {
        List<Slot> slots = new ArrayList<>();
        for (int i = recipeSlotStart; i < recipeSlotStart + recipeSlotCount; i++) {
            slots.add(handler.getSlot(i));
        }
        return slots;
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == recipeCategory;
    }
}
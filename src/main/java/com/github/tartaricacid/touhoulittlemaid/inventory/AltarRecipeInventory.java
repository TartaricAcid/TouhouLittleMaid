package com.github.tartaricacid.touhoulittlemaid.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

public class AltarRecipeInventory implements Container, RecipeInput {
    public static final int RECIPES_SIZE = 6;
    public final NonNullList<ItemStack> items = NonNullList.withSize(RECIPES_SIZE, ItemStack.EMPTY);

    public AltarRecipeInventory() {
    }

    public ItemStack getMatchIngredient(Ingredient ingredient) {
        return items.stream().filter(ingredient).findFirst().orElse(ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return RECIPES_SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.getContainerSize() <= index ? ItemStack.EMPTY : this.items.get(index);
    }

    @Override
    public int size() {
        return 6;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}

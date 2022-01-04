package com.github.tartaricacid.touhoulittlemaid.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class AltarRecipeInventory implements IInventory {
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
    public ItemStack removeItem(int index, int count) {
        return ItemStackHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.items.set(index, stack);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
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

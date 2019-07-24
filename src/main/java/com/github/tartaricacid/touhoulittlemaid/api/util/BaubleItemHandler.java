package com.github.tartaricacid.touhoulittlemaid.api.util;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * @author Snownee
 * @date 2019/7/23 14:53
 */
public class BaubleItemHandler extends ItemStackHandler {
    private IMaidBauble[] baubles;

    public BaubleItemHandler() {
        this(1);
    }

    public BaubleItemHandler(int size) {
        super(size);
        baubles = new IMaidBauble[size];
    }

    public BaubleItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
        baubles = new IMaidBauble[stacks.size()];
        IntStream.range(0, getSlots()).forEach(this::onContentsChanged);
    }

    @Override
    public void setSize(int size) {
        if (size == stacks.size()) {
            Arrays.fill(baubles, null);
        }
        else {
            baubles = new IMaidBauble[stacks.size()];
        }
        super.setSize(size);
    }

    private void setBaubleInSlot(int slot, IMaidBauble bauble) {
        validateSlotIndex(slot);
        baubles[slot] = bauble;
    }

    @Nullable
    public IMaidBauble getBaubleInSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) {
            return null;
        }
        else {
            return baubles[slot];
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) {
            setBaubleInSlot(slot, null);
        }
        else {
            setBaubleInSlot(slot, LittleMaidAPI.getBauble(stack));
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return LittleMaidAPI.getBauble(stack) != null;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (isItemValid(slot, stack)) {
            return super.insertItem(slot, stack, simulate);
        }
        else {
            return stack;
        }
    }

    public boolean fireEvent(BiFunction<IMaidBauble, ItemStack, Boolean> function) {
        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            IMaidBauble bauble = getBaubleInSlot(i);
            if (!stack.isEmpty() && bauble != null && function.apply(bauble, stack)) {
                return true;
            }
        }
        return false;
    }
}

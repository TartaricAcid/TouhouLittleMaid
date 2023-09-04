package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class BaubleItemHandler extends ItemStackHandler {
    /**
     * 存储 IMaidBauble 对象的数组，该数组和饰品栏同等大小
     */
    private IMaidBauble[] baubles;

    /**
     * 构建默认大小（1 格）的饰品栏
     */
    public BaubleItemHandler() {
        this(1);
    }

    /**
     * 构建 size 大小的饰品栏
     *
     * @param size 饰品栏大小
     */
    public BaubleItemHandler(int size) {
        super(size);
        baubles = new IMaidBauble[size];
    }

    /**
     * 通过输入的 NonNullList<ItemStack> 构建饰品栏大小
     *
     * @param stacks 输入的 NonNullList<ItemStack>
     */
    public BaubleItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
        baubles = new IMaidBauble[stacks.size()];
        IntStream.range(0, getSlots()).forEach(this::onContentsChanged);
    }

    /**
     * 重新更改饰品栏大小
     *
     * @param size 改变的大小
     */
    @Override
    public void setSize(int size) {
        if (size == stacks.size()) {
            Arrays.fill(baubles, null);
        } else {
            baubles = new IMaidBauble[stacks.size()];
        }
        super.setSize(size);
    }

    /**
     * 设定指定格子的物品的 IMaidBauble 对象
     *
     * @param slot   指定的格子
     * @param bauble 设定的 IMaidBauble 对象
     */
    private void setBaubleInSlot(int slot, @Nullable IMaidBauble bauble) {
        validateSlotIndex(slot);
        baubles[slot] = bauble;
    }

    /**
     * 获取指定格子的 IMaidBauble 对象
     *
     * @param slot 指定的格子
     * @return 获取的 IMaidBauble 对象
     */
    @Nullable
    public IMaidBauble getBaubleInSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) {
            return null;
        } else {
            return baubles[slot];
        }
    }

    /**
     * 当内容改变时触发的方法
     *
     * @param slot 触发的格子
     */
    @Override
    protected void onContentsChanged(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) {
            setBaubleInSlot(slot, null);
        } else {
            setBaubleInSlot(slot, BaubleManager.getBauble(stack));
        }
    }

    /**
     * 物品是否合法
     *
     * @param slot  格子
     * @param stack 传入的物品堆
     * @return 物品是否合法
     */
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return BaubleManager.getBauble(stack) != null;
    }

    /**
     * 插入物品时的逻辑
     */
    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (isItemValid(slot, stack)) {
            return super.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    /**
     * 处理反序列化时的饰品加载
     */
    @Override
    protected void onLoad() {
        IntStream.range(0, getSlots()).forEach(this::onContentsChanged);
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

package com.github.tartaricacid.touhoulittlemaid.api.util;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * 基于 Forge 的 ItemStackHandler 继承修改的类，用于构建饰品栏对象
 *
 * @author Snownee
 * @date 2019/7/23 14:53
 */
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
        // 依据传入的 ItemStack 是否为空，尝试往 baubles 中塞入 null
        IntStream.range(0, getSlots()).forEach(this::onContentsChanged);
    }

    /**
     * 重新更改饰品栏大小
     *
     * @param size 改变的大小
     */
    @Override
    public void setSize(int size) {
        // 如果 size 和先前一致，只需要清空 baubles 就行
        if (size == stacks.size()) {
            Arrays.fill(baubles, null);
        }
        // 否则重新创建数组
        else {
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
    private void setBaubleInSlot(int slot, IMaidBauble bauble) {
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
        // 如果 stack 为空，返回 null
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
        // 如果物品为空，该格子的 baubles 设置为 null
        if (stack.isEmpty()) {
            setBaubleInSlot(slot, null);
        }
        // 否则通过 API 获取对应的对象，塞入 baubles
        else {
            setBaubleInSlot(slot, LittleMaidAPI.getBauble(stack));
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
        // 只有在 LittleMaidAPI 拥有对应对象的，才是合法的
        return LittleMaidAPI.getBauble(stack) != null;
    }

    /**
     * 插入物品时的逻辑
     */
    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        // 物品合法才允许插入，否则原样返回
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

    /**
     * 事件触发
     *
     * @param function 触发时应用的逻辑
     * @return 该事件是否成功触发
     */
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

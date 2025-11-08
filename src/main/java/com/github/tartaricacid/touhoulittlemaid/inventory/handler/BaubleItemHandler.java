package com.github.tartaricacid.touhoulittlemaid.inventory.handler;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

public class BaubleItemHandler extends ItemStackHandler {
    /**
     * 存储 IMaidBauble 对象的数组，该数组和饰品栏不同等大小
     */
    private final Int2ObjectSortedMap<IMaidBauble> baubles = new Int2ObjectRBTreeMap<>();
    /**
     * 存储所有物品的缓存集合，用于提高查询效率
     */
    private final Set<Item> baubleItemsCache = Sets.newHashSet();

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
    }

    /**
     * 通过输入的 NonNullList<ItemStack> 构建饰品栏大小
     *
     * @param stacks 输入的 NonNullList<ItemStack>
     */
    public BaubleItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
        IntStream.range(0, getSlots()).forEach(this::onContentsChanged);
    }

    /**
     * 设定指定格子的物品的 IMaidBauble 对象
     *
     * @param slot   指定的格子
     * @param bauble 设定的 IMaidBauble 对象
     */
    private void setBaubleInSlot(int slot, @Nullable IMaidBauble bauble) {
        validateSlotIndex(slot);
        if (bauble == null) {
            baubles.remove(slot);
        } else {
            baubles.put(slot, bauble);
        }
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
            return baubles.get(slot);
        }
    }

    /**
     * 当内容改变时触发的方法
     *
     * @param slot 触发的格子
     */
    @Override
    protected void onContentsChanged(int slot) {
        // 更新饰品信息
        this.updateBaubles(slot);
        // 更新物品缓存
        this.updateBaublesCache();
    }

    /**
     * 更新指定格子的饰品信息
     *
     * @param slot 指定的格子
     */
    protected void updateBaubles(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) {
            setBaubleInSlot(slot, null);
        } else {
            setBaubleInSlot(slot, BaubleManager.getBauble(stack));
        }
    }

    protected void updateBaublesCache() {
        baubleItemsCache.clear();
        for (int baubleSlot : baubles.keySet()) {
            ItemStack stack = getStackInSlot(baubleSlot);
            if (!stack.isEmpty()) {
                baubleItemsCache.add(stack.getItem());
            }
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
        IntStream.range(0, getSlots()).forEach(this::updateBaubles);
        this.updateBaublesCache();
    }

    public boolean fireEvent(BiPredicate<IMaidBauble, ItemStack> function) {
        var iterator = baubles.int2ObjectEntrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            int slot = entry.getIntKey();

            IMaidBauble bauble = entry.getValue();
            ItemStack stack = getStackInSlot(slot);

            if (stack.isEmpty()) {
                // 删除不存在物品的映射
                iterator.remove();
                continue;
            }

            if (function.test(bauble, stack)) {
                return true;
            }
        }
        return false;
    }

    public int getBaubleSlot(IMaidBauble bauble) {
        for (var entry : baubles.int2ObjectEntrySet()) {
            if (entry.getValue() == bauble) {
                return entry.getIntKey();
            }
        }
        return -1;
    }

    @ApiStatus.AvailableSince("1.4.3")
    public boolean containsItem(Item item) {
        return baubleItemsCache.contains(item);
    }
}

/*
 * This file is adapted from Kiwi
 *
 * Original file:
 * src/main/java/snownee/kiwi/util/definition/ItemDefinition.java
 *
 * Kiwi is licensed under MIT. The original license header are provided below.
 *
 *
 * Copyright (c) 2019 BogoWorks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.tartaricacid.touhoulittlemaid.api.util;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IRegistryDelegate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * 可比较的物品定义信息，忽略 NBT 数据及数量，可用作 Map 的键。
 * 主要用于后续映射 物品 -> IMaidBauble 对象
 *
 * @author Snownee
 * @date 2019/7/23 14:53
 */
public final class ItemDefinition implements Comparable<ItemDefinition> {
    /**
     * 空物品定义
     */
    public static final ItemDefinition EMPTY = of(Items.AIR);
    /**
     * 因为存在某些模组（比如潘马思）替换原版物品的情况，
     * 替换后就会发生 Hash 变化，导致 物品 -> IMaidBauble 映射表出错，
     * 通过注册项来获取的对象支持替换，后续的映射表就不会出现问题。
     */
    private final IRegistryDelegate<Item> item;
    private final int metadata;

    /**
     * 通过传入物品和 metadata 来构建 ItemDefinition 对象
     *
     * @param item     物品
     * @param metadata 元数据值
     */
    public ItemDefinition(Item item, int metadata) {
        this.item = item.delegate;
        this.metadata = metadata;
    }

    /**
     * 将物品转换为 ItemDefinition 对象
     */
    public static ItemDefinition of(Item item) {
        return of(item, 0);
    }

    /**
     * 将 ItemStack 转换为 ItemDefinition 对象
     */
    public static ItemDefinition of(ItemStack stack) {
        return stack.isEmpty() ? EMPTY : of(stack.getItem(), stack.getHasSubtypes() ? stack.getMetadata() : 0);
    }

    /**
     * 将方块转换为 ItemDefinition 对象
     */
    public static ItemDefinition of(Block block) {
        return of(Item.getItemFromBlock(block));
    }

    /**
     * 将带 metadata 的方块转换为 ItemDefinition 对象
     */
    public static ItemDefinition of(Block block, int metadata) {
        return of(Item.getItemFromBlock(block), metadata);
    }

    /**
     * 将带 metadata 的物品转换为 ItemDefinition 对象
     */
    public static ItemDefinition of(Item item, int metadata) {
        return new ItemDefinition(item, metadata);
    }

    /**
     * 获取该对象对应的物品
     */
    public Item getItem() {
        return item.get();
    }

    /**
     * 获取物品的 metadata
     */
    public int getMetadata() {
        return metadata;
    }

    /**
     * 返回该类对应的物品堆
     */
    @Nonnull
    public List<ItemStack> examples() {
        // 如果 meta 为 WILDCARD_VALUE，则返回所有的子类
        if (metadata == OreDictionary.WILDCARD_VALUE) {
            CreativeTabs itemGroup = item.get().getCreativeTab();
            if (itemGroup != null) {
                NonNullList<ItemStack> stacks = NonNullList.create();
                item.get().getSubItems(itemGroup, stacks);
                return stacks;
            }
        }
        // 否则只返回单例集合
        return Collections.singletonList(this.getItemStack());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemDefinition that = (ItemDefinition) o;
        return this.item.equals(that.item) && this.metadata == that.metadata;
    }

    /**
     * 匹配物品堆是否和本对象相等
     */
    public boolean matches(ItemStack stack) {
        // 如果 meta 为 WILDCARD_VALUE，或 meta 互相匹配
        boolean metaMatches = OreDictionary.WILDCARD_VALUE == metadata || stack.getMetadata() == metadata;
        return stack.getItem().delegate == this.item && metaMatches;
    }

    @Override
    public int hashCode() {
        // 31 这个数字可以有效减少哈希碰撞问题
        return item.hashCode() * 31 + metadata;
    }

    @Override
    public int compareTo(ItemDefinition o) {
        int result = this.item.name().compareTo(o.item.name());
        return result == 0 ? this.metadata - o.metadata : result;
    }

    @Override
    public String toString() {
        // 如果 meta 为 WILDCARD_VALUE，输出为 * 字符
        String metaChar = metadata != OreDictionary.WILDCARD_VALUE ? Integer.toString(metadata) : "*";
        return item.name() + ":" + metaChar;
    }

    /**
     * 将该 ItemDefinition 对象转换为 ItemStack 对象.
     */
    public ItemStack getItemStack() {
        // 如果 metadata 为 WILDCARD_VALUE，返回 0
        int meta = metadata == OreDictionary.WILDCARD_VALUE ? 0 : this.metadata;
        return new ItemStack(this.item.get(), 1, meta, null);
    }

    public boolean isEmpty() {
        return item.get() == Items.AIR;
    }
}

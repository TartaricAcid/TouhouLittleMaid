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

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IRegistryDelegate;

/**
 * Comparable, NBT-insensitive, size-insensitive item definition that may be used as key of Map.
 *
 * 可比较的物品定义信息，忽略 NBT 数据及数量，可用作 Map 的键。
 */
public final class ItemDefinition implements Comparable<ItemDefinition>
{
    public static final ItemDefinition EMPTY = of(Items.AIR);

    public static ItemDefinition of(Item item)
    {
        return of(item, 0);
    }

    public static ItemDefinition of(ItemStack stack)
    {
        return stack.isEmpty() ? EMPTY : of(stack.getItem(), stack.getHasSubtypes() ? stack.getMetadata() : 0);
    }

    public static ItemDefinition of(Block block)
    {
        return of(Item.getItemFromBlock(block));
    }

    public static ItemDefinition of(Block block, int metadata)
    {
        return of(Item.getItemFromBlock(block), metadata);
    }

    public static ItemDefinition of(Item item, int metadata)
    {
        return new ItemDefinition(item, metadata);
    }

    private final IRegistryDelegate<Item> item;
    private final int metadata;

    public ItemDefinition(Item item, int metadata)
    {
        this.item = item.delegate;
        this.metadata = metadata;
    }

    public Item getItem()
    {
        return item.get();
    }

    public int getMetadata()
    {
        return metadata;
    }

    @Nonnull
    public List<ItemStack> examples()
    {
        if (metadata == OreDictionary.WILDCARD_VALUE)
        {
            CreativeTabs itemGroup = item.get().getCreativeTab();
            if (itemGroup != null)
            {
                NonNullList<ItemStack> stacks = NonNullList.create();
                item.get().getSubItems(itemGroup, stacks);
                return stacks;
            }
        }
        return Collections.singletonList(this.getItemStack());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ItemDefinition that = (ItemDefinition) o;
        return this.item.equals(that.item) && this.metadata == that.metadata;
    }

    public boolean matches(ItemStack stack)
    {
        return stack.getItem().delegate == this.item && (OreDictionary.WILDCARD_VALUE == metadata || stack.getMetadata() == metadata);
    }

    @Override
    public int hashCode()
    {
        return item.hashCode() * 31 + metadata;
    }

    @Override
    public int compareTo(ItemDefinition o)
    {
        int result = this.item.name().compareTo(o.item.name());
        return result == 0 ? this.metadata - o.metadata : result;
    }

    @Override
    public String toString()
    {
        return item.name() + ":" + (metadata != OreDictionary.WILDCARD_VALUE ? metadata : "*");
    }

    /**
     * 将该 ItemDefinition 对象转换为 ItemStack 对象.
     * @return The only possible permutation of this ItemDefinition, in ItemStack form
     */
    public ItemStack getItemStack()
    {
        return new ItemStack(this.item.get(), 1, metadata != OreDictionary.WILDCARD_VALUE ? this.metadata : 0, null);
    }

    public boolean isEmpty()
    {
        return item.get() == Items.AIR;
    }
}

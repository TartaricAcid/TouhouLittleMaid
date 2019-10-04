/*
 * This file is adapted from Kiwi
 *
 * Original file:
 * src/main/java/snownee/kiwi/util/definition/OreDictDefinition.java
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

import net.minecraft.item.ItemStack;

public class OreDictDefinition implements ProcessingInput
{
    public static final OreDictDefinition EMPTY = of("");

    String ore;

    private OreDictDefinition(String ore)
    {
        this.ore = ore;
    }

    public static OreDictDefinition of(String ore)
    {
        return new OreDictDefinition(ore);
    }

    @Override
    public List<ItemStack> examples()
    {
        return isEmpty() ? Collections.emptyList() : Util.getItemsFromOre(ore, 1);
    }

    @Override
    public boolean matches(ItemStack stack)
    {
        return Util.doesItemHaveOreName(stack, ore);
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
        OreDictDefinition that = (OreDictDefinition) o;
        return ore.equals(that.ore);
    }

    @Override
    public String toString()
    {
        return "ore:" + (isEmpty() ? "null" : ore);
    }

    @Override
    public int hashCode()
    {
        return ore.hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return ore.isEmpty();
    }
}

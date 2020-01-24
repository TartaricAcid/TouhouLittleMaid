/*
 * This file is adapted from Cuisine
 *
 * Original file:
 * src/main/java/snownee/cuisine/plugins/crafttweaker/CTIngredientInput.java
 *
 * Cuisine is licensed under MIT. The original license header are provided below.
 *
 *
 * Copyright (c) 2020 BogoWorks
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

package com.github.tartaricacid.touhoulittlemaid.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class CTIngredientInput implements ProcessingInput
{
    private final IIngredient ingredient;

    CTIngredientInput(IIngredient ingredient)
    {
        this.ingredient = ingredient;
    }

    @Nonnull
    @Override
    public List<ItemStack> examples()
    {
        return Optional.ofNullable(this.ingredient.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(AltarZen::toItemStack)
                .collect(Collectors.toList());
    }

    @Override
    public boolean matches(@Nonnull ItemStack itemStack)
    {
        return this.ingredient.matches(CraftTweakerMC.getIItemStack(itemStack));
    }

    @Override
    public boolean isEmpty()
    {
        return this.ingredient != null && this.ingredient.getAmount() > 0;
    }

}

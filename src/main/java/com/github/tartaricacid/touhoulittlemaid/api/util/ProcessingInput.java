package com.github.tartaricacid.touhoulittlemaid.api.util;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;

public interface ProcessingInput extends Predicate<ItemStack>
{

    /**
     * Return a list of ItemStack so that each element e in the list will
     * have <code>matches(e) == true</code>. The returning list may not
     * be all permutations of possibilities, and can only serve as a
     * reference, used in situations like JEI where a demonstration is
     * required.
     *
     * @return A list of ItemStack instances that satisfy <code>matches(e)
     * == true</code>.
     */
    @Nonnull List<ItemStack> examples();

    /**
     * Perform a check to determine whether the actual ItemStack satisfies
     * the criteria defined by this <code>CuisineProcessingInput</code>.
     *
     * @param stack The actual ItemStack instance to be examined.
     *
     * @return <code>true</code> if the given instance is matched;
     * <code>false</code> if otherwise.
     */
    boolean matches(@Nonnull ItemStack stack);

    @Override
    default boolean apply(ItemStack stack)
    {
        return matches(stack);
    }
    
    /**
     * @return <code>true</code> if this effectively represents an "empty"
     * state, and thus cannot matches anything; <code>false</code> if
     * otherwise.
     *
     * @implSpec
     * If <code>isEmpty</code> returns <code>true</code>, then any invocations
     * of <code>matches</code> on this must return <code>false</code>.
     */
    boolean isEmpty();

    /**
     * Perform equivalence check with another object.
     *
     * @param obj Another object
     *
     * @return <code>true</code> if the given object is identical to this.
     */
    @Override
    boolean equals(Object obj);
}

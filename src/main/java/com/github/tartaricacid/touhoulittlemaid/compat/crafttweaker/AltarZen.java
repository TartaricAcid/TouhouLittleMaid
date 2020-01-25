package com.github.tartaricacid.touhoulittlemaid.compat.crafttweaker;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;
import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipesManager;
import com.google.common.collect.Lists;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author TartaricAcid
 * @date 2020/1/10 16:12
 **/
@ZenClass("mods.touhoulittlemaid.Altar")
@ZenRegister
public class AltarZen {
    public static final List<IAction> DELAYED_ACTIONS = Lists.newLinkedList();

    @ZenMethod
    public static void addItemCraftRecipe(String id, float powerCost, IItemStack output, IIngredient... input) {
        DELAYED_ACTIONS.add(new AddItemCraftRecipe(id, powerCost, output, input));
    }

    @ZenMethod
    public static void removeRecipe(String id) {
        DELAYED_ACTIONS.add(new RemoveRecipe(id));
    }

    @Nonnull
    public static ItemStack toItemStack(IItemStack itemStack) {
        Object internal = itemStack.getInternal();
        if (!(internal instanceof ItemStack)) {
            TouhouLittleMaid.LOGGER.error("Not a valid item stack: " + itemStack);
            return ItemStack.EMPTY;
        }
        return (ItemStack) internal;
    }

    @Nonnull
    public static Stream<ItemStack> toItemStacks(IItemStack itemStack) {
        ItemStack raw = toItemStack(itemStack);
        if (raw.getMetadata() == OreDictionary.WILDCARD_VALUE) {
            NonNullList<ItemStack> items = NonNullList.create();
            raw.getItem().getSubItems(raw.getItem().getCreativeTab(), items);
            return items.stream();
        } else {
            return raw.isEmpty() ? Collections.EMPTY_LIST.stream() : Stream.of(raw);
        }
    }

    private static ProcessingInput[] toProcessingInput(IIngredient... ingredient) {
        ProcessingInput[] processingInputs = new ProcessingInput[ingredient.length];
        for (int i = 0; i < processingInputs.length; i++) {
            processingInputs[i] = new CTIngredientInput(ingredient[i]);
        }
        return processingInputs;
    }

    public static class AddItemCraftRecipe implements IAction {
        private String id;
        private float powerCost;
        private IItemStack output;
        private IIngredient[] input;

        AddItemCraftRecipe(String id, float powerCost, IItemStack output, IIngredient[] input) {
            this.id = id;
            this.powerCost = powerCost;
            this.output = output;
            this.input = input;
        }

        @Override
        public void apply() {
            AltarRecipesManager.instance().addItemCraftRecipe(new ResourceLocation(id), powerCost, toItemStack(output), toProcessingInput(input));
        }

        @Override
        public String describe() {
            return "Add altar item craft recipe: " + id;
        }
    }

    public static class RemoveRecipe implements IAction {
        private String id;

        RemoveRecipe(String id) {
            this.id = id;
        }

        @Override
        public void apply() {
            AltarRecipesManager.instance().removeRecipe(new ResourceLocation(id));
        }

        @Override
        public String describe() {
            return "Delete altar item craft recipe: " + id;
        }
    }
}

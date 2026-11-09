package com.github.tartaricacid.touhoulittlemaid.compat.wab.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 双线 API 差异的唯一落点。除本文件外，compat/wab/config/ 下其余文件在 Forge 1.20.1 与
 * NeoForge 1.21.1 两条线上必须字节一致。业务类只调这里，不直接碰注册表、附魔组件、物品句柄包。
 */
public final class WabPlatform {
    private WabPlatform() {
    }

    /** 把附魔、NBT 有无等属性补进 AI 工具回执。 */
    public static void appendAttributes(ItemStack stack, JsonObject out) {
        JsonArray enchants = new JsonArray();
        net.minecraft.world.item.enchantment.ItemEnchantments data = stack.getOrDefault(
                net.minecraft.core.component.DataComponents.ENCHANTMENTS,
                net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
        for (java.util.Map.Entry<net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment>, Integer> e
             : data.entrySet()) {
            JsonObject one = new JsonObject();
            ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.ENCHANTMENT.getKey(e.getKey().value());
            one.addProperty("id", key == null ? "" : key.toString());
            one.addProperty("level", e.getValue());
            enchants.add(one);
        }
        out.add("enchantments", enchants);
        out.addProperty("hasNbt", stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA) != null);
    }

    public static boolean hasEnchantment(ItemStack stack) {
        return !stack.getOrDefault(net.minecraft.core.component.DataComponents.ENCHANTMENTS,
                net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY).entrySet().isEmpty();
    }

    /** 把物品塞进女仆背包，返回塞不下的剩余部分。 */
    public static ItemStack insertStacked(Object inv, ItemStack stack) {
        return net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked(
                (net.neoforged.neoforge.items.IItemHandler) inv, stack, false);
    }

    /** 物品 id。 */
    public static String itemId(ItemStack stack) {
        ResourceLocation key = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
        return key == null ? "" : key.toString();
    }

    /** 两件物品是否可堆叠/合并。 */
    public static boolean sameItem(ItemStack a, ItemStack b) {
        return ItemStack.isSameItemSameComponents(a, b);
    }

    /** 按 id 造一份物品（农耕播种用）。 */
    public static ItemStack itemFromId(String id) {
        ResourceLocation key = ResourceLocation.tryParse(id);
        Item item = key == null ? null : net.minecraft.core.registries.BuiltInRegistries.ITEM.get(key);
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }
}

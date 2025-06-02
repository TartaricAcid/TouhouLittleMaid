package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class ItemDamageableBauble extends Item {
    public ItemDamageableBauble(int durability) {
        super((new Properties()).durability(durability).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment == Enchantments.MENDING && stack.is(TagItem.MAID_MENDING_BLOCKLIST_ITEM)) {
            return false;
        }
        if (enchantment == Enchantments.VANISHING_CURSE && stack.is(TagItem.MAID_VANISHING_BLOCKLIST_ITEM)) {
            return false;
        }
        return super.supportsEnchantment(stack, enchantment);
    }
}

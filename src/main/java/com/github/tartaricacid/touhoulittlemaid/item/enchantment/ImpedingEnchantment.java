package com.github.tartaricacid.touhoulittlemaid.item.enchantment;

import com.github.tartaricacid.touhoulittlemaid.init.InitEnchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class ImpedingEnchantment extends Enchantment {
    public ImpedingEnchantment() {
        super(Rarity.UNCOMMON, InitEnchantments.GOHEI, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    /**
     * 消耗 10 18 26 34
     */
    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 8;
    }
}

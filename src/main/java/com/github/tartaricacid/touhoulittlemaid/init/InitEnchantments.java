package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.EndersEnderEnchantment;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.ImpedingEnchantment;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.SpeedyEnchantment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, TouhouLittleMaid.MOD_ID);
    public static final DeferredHolder<Enchantment,Enchantment> IMPEDING = ENCHANTMENTS.register("impeding", ImpedingEnchantment::new);
    public static final DeferredHolder<Enchantment,Enchantment> SPEEDY = ENCHANTMENTS.register("speedy", SpeedyEnchantment::new);
    public static final DeferredHolder<Enchantment,Enchantment> ENDERS_ENDER = ENCHANTMENTS.register("enders_ender", EndersEnderEnchantment::new);
}

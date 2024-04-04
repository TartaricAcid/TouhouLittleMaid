package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.EndersEnderEnchantment;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.ImpedingEnchantment;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.SpeedyEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<Enchantment> IMPEDING = ENCHANTMENTS.register("impeding", ImpedingEnchantment::new);
    public static final RegistryObject<Enchantment> SPEEDY = ENCHANTMENTS.register("speedy", SpeedyEnchantment::new);
    public static final RegistryObject<Enchantment> ENDERS_ENDER = ENCHANTMENTS.register("enders_ender", EndersEnderEnchantment::new);

    public static final EnchantmentCategory GOHEI = EnchantmentCategory.create("gohei", item -> item instanceof ItemHakureiGohei);
}

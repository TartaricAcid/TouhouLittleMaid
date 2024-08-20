package com.github.tartaricacid.touhoulittlemaid.dataGen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public class EnchantmentKeys {
    public static final ResourceKey<Enchantment> IMPEDING = registerKey("impeding");
    public static final ResourceKey<Enchantment> SPEEDY = registerKey("speedy");
    public static final ResourceKey<Enchantment> ENDERS_ENDER = registerKey("enders_ender");

    private static ResourceKey<Enchantment> registerKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, getResourceLocation(name));
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        // TODO 附魔的参数可能会需要调
        register(context, IMPEDING, new Enchantment.Builder(
                Enchantment.definition(
                        items.getOrThrow(TagItem.GOHEI_ENCHANTABLE),
                        4,
                        4,
                        Enchantment.dynamicCost(10, 8),
                        Enchantment.dynamicCost(15, 8),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        ));

        register(context, SPEEDY, new Enchantment.Builder(
                Enchantment.definition(
                        items.getOrThrow(TagItem.GOHEI_ENCHANTABLE),
                        2,
                        2,
                        Enchantment.dynamicCost(15, 10),
                        Enchantment.dynamicCost(20, 10),
                        2,
                        EquipmentSlotGroup.MAINHAND
                )
        ));

        register(context, ENDERS_ENDER, new Enchantment.Builder(
                Enchantment.definition(
                        items.getOrThrow(TagItem.GOHEI_ENCHANTABLE),
                        1,
                        1,
                        Enchantment.constantCost(20),
                        Enchantment.constantCost(50),
                        4,
                        EquipmentSlotGroup.MAINHAND
                )
        ));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    public static int getEnchantmentLevel(RegistryAccess access, ResourceKey<Enchantment> enchantmentResourceKey, ItemStack mainHandItem) {
        return EnchantmentHelper.getTagEnchantmentLevel(getEnchantmentHolder(access, enchantmentResourceKey), mainHandItem);
    }

    public static Holder<Enchantment> getEnchantmentHolder(RegistryAccess access, ResourceKey<Enchantment> enchantmentResourceKey) {
        return access.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(enchantmentResourceKey);
    }
}

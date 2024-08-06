package com.github.tartaricacid.touhoulittlemaid.compat.domesticationinnovation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Map;

@EventBusSubscriber
public class EnchantmentRefuse {
    private static final String DOMESTICATION_INNOVATION = "domesticationinnovation";
    private static final ResourceLocation COLLAR_TAG_ID = ResourceLocation.fromNamespaceAndPath(DOMESTICATION_INNOVATION, "collar_tag");
    private static final ResourceLocation UNDEAD_CURSE_ENCHANTMENT_ID = ResourceLocation.fromNamespaceAndPath(DOMESTICATION_INNOVATION, "undead_curse");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInteractMaid(PlayerInteractEvent.EntityInteract event) {
        if (!ModList.get().isLoaded(DOMESTICATION_INNOVATION)) {
            return;
        }
        if (!(event.getTarget() instanceof EntityMaid maid)) {
            return;
        }
        if (maid.level.isClientSide) {
            return;
        }
        Player player = event.getEntity();
        if (!maid.isOwnedBy(player)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        ResourceLocation itemsKey = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!COLLAR_TAG_ID.equals(itemsKey)) {
            return;
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        for (Enchantment enchantment : enchantments.keySet()) {
            ResourceLocation enchantmentsKey = BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
            if (UNDEAD_CURSE_ENCHANTMENT_ID.equals(enchantmentsKey)) {
                event.setCanceled(true);
                player.sendSystemMessage(Component.translatable("domesticationinnovation.touhou_little_maid.enchantment.undead_curse"));
                return;
            }
        }
    }
}

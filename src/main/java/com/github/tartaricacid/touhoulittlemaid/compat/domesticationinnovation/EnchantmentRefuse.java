package com.github.tartaricacid.touhoulittlemaid.compat.domesticationinnovation;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber
public class EnchantmentRefuse {
    private static final String DOMESTICATION_INNOVATION = "domesticationinnovation";
    private static final ResourceLocation COLLAR_TAG_ID = new ResourceLocation(DOMESTICATION_INNOVATION, "collar_tag");
    private static final ResourceLocation UNDEAD_CURSE_ENCHANTMENT_ID = new ResourceLocation(DOMESTICATION_INNOVATION, "undead_curse");
    private static final ResourceLocation BLAZING_PROTECTION_ENCHANTMENT_ID = new ResourceLocation(DOMESTICATION_INNOVATION, "blazing_protection");

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
        Player player = event.getPlayer();
        if (!maid.isOwnedBy(player)) {
            return;
        }
        ItemStack stack = event.getItemStack();
        ResourceLocation itemsKey = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (!COLLAR_TAG_ID.equals(itemsKey)) {
            return;
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        for (Enchantment enchantment : enchantments.keySet()) {
            ResourceLocation enchantmentsKey = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
            if (UNDEAD_CURSE_ENCHANTMENT_ID.equals(enchantmentsKey)) {
                event.setCanceled(true);
                player.sendMessage(new TranslatableComponent("domesticationinnovation.touhou_little_maid.enchantment.undead_curse"), Util.NIL_UUID);
                return;
            }
            if (BLAZING_PROTECTION_ENCHANTMENT_ID.equals(enchantmentsKey)) {
                event.setCanceled(true);
                player.sendMessage(new TranslatableComponent("domesticationinnovation.touhou_little_maid.enchantment.blazing_protection"), Util.NIL_UUID);
                return;
            }
        }
    }
}
package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class ApplyGoldenAppleEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (itemstack.getItem() == Items.GOLDEN_APPLE) {
            if (!world.isRemote) {
                // 作用效果
                if (itemstack.getMetadata() > 0) {
                    maid.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                    maid.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                    maid.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                    maid.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
                } else {
                    maid.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
                    maid.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
                }
            }

            // 物品消耗判定
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            // 播放音效
            maid.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            event.setCanceled(true);
        }
    }
}

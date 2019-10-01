package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Snownee
 * @date 2019/7/23 14:53
 */
public class UndyingTotemBauble implements IMaidBauble {
    public UndyingTotemBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof AbstractEntityMaid) {
            AbstractEntityMaid maid = (AbstractEntityMaid) entity;
            int slot = LittleMaidAPI.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                maid.getBaubleInv().setStackInSlot(slot, ItemStack.EMPTY);
                event.setCanceled(true);
                maid.setHealth(1.0F);
                maid.clearActivePotions();
                maid.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                maid.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
                maid.world.setEntityState(entity, (byte) 35);
            }
        }
    }
}

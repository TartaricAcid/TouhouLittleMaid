package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UndyingTotem implements IMaidBauble
{
    public UndyingTotem()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDeath(LivingDeathEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        if (LittleMaidAPI.isMaidEntity(entity))
        {
            int slot = LittleMaidAPI.getBaubleSlotInMaid(entity, this);
            if (slot >= 0)
            {
                LittleMaidAPI.getBaubleInventory(entity).setStackInSlot(slot, ItemStack.EMPTY);
                event.setCanceled(true);
                entity.setHealth(1.0F);
                entity.clearActivePotions();
                entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
                entity.world.setEntityState(entity, (byte) 35);
            }
        }
    }
}

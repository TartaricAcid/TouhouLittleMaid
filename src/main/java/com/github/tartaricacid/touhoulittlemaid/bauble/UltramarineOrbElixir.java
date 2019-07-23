package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UltramarineOrbElixir implements IMaidBauble
{
    public UltramarineOrbElixir()
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
                event.setCanceled(true);
                entity.setHealth(entity.getMaxHealth());
                ((EntityLiving) entity).spawnExplosionParticle();
                ItemStack stack = LittleMaidAPI.getBaubleInventory(entity).getStackInSlot(slot);
                stack.damageItem(1, entity);
                LittleMaidAPI.getBaubleInventory(entity).setStackInSlot(slot, stack);
                // TODO: 注册独立的声音事件
                entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
            }
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
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
        if (entity instanceof AbstractEntityMaid)
        {
            AbstractEntityMaid maid = (AbstractEntityMaid) entity;
            int slot = LittleMaidAPI.getBaubleSlotInMaid(maid, this);
            if (slot >= 0)
            {
                event.setCanceled(true);
                maid.setHealth(entity.getMaxHealth());
                maid.spawnExplosionParticle();
                ItemStack stack = maid.getBaubleInventory().getStackInSlot(slot);
                stack.damageItem(1, maid);
                maid.getBaubleInventory().setStackInSlot(slot, stack);
                // TODO: 注册独立的声音事件
                maid.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
            }
        }
    }
}

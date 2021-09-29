package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ItemBreakMessage;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProjectileProtectBauble implements IMaidBauble {
    public ProjectileProtectBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidDamageEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        if (source.isProjectile()) {
            int slot = ItemsUtil.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                event.setCanceled(true);
                ItemStack stack = maid.getMaidBauble().getStackInSlot(slot);
                stack.hurtAndBreak(1, maid, (m) -> sendItemBreakMessage(m, stack));
                maid.getMaidBauble().setStackInSlot(slot, stack);
                maid.spawnExplosionParticle();
            }
        }
    }

    private void sendItemBreakMessage(EntityMaid maid, ItemStack stack) {
        NetworkHandler.sendToNearby(maid.level, maid.blockPosition(), new ItemBreakMessage(maid.getId(), stack));
    }
}

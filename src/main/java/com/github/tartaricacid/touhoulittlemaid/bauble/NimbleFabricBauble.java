package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NimbleFabricBauble implements IMaidBauble {
    private static final int MAX_RETRY = 16;

    public NimbleFabricBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(LivingAttackEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        DamageSource source = event.getSource();
        if (entity instanceof AbstractEntityMaid && source instanceof EntityDamageSourceIndirect) {
            AbstractEntityMaid maid = (AbstractEntityMaid) entity;
            int slot = LittleMaidAPI.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                event.setCanceled(true);
                ItemStack stack = maid.getBaubleInv().getStackInSlot(slot);
                stack.damageItem(1, maid);
                maid.getBaubleInv().setStackInSlot(slot, stack);
                for (int i = 0; i < MAX_RETRY; ++i) {
                    if (teleportRandomly(maid)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean teleportRandomly(AbstractEntityMaid maid) {
        double x = maid.posX + (maid.world.rand.nextDouble() - 0.5D) * 16.0D;
        double y = maid.posY + (double) (maid.world.rand.nextInt(64) - 8);
        double z = maid.posZ + (maid.world.rand.nextDouble() - 0.5D) * 16.0D;
        return this.teleportTo(maid, x, y, z);
    }

    private boolean teleportTo(AbstractEntityMaid maid, double x, double y, double z) {
        boolean teleportIsSuccess = maid.attemptTeleport(x, y, z);
        if (teleportIsSuccess) {
            maid.world.playSound(null, maid.prevPosX, maid.prevPosY, maid.prevPosZ,
                    SoundEvents.ENTITY_ENDERMEN_TELEPORT, maid.getSoundCategory(), 1.0F, 1.0F);
            maid.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
        return teleportIsSuccess;
    }
}

package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class SwitchSittingEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onInteract(InteractMaidEvent event) {
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (player.isSneaking()) {
            if (maid.isSleep()) {
                return;
            }

            maid.setSitting(!maid.isSitting());
            if (maid.isSitting()) {
                // 清除寻路逻辑
                maid.getNavigator().clearPath();
                // 清除所有的攻击目标
                maid.setAttackTarget(null);
                maid.setRevengeTarget(null);
            }
            maid.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F,
                    ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            event.setCanceled(true);
        }
    }
}

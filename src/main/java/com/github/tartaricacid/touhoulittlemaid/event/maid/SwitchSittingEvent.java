package com.github.tartaricacid.touhoulittlemaid.event.maid;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.event.api.InteractMaidEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SwitchSittingEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onInteractMaid(InteractMaidEvent event) {
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (player.isShiftKeyDown()) {
            maid.setInSittingPose(!maid.isInSittingPose());
            if (maid.isInSittingPose()) {
                // 清除寻路逻辑
                maid.getNavigation().stop();
                // 清除所有的攻击目标
                maid.setTarget(null);
            }
            maid.playSound(SoundEvents.ITEM_PICKUP, 0.2F,
                    ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            event.setCanceled(true);
        }
    }
}

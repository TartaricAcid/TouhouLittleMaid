package com.github.tartaricacid.touhoulittlemaid.event.maid;


import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class SwitchSittingEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onInteractMaid(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        Level world = event.getWorld();

        if (player.isShiftKeyDown() && !player.getMainHandItem().is(InitItems.KAPPA_COMPASS.get())) {
            maid.setInSittingPose(!maid.isInSittingPose());
            if (maid.isInSittingPose()) {
                maid.getNavigation().stop();
                maid.setTarget(null);
            }
            maid.playSound(SoundEvents.ITEM_PICKUP, 0.2F,
                    ((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            event.setCanceled(true);
        }
    }
}

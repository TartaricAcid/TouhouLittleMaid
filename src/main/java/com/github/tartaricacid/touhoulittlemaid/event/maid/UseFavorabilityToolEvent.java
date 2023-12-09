package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class UseFavorabilityToolEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        EntityMaid maid = event.getMaid();
        ItemStack stack = event.getStack();
        if (!maid.level.isClientSide) {
            boolean success = false;
            if (stack.getItem() == InitItems.FAVORABILITY_TOOL_ADD.get()) {
                FavorabilityManager.add(maid, 64);
                success = true;
            }
            if (stack.getItem() == InitItems.FAVORABILITY_TOOL_REDUCE.get()) {
                FavorabilityManager.reduce(maid, 64);
                success = true;
            }
            if (stack.getItem() == InitItems.FAVORABILITY_TOOL_FULL.get()) {
                FavorabilityManager.max(maid);
                success = true;
            }
            if (success) {
                maid.playSound(SoundEvents.PLAYER_LEVELUP, 0.5F, maid.getRandom().nextFloat() * 0.1F + 0.9F);
                event.setCanceled(true);
            }
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * 主人的潜行直喂：InteractMaidEvent 只有主人右键才会触发，她潜行拿着吃的时取消事件，
 * 把这一右键变成直接投喂——不取消的话它会继续往下走开界面那条路。
 */
@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class WabFeedingEvents {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        ItemStack held = event.getStack();
        if (!WabFeeding.wantsDirectFeed(player, maid, held)) return;
        event.setCanceled(true);
        if (!player.level().isClientSide) {
            WabFeeding.feedFoodDirect(player, maid, held, InteractionHand.MAIN_HAND);
        }
    }
}

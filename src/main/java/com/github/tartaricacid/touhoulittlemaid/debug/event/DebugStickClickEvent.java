package com.github.tartaricacid.touhoulittlemaid.debug.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.debug.target.DebugMaidManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;


@VisibleForDebug
@Mod.EventBusSubscriber
public class DebugStickClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        if (player.getMainHandItem().is(Items.DEBUG_STICK) && TouhouLittleMaid.DEBUG) {
            if (player.isShiftKeyDown()) {
                maid.setOwnerUUID(UUID.randomUUID());
                maid.level.broadcastEntityEvent(maid, EntityEvent.TAMING_SUCCEEDED);
                if (!event.getWorld().isClientSide) {
                    player.sendSystemMessage(Component.translatable("debug.touhou_little_maid.debug_stick.random_owner_uuid"));
                }
            } else {
                if (!event.getWorld().isClientSide) {
                    DebugMaidManager.triggerDebuggingMaid((ServerPlayer) player, maid);
                }
            }
            event.setCanceled(true);
        }
    }
}

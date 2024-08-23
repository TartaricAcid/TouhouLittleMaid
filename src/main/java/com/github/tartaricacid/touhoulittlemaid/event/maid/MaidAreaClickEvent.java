package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncMaidAreaPackage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class MaidAreaClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (player.getMainHandItem().is(Items.COMPASS) && player instanceof ServerPlayer serverPlayer) {
            if (!maid.level.isClientSide) {
                SchedulePos schedulePos = maid.getSchedulePos();
                if (!schedulePos.getWorkPos().equals(BlockPos.ZERO)) {
                    PacketDistributor.sendToPlayer(serverPlayer,new SyncMaidAreaPackage(maid.getId(), schedulePos));
                }
            }
            event.setCanceled(true);
        }
    }
}

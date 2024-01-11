package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncMaidAreaMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MaidAreaClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (player.getMainHandItem().getItem() == Items.COMPASS) {
            if (!maid.level.isClientSide) {
                SchedulePos schedulePos = maid.getSchedulePos();
                if (!schedulePos.getWorkPos().equals(BlockPos.ZERO)) {
                    NetworkHandler.sendToClientPlayer(new SyncMaidAreaMessage(maid.getId(), schedulePos), player);
                }
            }
            event.setCanceled(true);
        }
    }
}

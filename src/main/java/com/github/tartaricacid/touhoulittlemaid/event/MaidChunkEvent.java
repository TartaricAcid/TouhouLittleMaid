package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class MaidChunkEvent {
    @SubscribeEvent
    public static void onMaidJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityMaid && ((EntityMaid) entity).getOwnerUUID() != null) {
            EntityMaid maid = (EntityMaid) entity;
            MaidWorldData data = MaidWorldData.get(maid.level);
            if (data != null) {
                data.removeInfo(maid);
            }
        }
    }

    @SubscribeEvent
    public static void onMaidLevelWorld(EntityLeaveWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityMaid && !entity.level.isClientSide && entity.isAlive() && ((EntityMaid) entity).getOwnerUUID() != null) {
            EntityMaid maid = (EntityMaid) entity;
            MaidWorldData data = MaidWorldData.get(maid.level);
            if (data != null) {
                data.addInfo(maid);
            }
        }
    }
}

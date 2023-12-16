package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MaidChunkEvent {
    @SubscribeEvent
    public static void onMaidJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityMaid maid && maid.getOwnerUUID() != null) {
            MaidWorldData data = MaidWorldData.get(maid.level);
            if (data != null) {
                data.removeInfo(maid);
            }
        }
    }

    @SubscribeEvent
    public static void onMaidLevelWorld(EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityMaid maid && !maid.level.isClientSide && maid.isAlive() && maid.getOwnerUUID() != null) {
            MaidWorldData data = MaidWorldData.get(maid.level);
            if (data != null) {
                data.addInfo(maid);
            }
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.debug;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ChangeMaidOwner {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        if (player.getMainHandItem().getItem() == Items.DEBUG_STICK) {
            maid.setOwnerUUID(UUID.randomUUID());
            event.setCanceled(true);
        }
    }
}

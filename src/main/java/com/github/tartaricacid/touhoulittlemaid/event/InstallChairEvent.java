package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class InstallChairEvent {
    @SubscribeEvent
    public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        ItemStack mainHandItem = player.getMainHandItem();
        if (target instanceof Boat boat && boat.getPassengers().isEmpty() && mainHandItem.is(InitItems.CHAIR.get())) {
            if (player.level instanceof ServerLevel serverLevel) {
                EntityChair spawnChair = ItemChair.getSpawnChair(serverLevel, player, mainHandItem, target.blockPosition(), target.getXRot());
                if (spawnChair != null) {
                    serverLevel.addFreshEntity(spawnChair);
                    spawnChair.startRiding(target);
                }
            }
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.compat.curios;


import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.CuriosContainer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;

public class CuriosEvent {
    /**
     * 当添加可以修改槽位数量的饰品时，重置饰品容器
     */
    @SubscribeEvent
    public void onSlotUpdate(SlotModifiersUpdatedEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof EntityMaid maid && maid.getOwner() instanceof ServerPlayer player) {
            if (player.containerMenu instanceof CuriosContainer container) {
                container.resetPage(player);
            }
        }
    }
}

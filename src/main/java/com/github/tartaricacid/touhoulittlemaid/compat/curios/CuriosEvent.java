package com.github.tartaricacid.touhoulittlemaid.compat.curios;


import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.CuriosContainer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;

public class CuriosEvent {
    /**
     * 当添加可以修改槽位数量的饰品时，重置饰品容器
     */
    @SubscribeEvent
    public void onSlotUpdate(SlotModifiersUpdatedEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof EntityMaid maid && maid.getOwner() instanceof Player player) {
            if (player.containerMenu instanceof CuriosContainer container) {
                container.resetPage(player);

                // 客户端需要再次更新，否则可能会触发增减槽位不更新问题
                if (entity.level.isClientSide) {
                    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CuriosCompat::clientResetPage);
                }
            }
        }
    }
}

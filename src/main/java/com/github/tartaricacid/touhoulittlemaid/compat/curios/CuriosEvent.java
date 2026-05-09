package com.github.tartaricacid.touhoulittlemaid.compat.curios;


import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTombstoneEvent;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.menu.CuriosContainer;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.MaidContainerCache;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.SlotModifiersUpdatedEvent;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CuriosEvent {
    /**
     * 当添加可以修改槽位数量的饰品时，重置饰品容器
     */
    @SubscribeEvent
    public void onSlotUpdate(SlotModifiersUpdatedEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof EntityMaid maid && maid.getOwner() instanceof Player player) {
            MaidContainerCache.invalidate(maid);
            if (player.containerMenu instanceof CuriosContainer container) {
                container.resetPage(player);

                // 客户端需要再次更新，否则可能会触发增减槽位不更新问题
                if (entity.level.isClientSide) {
                    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CuriosCompat::clientResetPage);
                }
            }
        }
    }

    /**
     * 当女仆墓碑生成时，将 Curios 饰品从女仆身上转移到墓碑中
     * Curios 后续的掉落事件仍然会触发，但此时女仆身上已经没有饰品了
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMaidTombstone(MaidTombstoneEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (!CuriosCompat.isLoadedOrEnable()) {
            return;
        }

        EntityTombstone tombstone = event.getTombstone();
        EntityMaid maid = event.getMaid();

        CuriosApi.getCuriosInventory(maid).ifPresent(handler -> {
            var values = handler.getCurios().values();
            for (ICurioStacksHandler stacksHandler : values) {
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.extractItem(i, stacks.getSlotLimit(i), false);
                    if (!stack.isEmpty()) {
                        tombstone.insertItem(stack);
                    }
                }
            }
        });
    }
}

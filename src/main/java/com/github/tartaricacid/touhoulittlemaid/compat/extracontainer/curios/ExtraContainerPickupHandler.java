package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidPickupEvent;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.MaidContainerCache;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 允许女仆在拾取物品时放入 Curios 槽位中的背包
 * <p>
 * 拾取优先级：
 * 1. 优先放入已有相同物品的容器（物品栏 > back背包 > 其他背包，按优先级排序）
 * 2. 如果没有已有该物品的容器能放下，则按默认顺序依次尝试
 */
public class ExtraContainerPickupHandler {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onMaidPickupPre(MaidPickupEvent.ItemResultPre event) {
        EntityMaid maid = event.getMaid();
        ItemEntity itemEntity = event.getEntityItem();
        boolean simulate = event.isSimulate();

        if (!tryPickup(maid, itemEntity, simulate)) {
            return;
        }

        event.setCanPickup(true);
        event.setCanceled(true);
    }

    private boolean tryPickup(EntityMaid maid, ItemEntity itemEntity, boolean simulate) {
        if (maid.level.isClientSide || !itemEntity.isAlive() || itemEntity.hasPickUpDelay()) {
            return false;
        }
        ItemStack itemStack = itemEntity.getItem();
        if (!EntityMaid.canInsertItem(itemStack)) {
            return false;
        }

        int originCount = itemStack.getCount();
        var containers = MaidContainerCache.getContainers(maid);

        for (var container : containers) {
            if (!container.containing(maid, itemStack)) {
                continue;
            }
            itemStack = container.insert(maid, itemStack, simulate);
            if (itemStack.isEmpty()) {
                break;
            }
        }

        if (!itemStack.isEmpty()) {
            for (var container : containers) {
                itemStack = container.insert(maid, itemStack, simulate);
                if (itemStack.isEmpty()) {
                    break;
                }
            }
        }

        if (originCount == itemStack.getCount()) {
            return false;
        }
        if (!simulate) {
            handlePickupEffects(maid, itemEntity, itemStack, originCount);
        }
        return true;
    }

    private void handlePickupEffects(EntityMaid maid, ItemEntity itemEntity, ItemStack remaining, int originCount) {
        int pickedCount = originCount - remaining.getCount();
        maid.take(itemEntity, pickedCount);
        maid.tryPlayMaidPickupSound();

        ItemStack pickedStack = new ItemStack(itemEntity.getItem().getItem(), pickedCount);
        MinecraftForge.EVENT_BUS.post(new MaidPickupEvent.ItemResultPost(maid, pickedStack));

        if (remaining.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(remaining);
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidPickupEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidPlaySoundEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * 允许女仆在拾取物品时放入 Curios 槽位中的背包
 * 
 * 拾取优先级：
 * 1. 优先放入已有相同物品的容器（物品栏 > back背包 > 其他背包，按优先级排序）
 * 2. 如果没有已有该物品的容器能放下，则按默认顺序依次尝试
 */
public class BackpackPickupEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onMaidPickupPre(MaidPickupEvent.ItemResultPre event) {
        EntityMaid maid = event.getMaid();
        ItemEntity itemEntity = event.getEntityItem();
        boolean simulate = event.isSimulate();

        if (!tryPickup(maid, itemEntity, simulate)) return;
        
        event.setCanPickup(true);
        event.setCanceled(true);
    }

    private boolean tryPickup(EntityMaid maid, ItemEntity itemEntity, boolean simulate) {
        if (maid.level().isClientSide) return false;
        if (!itemEntity.isAlive()) return false;
        if (itemEntity.hasPickUpDelay()) return false;

        ItemStack itemstack = itemEntity.getItem();
        if (!EntityMaid.canInsertItem(itemstack)) return false;

        int oriCnt = itemstack.getCount();
        List<MaidBackpackCache.ContainerRef> containers = MaidBackpackCache.getContainers(maid);

        for (MaidBackpackCache.ContainerRef container : containers) {
            if (!container.containing(itemstack)) continue;

            itemstack = container.insert(itemstack, simulate);
            if (itemstack.isEmpty()) break;
        }

        if (!itemstack.isEmpty())
            for (MaidBackpackCache.ContainerRef container : containers)
            {
                itemstack = container.insert(itemstack, simulate);
                if (itemstack.isEmpty()) break;
            }

        if (oriCnt == itemstack.getCount()) return false;
        if (!simulate) handlePickupEffects(maid, itemEntity, itemstack, oriCnt);
        return true;
    }

    private void handlePickupEffects(EntityMaid maid, ItemEntity itemEntity, ItemStack remaining, int oriCnt) {
        int pickedCount = oriCnt - remaining.getCount();
        maid.take(itemEntity, pickedCount);

        if (!MinecraftForge.EVENT_BUS.post(new MaidPlaySoundEvent(maid)))
            maid.playSound(InitSounds.MAID_ITEM_GET.get(), 1, 1);

        ItemStack pickedStack = new ItemStack(itemEntity.getItem().getItem(), pickedCount);
        MinecraftForge.EVENT_BUS.post(new MaidPickupEvent.ItemResultPost(maid, pickedStack));

        if (remaining.isEmpty()) itemEntity.discard();
        else itemEntity.setItem(remaining);
    }
}

package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidFavorabilityLevelChangeEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class MaidDropBaubleEvent {
    /**
     * 当女仆好感度降级时，需要移除超出上限的饰品
     */
    @SubscribeEvent
    public static void onFavorabilityLevelChange(MaidFavorabilityLevelChangeEvent event) {
        int newLevel = event.getNewLevel();
        EntityMaid maid = event.getMaid();
        // 3 级：不需要掉落
        if (newLevel >= 3) {
            return;
        }
        // 2 级：20 个格子
        // 0 和 1 级：10 个格子
        int startIndex = newLevel <= 1 ? 10 : 20;
        BaubleItemHandler maidBauble = maid.getMaidBauble();
        for (int i = startIndex; i < maidBauble.getSlots(); i++) {
            ItemStack drop = maidBauble.extractItem(i, 1, false);
            maid.spawnAtLocation(drop);
        }
    }
}

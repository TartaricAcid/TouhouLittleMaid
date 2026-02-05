package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.SBackpackCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class BackpackCuriosEquipEventHandler {

    @SubscribeEvent
    public void onCurioChange(CurioChangeEvent event) {
        if (!(event.getEntity() instanceof EntityMaid maid)) return;

        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        String slotType = event.getIdentifier();
        int slotIndex = event.getSlotIndex();

        boolean wasBackpack = SBackpackCompat.isBackpack(from);
        boolean isBackpack = SBackpackCompat.isBackpack(to);

        if (wasBackpack && !isBackpack) MaidBackpackCache.onUnequipped(maid, slotType, slotIndex);
        else if (!wasBackpack && isBackpack) MaidBackpackCache.onEquipped(maid, slotType, slotIndex);
        // 如果背包被替换为另一个背包，槽位引用不变，不需要更新
    }
}

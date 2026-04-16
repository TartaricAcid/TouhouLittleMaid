package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios;

import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.ExtraContainerManager;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.MaidContainerCache;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class ExtraContainerEquipHandler {
    @SubscribeEvent
    public void onCurioChange(CurioChangeEvent event) {
        if (!(event.getEntity() instanceof EntityMaid maid)) {
            return;
        }

        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        String slotType = event.getIdentifier();
        int slotIndex = event.getSlotIndex();

        boolean wasBackpack = ExtraContainerManager.isAnyBackpack(from);
        boolean isBackpack = ExtraContainerManager.isAnyBackpack(to);

        if (wasBackpack && !isBackpack) {
            MaidContainerCache.onUnequipped(maid, slotType, slotIndex);
        } else if (!wasBackpack && isBackpack) {
            MaidContainerCache.onEquipped(maid, to, slotType, slotIndex);
        }
    }
}

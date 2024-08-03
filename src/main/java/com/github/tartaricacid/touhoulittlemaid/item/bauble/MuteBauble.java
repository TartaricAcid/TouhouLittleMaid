package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidPlaySoundEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;

public class MuteBauble implements IMaidBauble {
    public MuteBauble() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidPlaySoundEvent event) {
        EntityMaid maid = event.getMaid();
        int slot = ItemsUtil.getBaubleSlotInMaid(maid, this);
        if (slot >= 0) {
            event.setCanceled(true);
        }
    }

    @Override
    public String getChatBubbleId() {
        return "mute";
    }
}

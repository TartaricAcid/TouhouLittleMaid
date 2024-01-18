package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ApplyScriptBook {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        EntityMaid maid = event.getMaid();
        Player player = event.getPlayer();

        if (stack.getItem() == Items.WRITABLE_BOOK || stack.getItem() == Items.WRITTEN_BOOK) {
            if (player.isDiscrete()) {
                maid.getScriptBookManager().removeScript();
            } else {
                boolean installSuccess = maid.getScriptBookManager().installScript(stack);
                if (!installSuccess) {
                    maid.getScriptBookManager().copyScript(stack);
                }
            }
            event.setCanceled(true);
        }
    }
}

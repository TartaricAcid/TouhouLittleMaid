package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class ApplyScriptBook {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        EntityMaid maid = event.getMaid();
        Player player = event.getPlayer();

        if (stack.getItem() == Items.WRITABLE_BOOK || stack.getItem() == Items.WRITTEN_BOOK) {
            event.setCanceled(true);
            
            // 清除台本
            if (player.isDiscrete()) {
                maid.getScriptBookManager().removeScript();
                if (!maid.level.isClientSide) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.script_book.remove"));
                }
                return;
            }
            
            // 安装台本
            boolean installSuccess = maid.getScriptBookManager().installScript(stack);
            if (installSuccess) {
                if (!maid.level.isClientSide) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.script_book.install"));
                }
                return;
            }
            
            // 复制台本
            if (stack.getItem() == Items.WRITABLE_BOOK) {
                boolean copySuccess = maid.getScriptBookManager().copyScript(stack);
                if (copySuccess && !maid.level.isClientSide) {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.script_book.copy"));
                }
            }
        }
    }
}

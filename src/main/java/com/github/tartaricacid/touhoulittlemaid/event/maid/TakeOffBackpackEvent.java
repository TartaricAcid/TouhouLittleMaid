package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber
public class TakeOffBackpackEvent {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        int backpackLevel = maid.getBackpackLevel();

        if (backpackLevel != BackpackLevel.EMPTY && stack.is(Tags.Items.SHEARS)) {
            if (maid.backpackHasDelay()) {
                return;
            }
            maid.setBackpackLevel(BackpackLevel.EMPTY);
            maid.setBackpackDelay();
            player.getCooldowns().addCooldown(stack.getItem(), 20);
            stack.hurtAndBreak(1, player, m -> m.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            maid.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
            ItemMaidBackpack.getInstance(backpackLevel).ifPresent(backpack -> ItemHandlerHelper.giveItemToPlayer(player, backpack.getDefaultInstance()));
            int minIndex = BackpackLevel.BACKPACK_CAPACITY_MAP.get(BackpackLevel.EMPTY);
            int maxIndex = BackpackLevel.BACKPACK_CAPACITY_MAP.get(BackpackLevel.BIG);
            ItemsUtil.dropEntityItems(maid, maid.getMaidInv(), minIndex, maxIndex);
            event.setCanceled(true);
        }
    }
}

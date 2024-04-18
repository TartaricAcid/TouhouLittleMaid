package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber
public class HandleBackpackEvent {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        IMaidBackpack maidBackpack = maid.getMaidBackpackType();
        if (stack.is(Tags.Items.SHEARS)) {
            if (maid.isOwnedBy(player) && !maid.backpackHasDelay() && maidBackpack != BackpackManager.getEmptyBackpack()) {
                maid.setBackpackDelay();
                player.getCooldowns().addCooldown(stack.getItem(), 20);
                ItemHandlerHelper.giveItemToPlayer(player, maidBackpack.getTakeOffItemStack(stack, player, maid));
                maidBackpack.onTakeOff(stack, player, maid);
                maid.setMaidBackpackType(BackpackManager.getEmptyBackpack());
                stack.hurtAndBreak(1, player, m -> m.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                maid.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
                event.setCanceled(true);
            }
        } else {
            BackpackManager.findBackpack(stack).ifPresent(backpack -> {
                if (maid.isOwnedBy(player) && !maid.backpackHasDelay() && backpack != BackpackManager.getEmptyBackpack() && backpack != maidBackpack) {
                    maid.setBackpackDelay();
                    BackpackManager.addBackpackCooldown(player);
                    ItemHandlerHelper.giveItemToPlayer(player, maidBackpack.getTakeOffItemStack(stack, player, maid));
                    maidBackpack.onTakeOff(stack, player, maid);
                    maid.setMaidBackpackType(backpack);
                    stack.shrink(1);
                    backpack.onPutOn(stack, player, maid);
                    maid.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
                    event.setCanceled(true);
                }
            });
        }
    }
}

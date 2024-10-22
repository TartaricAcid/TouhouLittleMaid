package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemHandlerHelper;

@EventBusSubscriber
public class HandleBackpackEvent {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        IMaidBackpack maidBackpack = maid.getMaidBackpackType();
        if (stack.is(Tags.Items.TOOLS_SHEAR)) {
            if (maid.isOwnedBy(player) && !maid.backpackHasDelay() && maidBackpack != BackpackManager.getEmptyBackpack()) {
                maid.setBackpackDelay();
                player.getCooldowns().addCooldown(stack.getItem(), 20);
                ItemHandlerHelper.giveItemToPlayer(player, maidBackpack.getTakeOffItemStack(stack, player, maid));
                maidBackpack.onTakeOff(stack, player, maid);
                maid.setMaidBackpackType(BackpackManager.getEmptyBackpack());
                stack.hurtAndBreak(1, player, event.getPlayer().getEquipmentSlotForItem(stack));
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
                    backpack.onPutOn(stack, player, maid);
                    stack.shrink(1);
                    maid.playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
                    if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                        InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.MAID_BACKPACK);
                    }
                    event.setCanceled(true);
                }
            });
        }
    }
}

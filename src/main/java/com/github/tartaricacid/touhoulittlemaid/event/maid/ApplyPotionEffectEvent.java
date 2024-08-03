package com.github.tartaricacid.touhoulittlemaid.event.maid;


import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.items.ItemHandlerHelper;

@EventBusSubscriber
public final class ApplyPotionEffectEvent {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        EntityMaid maid = event.getMaid();
        Level world = event.getWorld();
        Player player = event.getPlayer();

        if (player.isDiscrete() && stack.getItem() == Items.POTION) {
            stack.getItem().finishUsingItem(stack.copy(), world, maid);
            if (!player.isCreative()) {
                stack.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_BOTTLE));
            }
            maid.playSound(SoundEvents.GENERIC_DRINK, 0.6f, 0.8F + world.random.nextFloat() * 0.4F);
            event.setCanceled(true);
        }

        if (player.isDiscrete() && stack.getItem() == Items.MILK_BUCKET) {
            maid.curePotionEffects(stack);
            if (!player.isCreative()) {
                stack.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.BUCKET));
            }
            maid.playSound(SoundEvents.GENERIC_DRINK, 0.6f, 0.8F + world.random.nextFloat() * 0.4F);
            event.setCanceled(true);
        }
    }
}

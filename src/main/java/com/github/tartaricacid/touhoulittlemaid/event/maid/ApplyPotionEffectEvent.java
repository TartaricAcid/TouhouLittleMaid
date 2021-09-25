package com.github.tartaricacid.touhoulittlemaid.event.maid;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.event.api.InteractMaidEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

@Mod.EventBusSubscriber
public final class ApplyPotionEffectEvent {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        EntityMaid maid = event.getMaid();
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();

        if (stack.getItem() == Items.POTION) {
            stack.getItem().finishUsingItem(stack.copy(), world, maid);
            if (!player.isCreative()) {
                stack.shrink(1);
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_BOTTLE));
            }
            maid.playSound(SoundEvents.GENERIC_DRINK, 0.6f, 0.8F + world.random.nextFloat() * 0.4F);
            event.setCanceled(true);
        }

        if (stack.getItem() == Items.MILK_BUCKET) {
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

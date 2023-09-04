package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemSmartSlab;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class SlabClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        ItemStack stack = event.getStack();
        Item emptySmartSlab = InitItems.SMART_SLAB_EMPTY.get();
        Item maidSmartSlab = InitItems.SMART_SLAB_HAS_MAID.get();
        if (stack.getItem() == emptySmartSlab) {
            if (!player.getCooldowns().isOnCooldown(emptySmartSlab)) {
                ItemStack output = maidSmartSlab.getDefaultInstance();
                ItemSmartSlab.storeMaidData(output, maid);
                if (maid.hasCustomName()) {
                    output.setHoverName(maid.getCustomName());
                }
                maid.spawnExplosionParticle();
                maid.discard();
                maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, maid.level.random.nextFloat() * 0.1F + 0.9F);
                player.setItemInHand(InteractionHand.MAIN_HAND, output);
                player.getCooldowns().addCooldown(maidSmartSlab, 20);
            }
            event.setCanceled(true);
        }
    }
}

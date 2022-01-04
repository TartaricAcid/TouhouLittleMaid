package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemSmartSlab;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class SlabClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();
        ItemStack stack = event.getStack();
        if (stack.getItem() == InitItems.SMART_SLAB_EMPTY.get()) {
            ItemStack output = InitItems.SMART_SLAB_HAS_MAID.get().getDefaultInstance();
            ItemSmartSlab.storeMaidData(output, maid);
            if (maid.hasCustomName()) {
                output.setHoverName(maid.getCustomName());
            }
            maid.spawnExplosionParticle();
            maid.remove();
            maid.playSound(SoundEvents.PLAYER_SPLASH, 1.0F, maid.level.random.nextFloat() * 0.1F + 0.9F);
            player.setItemInHand(Hand.MAIN_HAND, output);
            event.setCanceled(true);
        }
    }
}

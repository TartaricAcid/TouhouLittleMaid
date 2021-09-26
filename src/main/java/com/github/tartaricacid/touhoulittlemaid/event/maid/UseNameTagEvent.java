package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.item.NameTagGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class UseNameTagEvent {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onInteractClient(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (!stack.hasCustomHoverName() && player.getMainHandItem().getItem() == Items.NAME_TAG && player.equals(maid.getOwner())) {
            Minecraft.getInstance().setScreen(new NameTagGui(maid));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onInteractServer(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        PlayerEntity player = event.getPlayer();
        EntityMaid maid = event.getMaid();

        if (!stack.hasCustomHoverName() && player.getMainHandItem().getItem() == Items.NAME_TAG && player.equals(maid.getOwner())) {
            event.setCanceled(true);
        }
    }
}

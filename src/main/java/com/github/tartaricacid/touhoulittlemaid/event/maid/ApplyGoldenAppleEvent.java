package com.github.tartaricacid.touhoulittlemaid.event.maid;


import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public final class ApplyGoldenAppleEvent {
    @SubscribeEvent
    public static void onInteractMaid(InteractMaidEvent event) {
        ItemStack stack = event.getStack();
        EntityMaid maid = event.getMaid();
        Level world = event.getWorld();
        FoodProperties food = stack.getItem().getFoodProperties(stack, maid);
        Player player = event.getPlayer();

        if (player.isDiscrete() && (food == Foods.GOLDEN_APPLE || food == Foods.ENCHANTED_GOLDEN_APPLE)) {
            maid.eat(world, stack);
            event.setCanceled(true);
        }
    }
}

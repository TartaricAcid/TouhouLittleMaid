package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class ApplyPotionEffectEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (itemstack.getItem() == Items.POTIONITEM) {
            if (player.capabilities.isCreativeMode) {
                itemstack.getItem().onItemUseFinish(itemstack.copy(), world, maid);
            } else {
                player.setHeldItem(EnumHand.MAIN_HAND, itemstack.getItem().onItemUseFinish(itemstack, world, maid));
            }
            maid.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.6f, 0.8F + world.rand.nextFloat() * 0.4F);
            event.setCanceled(true);
        }
    }
}

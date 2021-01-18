package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.EventType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.favorability.FavorabilityEvent;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class UseFavorabilityToolEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();
        World world = event.getWorld();

        if (itemstack.getItem() == MaidItems.CREATIVE_FAVORABILITY_TOOL && maid instanceof EntityMaid) {
            MinecraftForge.EVENT_BUS.post(new FavorabilityEvent(EventType.USE_CREATIVE_TOOL, (EntityMaid) maid));
            // 物品消耗判定
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            // 播放音效
            maid.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            event.setCanceled(true);
        }
    }
}

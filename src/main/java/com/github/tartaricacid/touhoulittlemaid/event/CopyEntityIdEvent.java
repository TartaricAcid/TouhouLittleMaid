package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class CopyEntityIdEvent {
    @SubscribeEvent
    public static void copyEntityId(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        Entity target = event.getTarget();
        if (player.getItemInHand(hand).is(InitItems.ENTITY_ID_COPY.get())) {
            if (player.level.isClientSide && FMLEnvironment.dist == Dist.CLIENT) {
                copyEntityId(player, target);
            }
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void copyEntityId(Player player, Entity target) {
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
        if (key == null) {
            return;
        }
        Minecraft.getInstance().keyboardHandler.setClipboard(key.toString());
        player.sendSystemMessage(Component.translatable("message.touhou_little_maid.entity_id_copy.copy", key.toString()));
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class ShowPowerEvent {
    private static ItemStack POWER_POINT;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() != InitItems.HAKUREI_GOHEI.get()) {
                return;
            }
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            FontRenderer fontRenderer = Minecraft.getInstance().font;
            if (POWER_POINT == null) {
                POWER_POINT = InitItems.POWER_POINT.get().getDefaultInstance();
            }
            itemRenderer.renderGuiItem(POWER_POINT, 5, 5);
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(cap ->
                    fontRenderer.draw(event.getMatrixStack(), String.format("%s×%.2f", TextFormatting.BOLD, cap.get()), 20, 10, 0xffffff));
            RenderSystem.enableBlend();
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Dist.CLIENT)
public final class ShowPowerEvent {
    private static ItemStack POWER_POINT;

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (!ItemHakureiGohei.isGohei(stack)) {
                return;
            }
            GuiGraphics graphics = event.getGuiGraphics();
            Font font = Minecraft.getInstance().font;
            if (POWER_POINT == null) {
                POWER_POINT = InitItems.POWER_POINT.get().getDefaultInstance();
            }
            graphics.renderItem(POWER_POINT, 5, 5);
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(cap -> graphics.drawString(font, String.format("%s×%.2f", ChatFormatting.BOLD, cap.get()), 20, 10, 0xffffff));
            RenderSystem.enableBlend();
        }
    }
}

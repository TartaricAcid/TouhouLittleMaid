package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/9/12 18:10
 **/
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class DrawPowerOverlayEvent {
    private static final ResourceLocation POWER_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
            PowerHandler powerHandler = Minecraft.getMinecraft().player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
            if (stack.getItem() == MaidItems.HAKUREI_GOHEI && powerHandler != null) {
                int x = (int) (GeneralConfig.MISC_CONFIG.PowerPointHudX * Minecraft.getMinecraft().displayWidth);
                int y = (int) (GeneralConfig.MISC_CONFIG.PowerPointHudY * Minecraft.getMinecraft().displayHeight);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.translate(x, y, 0);
                GlStateManager.scale(0.8, 0.8, 0.8f);
                Minecraft.getMinecraft().renderEngine.bindTexture(POWER_TEXTURE);
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 32, 0, 16, 16, 64, 64);
                GlStateManager.popMatrix();
                Minecraft.getMinecraft().fontRenderer.drawString(String.format("%s×%.2f", TextFormatting.BOLD, powerHandler.get()), 13 + x, 2 + y, 0xffffff, true);
            }
        }
    }
}

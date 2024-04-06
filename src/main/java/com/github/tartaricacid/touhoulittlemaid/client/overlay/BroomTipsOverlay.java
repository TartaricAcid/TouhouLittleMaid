package com.github.tartaricacid.touhoulittlemaid.client.overlay;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

import static net.minecraft.client.gui.GuiComponent.blit;


public class BroomTipsOverlay implements IGuiOverlay {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        Options options = minecraft.options;
        if (options.hideGui) {
            return;
        }
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof EntityBroom broom && !broom.hasPassenger(e -> e instanceof EntityMaid)) {
            gui.setupOverlayRenderState(true, false);
            Component tip = Component.translatable("message.touhou_little_maid.broom.unable_fly");
            List<FormattedCharSequence> split = minecraft.font.split(tip, 150);
            int offset = (screenHeight / 2 - 5) - split.size() * 10;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BG);
            RenderSystem.enableDepthTest();
            blit(poseStack, screenWidth / 2 - 8, offset - 2, 48, 16, 16, 16, 256, 256);
            offset += 18;
            for (FormattedCharSequence sequence : split) {
                int width = minecraft.font.width(sequence);
                minecraft.font.drawShadow(poseStack, sequence, (screenWidth - width) / 2f, offset, 0xFFFFFF);
                offset += 10;
            }
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.util.GuiTools;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HistoryChatWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/gui/maid_history_chat.png");
    private static final long TICKS_PER_DAY = 24000;
    private static final long TICKS_PER_HOUR = 1000;

    private final boolean isLeft;
    private final ResourceLocation playerSkin;
    private final Component time;

    public HistoryChatWidget(int pX, int pY, int width, int height, Component message, ResourceLocation playerSkin, long gameTime, boolean isLeft) {
        super(pX, pY, width, height, message);
        this.isLeft = isLeft;
        this.playerSkin = playerSkin;
        this.time = convertGameTime(gameTime);
    }

    private Component convertGameTime(long inputGameTime) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return Component.empty();
        }
        long currentGameTime = level.getGameTime();
        long diff = currentGameTime - inputGameTime;
        if (diff < 0) {
            return Component.empty();
        }

        long days = diff / TICKS_PER_DAY;
        diff %= TICKS_PER_DAY;
        long hours = diff / TICKS_PER_HOUR;
        if (days > 0) {
            return Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.days", days);
        } else if (hours > 0) {
            return Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.hours", hours);
        } else {
            return Component.translatable("gui.touhou_little_maid.button.maid_ai_chat_config.history_chat.just_now");
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.setColor(1, 1, 1, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        this.drawBackground(graphics);
        this.drawAvatar(graphics);
        this.renderString(graphics, Minecraft.getInstance().font);
    }

    private void drawAvatar(GuiGraphics graphics) {
        int size = 16;
        int offset = 6;
        int xOffset = this.isLeft ? (-size - offset) : this.getWidth() + offset;
        if (isLeft) {
            graphics.blit(TEXTURE, this.getX() + xOffset, this.getHeightMiddle(size), 0, 32, size, size);
        } else {
            PlayerFaceRenderer.draw(graphics, this.playerSkin, this.getX() + xOffset, this.getHeightMiddle(size), size);
        }
    }

    private void drawBackground(GuiGraphics graphics) {
        int heightMiddle = this.getHeightMiddle(14);
        GuiTools.blitNineSliced(graphics, TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                8, 4, 100, 16, 0, this.getTextureY());
        if (isLeft) {
            graphics.blit(TEXTURE, this.getX() - 4, heightMiddle, 100, 16, 6, 14);
        } else {
            graphics.blit(TEXTURE, this.getX() + this.getWidth() - 2, heightMiddle, 100, 0, 6, 14);
        }
    }

    public void renderString(GuiGraphics graphics, Font font) {
        Component message = this.getMessage();
        graphics.setColor(1, 1, 1, 1);
        if (isLeft) {
            graphics.drawWordWrap(font, message, this.getX() + 5, this.getY() + 5, this.getWidth() - 10, 0x555555);
        } else {
            graphics.drawWordWrap(font, message, this.getX() + 5, this.getY() + 5, this.getWidth() - 10, 0xFFFFFF);
        }

        float scale = 0.5f;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 0);
        if (isLeft) {
            graphics.drawString(font, this.time.getVisualOrderText(),
                    (this.getX() + 2) / scale,
                    (this.getY() - 5) / scale,
                    0x999999, false);
        } else {
            float width = font.width(this.time) * scale;
            graphics.drawString(font, this.time.getVisualOrderText(),
                    (this.getX() + this.getWidth() - width - 2) / scale,
                    (this.getY() - 5) / scale,
                    0x999999, false);
        }
        graphics.pose().popPose();
    }

    private int getTextureY() {
        return this.isLeft ? 16 : 0;
    }

    private int getHeightMiddle(int height) {
        return this.getY() + (this.getHeight() - height) / 2;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}

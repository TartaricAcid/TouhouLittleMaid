package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
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
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class HistoryChatWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_history_chat.png");
    private static final long TICKS_PER_DAY = 24000;
    private static final long TICKS_PER_HOUR = 1000;

    /**
     * 普通的 LLM 返回的聊天消息
     */
    private final boolean isLeft;
    /**
     * 工具调用等类似于系统消息的内容
     */
    private final boolean isTool;

    private final ResourceLocation playerSkin;
    private final Component time;

    public HistoryChatWidget(int pX, int pY, int width, int height, Component message, ResourceLocation playerSkin,
                             long gameTime, boolean isLeft, boolean isTool) {
        super(pX, pY, width, height, message);
        this.isLeft = isLeft;
        this.isTool = isTool;
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
        if (this.isTool) {
            // 工具消息只渲染文本
            this.renderToolText(graphics, Minecraft.getInstance().font);
        } else {
            // 普通消息渲染头像、背景和文本
            this.drawBackground(graphics);
            this.drawAvatar(graphics);
            this.renderString(graphics, Minecraft.getInstance().font);
        }
    }

    private void renderToolText(GuiGraphics graphics, Font font) {
        float scale = 0.5f;
        int width = (int) (this.getWidth() / scale);
        float posX = this.getX() / scale + width / 2f;
        float posY = this.getY() / scale;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1);

        List<FormattedCharSequence> lines = font.split(this.getMessage(), width);

        for (int i = 0; i < lines.size(); i++) {
            graphics.drawCenteredString(font, lines.get(i), (int) posX, (int) posY + i * font.lineHeight, 0x999999);
        }

        graphics.pose().popPose();
    }

    private void drawAvatar(GuiGraphics graphics) {
        int size = 16;
        int offset = 6;
        int xOffset = this.isLeft ? (-size - offset) : this.getWidth() + offset;
        if (isLeft) {
            graphics.blit(TEXTURE, this.getX() + xOffset, this.getHeightMiddle(size), 0, 16, size, size, 128, 128);
        } else {
            PlayerFaceRenderer.draw(graphics, this.playerSkin, this.getX() + xOffset, this.getHeightMiddle(size), size);
        }
    }

    private void drawBackground(GuiGraphics graphics) {
        int heightMiddle = this.getHeightMiddle(14);
        graphics.blitNineSliced(TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
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

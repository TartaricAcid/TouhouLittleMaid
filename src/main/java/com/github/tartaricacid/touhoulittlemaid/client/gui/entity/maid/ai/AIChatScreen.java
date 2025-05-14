package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.GetMaidAIDataMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendUserChatMessage;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AIChatScreen extends Screen {
    private final EntityMaid maid;
    private EditBox input;
    private FlatColorButton configButton;
    private int tickCounter = 0;

    public AIChatScreen(EntityMaid maid) {
        super(Component.literal("Maid AI Chat Screen"));
        this.maid = maid;
    }

    @Override
    protected void init() {
        int posX = this.width / 2;
        int posY = this.height / 2;

        this.input = new EditBox(this.getMinecraft().fontFilterFishy, posX - 165, posY + 64,
                300, 20, Component.translatable("chat.editBox"));
        this.input.setMaxLength(128);
        this.input.setBordered(false);
        this.input.setValue("");
        this.input.setCanLoseFocus(false);
        this.addWidget(this.input);
        this.setInitialFocus(this.input);

        this.configButton = new FlatColorButton(posX + 142, posY + 58, 20, 20, Component.literal("✎"),
                b -> NetworkHandler.CHANNEL.sendToServer(new GetMaidAIDataMessage(this.maid.getId())))
                .setTooltips("ai.touhou_little_maid.chat.config.tip");
        this.addRenderableWidget(this.configButton);
    }

    @Override
    public void resize(Minecraft mc, int pWidth, int pHeight) {
        String chatText = this.input.getValue();
        super.resize(mc, pWidth, pHeight);
        this.input.setValue(chatText);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int xOffset = 5;
        int yOffset = 6;
        graphics.fill(input.getX() - xOffset,
                input.getY() - yOffset,
                input.getX() + input.getInnerWidth() + xOffset,
                input.getY() + input.getHeight() - yOffset,
                0x9f_000000);
        input.render(graphics, mouseX, mouseY, partialTicks);

        if (StringUtils.isEmpty(input.getValue())) {
            MutableComponent text = Component.translatable("ai.touhou_little_maid.chat.input.tip").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
            int xPos = input.getX() + input.getInnerWidth() / 2 - xOffset;
            graphics.drawCenteredString(font, text, xPos, input.getY(), 0xFFFFFF);
        }

        super.render(graphics, mouseX, mouseY, partialTicks);
        this.configButton.renderToolTip(graphics, this, mouseX, mouseY);
    }

    @Override
    public void tick() {
        this.input.tick();
        tickCounter++;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.input.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.input);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        // GUI 刚打开的 5 tick 内，不允许输入，否则会把按键录入
        if (tickCounter < 5) {
            return false;
        }
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    protected void insertText(String text, boolean overwrite) {
        if (overwrite) {
            this.input.setValue(text);
        } else {
            this.input.insertText(text);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            sendDoneMessage();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void sendDoneMessage() {
        String value = input.getValue();
        LocalPlayer player = this.getMinecraft().player;
        if (StringUtils.isNotBlank(value) && player != null) {
            LanguageManager languageManager = Minecraft.getInstance().getLanguageManager();
            LanguageInfo info = languageManager.getLanguage(languageManager.getSelected());
            String language;
            if (info != null) {
                language = info.toComponent().getString();
            } else {
                language = "English (US)";
            }
            NetworkHandler.CHANNEL.sendToServer(new SendUserChatMessage(this.maid.getId(), value, language));
            String name = player.getScoreboardName();
            String format = String.format("<%s> %s", name, value);
            player.sendSystemMessage(Component.literal(format).withStyle(ChatFormatting.GRAY));
        }
        this.onClose();
    }
}

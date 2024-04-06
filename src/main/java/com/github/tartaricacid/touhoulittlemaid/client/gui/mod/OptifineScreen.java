package com.github.tartaricacid.touhoulittlemaid.client.gui.mod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;

/**
 * Refer: https://github.com/TeamTwilight/twilightforest/blob/1.20.x/src/main/java/twilightforest/client/OptifineWarningScreen.java
 */
public class OptifineScreen extends Screen {
    public final Screen lastScreen;
    private final MutableComponent text = Component.translatable("gui.touhou_little_maid.optifine_warning.text");
    private final String embeddiumUrl = "https://www.curseforge.com/minecraft/mc-mods/embeddium";
    private final String oculusUrl = "https://www.curseforge.com/minecraft/mc-mods/oculus";
    private Button exitButton;
    private int ticksUntilEnable = 20 * 10;
    private MultiLineLabel message = MultiLineLabel.EMPTY;

    public OptifineScreen(Screen lastScreen) {
        super(Component.translatable("gui.touhou_little_maid.optifine_warning.title").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE));
        this.lastScreen = lastScreen;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), text);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height * 3 / 4 - 15, 150, 20, Component.translatable("gui.touhou_little_maid.optifine_warning.embeddium"), b -> openUrl(embeddiumUrl)));
        this.addRenderableWidget(new Button(this.width / 2 + 5, this.height * 3 / 4 - 15, 150, 20, Component.translatable("gui.touhou_little_maid.optifine_warning.oculus"), b -> openUrl(oculusUrl)));
        this.exitButton = this.addRenderableWidget(new Button(this.width / 2 - 75, this.height * 3 / 4 + 25, 150, 20, CommonComponents.GUI_PROCEED, (pressed) -> Minecraft.getInstance().setScreen(this.lastScreen)));
        this.exitButton.active = false;
        this.message = MultiLineLabel.create(this.font, text, this.width - 50);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 30, 16777215);
        this.message.renderCentered(poseStack, this.width / 2, 70);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (--this.ticksUntilEnable <= 0) {
            this.exitButton.active = true;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.ticksUntilEnable <= 0;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.lastScreen);
    }

    private void openUrl(String url) {
        if (StringUtils.isNotBlank(url) && minecraft != null) {
            minecraft.setScreen(new ConfirmLinkScreen(yes -> {
                if (yes) {
                    Util.getPlatform().openUri(url);
                }
                minecraft.setScreen(this);
            }, url, true));
        }
    }
}

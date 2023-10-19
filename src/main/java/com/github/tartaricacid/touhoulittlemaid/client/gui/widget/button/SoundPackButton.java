package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SoundPackButton extends FlatColorButton {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_custom_sound.png");
    private final SoundPackInfo info;

    public SoundPackButton(int pX, int pY, SoundPackInfo info, OnPress onPress) {
        super(pX, pY, 230, 43, Component.empty(), onPress);
        this.info = info;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pPartialTick) {
        super.renderWidget(graphics, mouseX, mouseY, pPartialTick);
        ResourceLocation icon = info.getIcon();
        if (icon == null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ICON);
            graphics.blit(ICON, this.getX() + 4, this.getY() + 4, 0, 16, 32, 32, 256, 256);
        }
    }

    @Override
    @SuppressWarnings("all")
    public void renderString(GuiGraphics graphics, Font font, int pColor) {
        int startX = this.getX() + 42;
        int startY = this.getY() + 7;

        MutableComponent packName = ParseI18n.parse(info.getPackName());
        String version = info.getVersion();
        List<String> author = info.getAuthor();
        String date = info.getDate();

        graphics.drawString(font, packName, startX, startY, ChatFormatting.WHITE.getColor());

        if (StringUtils.isNotBlank(version)) {
            int titleWidth = font.width(packName);
            graphics.drawString(font, "§nv" + version, startX + titleWidth + 5, startY, ChatFormatting.AQUA.getColor());
        }

        if (!author.isEmpty()) {
            startY += 10;
            String authorListText = StringUtils.joinWith(I18n.get("gui.touhou_little_maid.resources_download.author.delimiter"), author);
            String authorText = I18n.get("gui.touhou_little_maid.resources_download.author", authorListText);
            graphics.drawString(font, authorText, startX, startY, ChatFormatting.GOLD.getColor());
        }

        if (StringUtils.isNotBlank(date)) {
            startY += 10;
            MutableComponent dateText = Component.translatable("gui.touhou_little_maid.skin.text.date", date);
            graphics.drawString(font, dateText, startX, startY, ChatFormatting.GREEN.getColor());
        }
    }
}

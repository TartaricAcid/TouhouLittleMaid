package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.SizeTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SoundPackButton extends FlatColorButton {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_custom_sound.png");
    private final SoundPackInfo info;
    private boolean isUse = false;

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
            graphics.blit(ICON, this.getX() + 4, this.getY() + 5, 0, 16, 32, 32, 256, 256);
        } else {
            if (info.getIconAnimation() == CustomModelPack.AnimationState.UNCHECK) {
                checkIconAnimation(info, icon);
            }
            if (info.getIconAnimation() == CustomModelPack.AnimationState.FALSE) {
                graphics.blit(icon, this.getX() + 4, this.getY() + 5,
                        0, 0, 32, 32, 32, 32);
            } else {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, icon);
                int time = getTickTime() / info.getIconDelay();
                int iconIndex = time % info.getIconAspectRatio();
                graphics.blit(icon, this.getX() + 4, this.getY() + 5,
                        0, iconIndex * 32, 32,
                        32, 32, 32 * info.getIconAspectRatio());
            }
        }
        if (isUse) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ICON);
            graphics.blit(ICON, this.getX() + this.getWidth() - 20, this.getY() + 13, 32, 0, 16, 16, 256, 256);
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

    private int getTickTime() {
        return (int) System.currentTimeMillis() / 50;
    }

    private void checkIconAnimation(SoundPackInfo info, ResourceLocation icon) {
        AbstractTexture iconText = Minecraft.getInstance().getTextureManager().getTexture(icon);
        if (iconText instanceof SizeTexture) {
            int width = ((SizeTexture) iconText).getWidth();
            int height = ((SizeTexture) iconText).getHeight();
            if (width >= height) {
                info.setIconAnimation(CustomModelPack.AnimationState.FALSE);
            } else {
                info.setIconAnimation(CustomModelPack.AnimationState.TRUE);
                info.setIconAspectRatio(height / width);
            }
        } else {
            info.setIconAnimation(CustomModelPack.AnimationState.FALSE);
        }
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }
}

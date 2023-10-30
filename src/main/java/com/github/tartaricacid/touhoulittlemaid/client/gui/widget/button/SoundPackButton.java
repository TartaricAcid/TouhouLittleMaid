package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.SizeTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.sound.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SoundPackButton extends FlatColorButton {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_custom_sound.png");
    private final SoundPackInfo info;
    private boolean isUse = false;

    public SoundPackButton(int pX, int pY, SoundPackInfo info, OnPress onPress) {
        super(pX, pY, 230, 43, TextComponent.EMPTY, onPress);
        this.info = info;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float pPartialTick) {
        super.renderButton(poseStack, mouseX, mouseY, pPartialTick);
        ResourceLocation icon = info.getIcon();
        if (icon == null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ICON);
            blit(poseStack, this.x + 4, this.y + 5, 0, 16, 32, 32, 256, 256);
        } else {
            if (info.getIconAnimation() == CustomModelPack.AnimationState.UNCHECK) {
                checkIconAnimation(info, icon);
            }
            if (info.getIconAnimation() == CustomModelPack.AnimationState.FALSE) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, icon);
                blit(poseStack, this.x + 4, this.y + 5,
                        0, 0, 32, 32, 32, 32);
            } else {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, icon);
                int time = getTickTime() / info.getIconDelay();
                int iconIndex = time % info.getIconAspectRatio();
                blit(poseStack, this.x + 4, this.y + 5,
                        0, iconIndex * 32, 32,
                        32, 32, 32 * info.getIconAspectRatio());
            }
        }
        if (isUse) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ICON);
            blit(poseStack, this.x + this.getWidth() - 20, this.y + 13, 32, 0, 16, 16, 256, 256);
        }
    }

    @Override
    @SuppressWarnings("all")
    public void renderString(PoseStack poseStack, Font font, int pColor) {
        int startX = this.x + 42;
        int startY = this.y + 7;

        MutableComponent packName = ParseI18n.parse(info.getPackName());
        String version = info.getVersion();
        List<String> author = info.getAuthor();
        String date = info.getDate();

        drawString(poseStack, font, packName, startX, startY, ChatFormatting.WHITE.getColor());

        if (StringUtils.isNotBlank(version)) {
            int titleWidth = font.width(packName);
            drawString(poseStack, font, "§nv" + version, startX + titleWidth + 5, startY, ChatFormatting.AQUA.getColor());
        }

        if (!author.isEmpty()) {
            startY += 10;
            String authorListText = StringUtils.joinWith(I18n.get("gui.touhou_little_maid.resources_download.author.delimiter"), author);
            String authorText = I18n.get("gui.touhou_little_maid.resources_download.author", authorListText);
            drawString(poseStack, font, authorText, startX, startY, ChatFormatting.GOLD.getColor());
        }

        if (StringUtils.isNotBlank(date)) {
            startY += 10;
            MutableComponent dateText = new TranslatableComponent("gui.touhou_little_maid.skin.text.date", date);
            drawString(poseStack, font, dateText, startX, startY, ChatFormatting.GREEN.getColor());
        }
    }

    private int getTickTime() {
        return (int) System.currentTimeMillis() / 50;
    }

    private void checkIconAnimation(SoundPackInfo info, ResourceLocation icon) {
        AbstractTexture iconText = Minecraft.getInstance().textureManager.getTexture(icon);
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

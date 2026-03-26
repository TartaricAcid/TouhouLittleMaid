package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsSTTSiteScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class STTSiteButton extends Button {
    private static final ResourceLocation MISC = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/ai_chat/misc.png");

    private final STTSite site;
    private final AIChatSettingsSTTSiteScreen parent;

    public STTSiteButton(STTSite site, AIChatSettingsSTTSiteScreen parent, int x, int y, int width) {
        super(x, y, width, 24, Component.empty(), b -> {
        }, DEFAULT_NARRATION);
        this.site = site;
        this.parent = parent;

        String nameKey = this.site.getNameKey();
        if (I18n.exists(nameKey)) {
            this.setMessage(Component.literal(I18n.get(nameKey)));
        } else {
            this.setMessage(Component.literal(this.site.id()));
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xbf_090909, 0xbf_090909);
        if (this.isHoveredOrFocused()) {
            graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x2f_F3EFE0, 0x2f_F3EFE0);
        }

        this.renderString(graphics, Minecraft.getInstance().font, 0xF3EFE0);

        RenderSystem.enableBlend();
        // 站点图标（左侧）
        graphics.blit(this.site.icon(), this.getX() + 6, this.getY() + 4, 0, 0, 16, 16, 16, 16);
        // 启用按钮（右侧）
        graphics.blit(MISC, this.getX() + this.width - 46, this.getY() + 4, this.site.enabled() ? 16 : 0, 0, 16, 16);
        // 编辑按钮（最右侧）
        graphics.blit(MISC, this.getX() + this.width - 24, this.getY() + 4, 16, 16, 16, 16);
        RenderSystem.disableBlend();
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int right = this.getX() + this.width;

        // 启用按钮
        if (right - 50 <= mouseX && mouseX <= right - 26) {
            this.parent.toggleSTTSite(this.site.id());
            return;
        }

        // 编辑按钮
        if (right - 28 <= mouseX && mouseX <= right - 4) {
            this.parent.openSTTSiteEditor(this.site.id());
        }
    }

    @Override
    public void renderString(GuiGraphics graphics, Font font, int color) {
        graphics.drawString(font, this.getMessage(), this.getX() + 28, this.getY() + (this.height - 8) / 2,
                this.site.enabled() ? 0xFF999999 : 0xFF444444, false);
    }
}

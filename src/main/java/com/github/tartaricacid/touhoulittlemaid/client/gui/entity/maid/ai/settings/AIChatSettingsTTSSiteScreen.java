package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.TTSSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai.TTSSiteButton;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * TTS 站点列表标签页，支持编辑站点配置
 */
public class AIChatSettingsTTSSiteScreen extends AIChatSettingsHubScreen {
    private static final int ROW_HEIGHT = 26;

    public AIChatSettingsTTSSiteScreen(@Nullable Screen parent, SharedState state, boolean insufficientPermissions) {
        super(parent, state, insufficientPermissions);
        this.listScrollOffset = state.ttsListScrollOffset;
    }

    @Override
    protected Type getType() {
        return Type.TTS_SITE;
    }

    @Override
    protected void initContent() {
        int contentX = this.getContentX();
        int contentWidth = this.getContentWidth();
        this.listArea = new Rectangle(contentX, this.getContentY(), contentWidth, this.getFooterTop() - this.getContentY() - 8);

        List<TTSSite> sites = new ArrayList<>(this.state.ttsSites.values());
        int visibleCount = this.getVisibleListCount(ROW_HEIGHT);
        int maxOffset = Math.max(0, sites.size() - visibleCount);
        if (this.listScrollOffset > maxOffset) {
            this.listScrollOffset = maxOffset;
        }

        int endIndex = Math.min(sites.size(), this.listScrollOffset + visibleCount);
        for (int i = this.listScrollOffset; i < endIndex; i++) {
            int rowY = (int) this.listArea.y + (i - this.listScrollOffset) * ROW_HEIGHT;
            this.addRenderableWidget(new TTSSiteButton(sites.get(i), this, contentX, rowY, contentWidth));
        }
    }

    @Override
    protected void persistTransientState() {
        this.state.ttsListScrollOffset = this.listScrollOffset;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderListScrollbar(graphics, this.state.ttsSites.size(), this.getVisibleListCount(ROW_HEIGHT));
        this.renderInsufficientPermissions(graphics);
    }

    public void openTTSSiteEditor(String siteId) {
        TTSSite site = this.state.ttsSites.get(siteId);
        if (site == null || this.minecraft == null) {
            return;
        }
        this.minecraft.setScreen(new TTSSiteEditorScreen(this, site));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.handleListScroll(mouseX, mouseY, delta, this.state.ttsSites.size(), this.getVisibleListCount(ROW_HEIGHT))) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}

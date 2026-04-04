package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.STTSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai.STTSiteButton;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * STT 站点列表标签页，站点数据保存在本地（非服务端同步）
 */
public class AIChatSettingsSTTSiteScreen extends AIChatSettingsHubScreen {
    private static final int ROW_HEIGHT = 26;

    public AIChatSettingsSTTSiteScreen(Screen parent, SharedState state, boolean insufficientPermissions) {
        super(parent, state, insufficientPermissions);
        this.listScrollOffset = state.sttSiteListScrollOffset;
    }

    @Override
    protected Type getType() {
        return Type.STT_SITE;
    }

    @Override
    protected void initContent() {
        int contentX = this.getContentX();
        int contentWidth = this.getContentWidth();
        this.listArea = new Rectangle(contentX, this.getContentY(), contentWidth, this.getFooterTop() - this.getContentY() - 8);

        List<STTSite> sites = Lists.newArrayList(this.state.sttSites.values());
        int visibleCount = this.getVisibleListCount(ROW_HEIGHT);
        int maxOffset = Math.max(0, sites.size() - visibleCount);
        if (this.listScrollOffset > maxOffset) {
            this.listScrollOffset = maxOffset;
        }

        int endIndex = Math.min(sites.size(), this.listScrollOffset + visibleCount);
        for (int i = this.listScrollOffset; i < endIndex; i++) {
            int rowY = (int) this.listArea.y + (i - this.listScrollOffset) * ROW_HEIGHT;
            this.addRenderableWidget(new STTSiteButton(sites.get(i), this, contentX, rowY, contentWidth));
        }
    }

    @Override
    protected void persistTransientState() {
        this.state.sttSiteListScrollOffset = this.listScrollOffset;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderListScrollbar(graphics, this.state.sttSites.size(), this.getVisibleListCount(ROW_HEIGHT));
    }

    public void openSTTSiteEditor(String siteId) {
        STTSite site = this.state.sttSites.get(siteId);
        if (site == null || this.minecraft == null) {
            return;
        }
        this.state.selectedSttSiteId = siteId;
        this.minecraft.setScreen(new STTSiteEditorScreen(this, site));
    }

    public void toggleSTTSite(String siteId) {
        STTSite site = this.state.sttSites.get(siteId);
        if (site == null) {
            return;
        }
        site.setEnabled(!site.enabled());
        this.saveLocalSTTSite(site);
        this.init();
    }

    public void saveLocalSTTSite(STTSite site) {
        this.state.sttSites.put(site.id(), site);
        this.state.selectedSttSiteId = site.id();
        AvailableSites.STT_SITES.clear();
        AvailableSites.STT_SITES.putAll(this.state.sttSites);
        AvailableSites.saveSTTSitesOnly();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.handleListScroll(mouseX, mouseY, delta, this.state.sttSites.size(), this.getVisibleListCount(ROW_HEIGHT))) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings;

import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializerRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.LLMOpenAISite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.LLMSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai.LLMSiteButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * LLM 站点列表标签页，支持新建 / 编辑 / 删除站点
 */
public class AIChatSettingsLLMSiteScreen extends AIChatSettingsHubScreen {
    private static final int ROW_HEIGHT = 26;

    public AIChatSettingsLLMSiteScreen(@Nullable Screen parent, SharedState state, boolean insufficientPermissions) {
        super(parent, state, insufficientPermissions);
        this.listScrollOffset = state.llmListScrollOffset;
    }

    public AIChatSettingsLLMSiteScreen(
            @Nullable Screen parent,
            Map<String, LLMSite> llmSites,
            Map<String, TTSSite> ttsSites,
            boolean insufficientPermissions
    ) {
        super(parent, llmSites, ttsSites, insufficientPermissions);
    }

    @Override
    protected Type getType() {
        return Type.LLM_SITE;
    }

    @Override
    protected void initContent() {
        int contentX = this.getContentX();
        int contentWidth = this.getContentWidth();
        int createButtonY = this.startY + BASE_HEIGHT - 48;
        this.listArea = new Rectangle(contentX, this.getContentY(), contentWidth, createButtonY - this.getContentY() - 4);

        List<LLMSite> sites = Lists.newArrayList(this.state.llmSites.values());
        int visibleCount = this.getVisibleListCount(ROW_HEIGHT);
        int maxOffset = Math.max(0, sites.size() - visibleCount);
        if (this.listScrollOffset > maxOffset) {
            this.listScrollOffset = maxOffset;
        }

        int endIndex = Math.min(sites.size(), this.listScrollOffset + visibleCount);
        for (int i = this.listScrollOffset; i < endIndex; i++) {
            int rowY = (int) this.listArea.y + (i - this.listScrollOffset) * ROW_HEIGHT;
            this.addRenderableWidget(new LLMSiteButton(sites.get(i), this, contentX, rowY, contentWidth));
        }

        if (!this.insufficientPermissions) {
            this.addLLMCreateButtons(contentX, contentWidth, createButtonY);
        }
    }

    private void addLLMCreateButtons(int btnX, int btnWidth, int btnY) {
        LLMApiType[] values = LLMApiType.values();
        int buttonWidth = (btnWidth - 4 * (values.length - 1)) / values.length;
        for (int i = 0; i < values.length; i++) {
            int x = btnX + i * (buttonWidth + 4);
            LLMApiType apiType = values[i];
            String siteName = I18n.get("ai.touhou_little_maid.chat.site.%s.name".formatted(apiType.getName()));
            MutableComponent text = Component.translatable("ai.touhou_little_maid.chat.settings.hub.create", siteName);
            this.addRenderableWidget(new FlatColorButton(x, btnY, buttonWidth, 20, text, b -> this.openNewLLMSiteEditor(apiType)));
        }
    }

    @Override
    protected void persistTransientState() {
        this.state.llmListScrollOffset = this.listScrollOffset;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderListScrollbar(graphics, this.state.llmSites.size(), this.getVisibleListCount(ROW_HEIGHT));
        this.renderInsufficientPermissions(graphics);
    }

    public void openLLMSiteEditor(String siteId) {
        LLMSite site = this.state.llmSites.get(siteId);
        if (!(site instanceof LLMOpenAISite) || this.minecraft == null) {
            return;
        }
        boolean supportsReasoning = "openai".equals(site.id());
        this.minecraft.setScreen(new LLMSiteEditorScreen(this, site, false, supportsReasoning));
    }

    public void openNewLLMSiteEditor(LLMApiType apiType) {
        LLMSite site = this.createDefaultLLMSite(apiType);
        if (!(site instanceof LLMOpenAISite) || this.minecraft == null) {
            return;
        }
        this.minecraft.setScreen(new LLMSiteEditorScreen(this, site, true));
    }

    public boolean hasLLMSite(String siteId) {
        return this.state.llmSites.containsKey(siteId);
    }

    @Nullable
    private LLMSite createDefaultLLMSite(LLMApiType apiType) {
        var serializer = SerializerRegister.getLLMSerializer(apiType.getName());
        if (serializer == null) {
            return null;
        }
        return serializer.defaultSite();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.handleListScroll(mouseX, mouseY, delta, this.state.llmSites.size(), this.getVisibleListCount(ROW_HEIGHT))) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}

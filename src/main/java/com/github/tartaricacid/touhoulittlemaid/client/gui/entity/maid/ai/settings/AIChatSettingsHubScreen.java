package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.AvailableSites;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.AIChatScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai.SideButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai.SideGroupWidget;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.OpenMaidAIChatMessage;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;
import static net.minecraft.network.chat.CommonComponents.GUI_BACK;

/**
 * 设置界面的骨架：左侧标签页导航 + 右侧内容区。
 * 各标签页（LLM / TTS / STT 配置 / STT 站点）由子类实现 {@link #initContent()}。
 */
public abstract class AIChatSettingsHubScreen extends Screen {
    protected static final int BASE_WIDTH = 400;
    protected static final int BASE_HEIGHT = 230;
    protected static final int SIDE_WIDTH = 100;

    protected static final int CONTENT_X_OFFSET = SIDE_WIDTH + 5;
    protected static final int CONTENT_WIDTH = BASE_WIDTH - CONTENT_X_OFFSET;

    protected final @Nullable Screen parent;
    protected final boolean insufficientPermissions;
    /**
     * 标签页切换时保持各子页面的临时状态（滚动位置、输入值等）
     */
    protected final SharedState state;

    protected int startX;
    protected int startY;

    /**
     * 子类用于列表区域和滚动偏移的共享字段
     */
    protected Rectangle listArea;
    protected int listScrollOffset;

    protected AIChatSettingsHubScreen(@Nullable Screen parent, SharedState state, boolean insufficientPermissions) {
        super(Component.literal("AI Chat Settings Hub"));
        this.parent = parent;
        this.state = state;
        this.insufficientPermissions = insufficientPermissions;
    }

    protected AIChatSettingsHubScreen(@Nullable Screen parent, Map<String, LLMSite> llmSites,
                                      Map<String, TTSSite> ttsSites, boolean insufficientPermissions) {
        this(parent, SharedState.create(llmSites, ttsSites), insufficientPermissions);
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.startX = (this.width - BASE_WIDTH) / 2;
        this.startY = (this.height - BASE_HEIGHT) / 2;

        int sideY = this.startY + 5;
        sideY = this.addSiteSideButtons(sideY);
        this.addSTTSideButtons(sideY);

        this.initContent();
        this.addFooterButtons();

        this.children().stream()
                .filter(w -> w instanceof SideButton)
                .forEach(w -> ((SideButton) w).updateSelect(this.getType()));
    }

    protected abstract Type getType();

    protected abstract void initContent();

    protected void persistTransientState() {
    }

    protected int getContentX() {
        return this.startX + CONTENT_X_OFFSET;
    }

    protected int getContentY() {
        return this.startY + 5;
    }

    protected int getContentWidth() {
        return CONTENT_WIDTH;
    }

    protected int getFooterY() {
        return this.startY + BASE_HEIGHT - 24;
    }

    protected int getFooterTop() {
        return this.startY + BASE_HEIGHT - 28;
    }

    protected int getVisibleListCount(int rowHeight) {
        return Math.max(1, (int) (this.listArea.h / rowHeight));
    }

    /**
     * 在列表区域右侧绘制滚动条
     */
    protected void renderListScrollbar(GuiGraphics graphics, int totalCount, int visibleCount) {
        if (this.listArea == null || totalCount <= visibleCount) {
            return;
        }
        int trackTop = (int) this.listArea.y;
        int trackHeight = (int) this.listArea.h - 5;
        int thumbHeight = Math.max(12, visibleCount * trackHeight / totalCount);
        int scrollRange = Math.max(1, totalCount - visibleCount);
        int thumbOffset = (trackHeight - thumbHeight) * this.listScrollOffset / scrollRange;
        graphics.fill(
                (int) this.listArea.right() + 2,
                trackTop + thumbOffset,
                (int) this.listArea.right() + 4,
                trackTop + thumbOffset + thumbHeight,
                0xFF55FF55
        );
    }

    /**
     * 在 LLM 和 TTS 站点权限不足时绘制提示文本
     */
    protected void renderInsufficientPermissions(GuiGraphics graphics) {
        if (this.insufficientPermissions) {
            MutableComponent text = Component.translatable("ai.touhou_little_maid.chat.settings.hub.insufficient_permissions");
            graphics.drawWordWrap(font, text, getContentX() + 20, getContentY() + 20, getContentWidth() - 60, 0xFFFF5555);
        }
    }

    /**
     * 处理列表区域的滚轮滚动，返回 true 表示已消费事件
     */
    protected boolean handleListScroll(double mouseX, double mouseY, double delta, int totalCount, int visibleCount) {
        if (this.listArea == null || !this.listArea.contains(mouseX, mouseY)) {
            return false;
        }
        int maxOffset = Math.max(0, totalCount - visibleCount);
        if (delta < 0 && this.listScrollOffset < maxOffset) {
            this.listScrollOffset++;
            this.init();
            return true;
        }
        if (delta > 0 && this.listScrollOffset > 0) {
            this.listScrollOffset--;
            this.init();
            return true;
        }
        return false;
    }

    protected void switchTo(Type type) {
        if (this.minecraft == null) {
            return;
        }
        this.persistTransientState();
        this.minecraft.setScreen(this.createTabScreen(type));
    }

    protected AIChatSettingsHubScreen createTabScreen(Type type) {
        return switch (type) {
            case LLM_SITE -> new AIChatSettingsLLMSiteScreen(this.parent, this.state, this.insufficientPermissions);
            case TTS_SITE -> new AIChatSettingsTTSSiteScreen(this.parent, this.state, this.insufficientPermissions);
            case STT_CONFIG -> new AIChatSettingsSTTConfigScreen(this.parent, this.state, this.insufficientPermissions);
            case STT_SITE -> new AIChatSettingsSTTSiteScreen(this.parent, this.state, this.insufficientPermissions);
        };
    }

    /**
     * 服务端同步站点数据后，用新数据重新打开当前标签页
     */
    public void reopenSelf(Map<String, LLMSite> llmSites, Map<String, TTSSite> ttsSites) {
        if (this.minecraft == null) {
            return;
        }
        this.persistTransientState();
        this.state.llmSites.clear();
        this.state.llmSites.putAll(llmSites);
        this.state.ttsSites.clear();
        this.state.ttsSites.putAll(ttsSites);
        this.minecraft.setScreen(this.createTabScreen(this.getType()));
    }

    private int addSiteSideButtons(int sideY) {
        this.addRenderableOnly(new SideGroupWidget(this.startX, sideY, SITE_NAME));

        sideY += 20;
        this.addRenderableWidget(new SideButton(Type.LLM_SITE, this.startX, sideY, SITE_LLM_NAME, b -> this.switchTo(Type.LLM_SITE)));

        sideY += 20;
        this.addRenderableWidget(new SideButton(Type.TTS_SITE, this.startX, sideY, SITE_TTS_NAME, b -> this.switchTo(Type.TTS_SITE)));

        return sideY;
    }

    private void addSTTSideButtons(int sideY) {
        sideY += 30;
        this.addRenderableOnly(new SideGroupWidget(this.startX, sideY, STT_NAME));

        sideY += 20;
        this.addRenderableWidget(new SideButton(Type.STT_CONFIG, this.startX, sideY, STT_CONFIG_NAME, b -> this.switchTo(Type.STT_CONFIG)));

        sideY += 20;
        this.addRenderableWidget(new SideButton(Type.STT_SITE, this.startX, sideY, STT_SITE_NAME, b -> this.switchTo(Type.STT_SITE)));
    }

    protected void addFooterButtons() {
        int x = this.getContentX();
        int y = this.getFooterY();
        this.addRenderableWidget(new FlatColorButton(x + this.getContentWidth() - 80, y, 80, 20, GUI_BACK, b -> this.onClose()));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (this.parent instanceof AIChatScreen chatScreen && chatScreen.getMaid().isAlive()) {
            NetworkHandler.CHANNEL.sendToServer(new OpenMaidAIChatMessage(chatScreen.getMaid()));
        } else {
            this.getMinecraft().setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static AIChatSettingsHubScreen openDefault(
            @Nullable Screen parent,
            Map<String, LLMSite> llmSites,
            Map<String, TTSSite> ttsSites,
            boolean insufficientPermissions
    ) {
        return new AIChatSettingsLLMSiteScreen(parent, llmSites, ttsSites, insufficientPermissions);
    }

    public static final class SharedState {
        public final Map<String, LLMSite> llmSites;
        public final Map<String, TTSSite> ttsSites;
        public final Map<String, STTSite> sttSites;

        public boolean sttEnabled;
        public STTApiType sttType;
        public String sttMicrophone;
        public int maidCanChatDistance;
        public String sttProxyAddress;

        // 临时变量
        public int llmListScrollOffset;
        public int ttsListScrollOffset;
        public int sttSiteListScrollOffset;
        public @Nullable String selectedSttSiteId;

        private SharedState(Map<String, LLMSite> llmSites,
                            Map<String, TTSSite> ttsSites,
                            Map<String, STTSite> sttSites,
                            boolean sttEnabled,
                            STTApiType sttType,
                            String sttMicrophone,
                            int maidCanChatDistance,
                            String sttProxyAddress,
                            int llmListScrollOffset,
                            int ttsListScrollOffset,
                            int sttSiteListScrollOffset,
                            @Nullable String selectedSttSiteId
        ) {
            this.llmSites = llmSites;
            this.ttsSites = ttsSites;
            this.sttSites = sttSites;
            this.sttEnabled = sttEnabled;
            this.sttType = sttType;
            this.sttMicrophone = sttMicrophone;
            this.maidCanChatDistance = maidCanChatDistance;
            this.sttProxyAddress = sttProxyAddress;
            this.llmListScrollOffset = llmListScrollOffset;
            this.ttsListScrollOffset = ttsListScrollOffset;
            this.sttSiteListScrollOffset = sttSiteListScrollOffset;
            this.selectedSttSiteId = selectedSttSiteId;
        }

        private static SharedState create(Map<String, LLMSite> llmSites, Map<String, TTSSite> ttsSites) {
            return new SharedState(
                    Maps.newLinkedHashMap(llmSites),
                    Maps.newLinkedHashMap(ttsSites),
                    Maps.newLinkedHashMap(AvailableSites.STT_SITES),
                    AIConfig.STT_ENABLED.get(),
                    AIConfig.STT_TYPE.get(),
                    AIConfig.STT_MICROPHONE.get(),
                    AIConfig.MAID_CAN_CHAT_DISTANCE.get(),
                    AIConfig.STT_PROXY_ADDRESS.get(),
                    0, 0, 0, null
            );
        }
    }

    public enum Type {
        LLM_SITE,
        TTS_SITE,
        STT_CONFIG,
        STT_SITE
    }
}

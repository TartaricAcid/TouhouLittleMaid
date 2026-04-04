package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.ChatClientInfo;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatManager;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatSerializable;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.ClientAvailableSitesSync;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.Role;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.SupportLanguage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system.TTSSystemSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendUserChatMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.OpenAIConfigMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.SaveMaidAIDataMessage;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.MaidAIChatSerializable.NO_TTS_SITE;

public class AIChatScreen extends Screen {
    private static final int POPUP_ROW_HEIGHT = 16;

    private final EntityMaid maid;
    private final MaidAIChatManager manager;

    private int currentTokens = 0;
    private int maxTokens = Integer.MAX_VALUE;

    private EditBox input;

    private FlatColorButton configButton;
    private FlatColorButton historyButton;
    private FlatColorButton settingButton;

    private FlatColorButton llmButton;
    private FlatColorButton ttsButton;
    private FlatColorButton langButton;

    private PopupType openPopup;
    private int popupScrollOffset;
    private @Nullable PopupGeometry popupGeometry;

    private int inputHistoryIndex;

    /**
     * 用来禁止快捷键打开时，会把快捷键输入到聊天框里的问题<br>
     * 开屏后前几帧不处理输入
     */
    private int tickCounter = 0;

    public AIChatScreen(EntityMaid maid) {
        super(Component.literal("Maid AI Chat Screen"));
        this.maid = maid;
        this.manager = maid.getAiChatManager();
    }

    public void updateTokens(int current, int max) {
        this.currentTokens = current;
        this.maxTokens = max;
    }

    @Override
    protected void init() {
        String currentInput = this.input != null ? this.input.getValue() : StringUtils.EMPTY;
        this.clearWidgets();

        int posX = this.width / 2;
        int posY = this.height / 2;

        int inputX = posX - 165;
        int inputY = posY + 58;
        int inputWidth = 330;

        // 添加聊天输入框
        this.addInputWidget(inputX, inputY, inputWidth, currentInput);
        // 模型存在判定
        this.ensureValidSelections();

        // 添加上侧配置按钮
        int y = inputY - 28, size = 18, gap = 2;
        this.addLeftButtons(inputX - 8, y, size, gap);
        this.addRightButtons(inputX + inputWidth - size * 3 + gap * 2, y, size, gap);
        // 刷新下拉框相关内容的状态
        this.refreshSelectorButtons();
    }

    private void addInputWidget(int inputX, int inputY, int inputWidth, String currentInput) {
        this.input = new EditBox(this.getMinecraft().fontFilterFishy, inputX, inputY, inputWidth, 20, Component.translatable("chat.editBox"));
        this.input.setMaxLength(128);
        this.input.setBordered(false);
        this.input.setValue(currentInput);
        this.input.setCanLoseFocus(false);

        this.addWidget(this.input);
        this.setInitialFocus(this.input);
        this.inputHistoryIndex = 0;
    }

    private void addLeftButtons(int leftX, int y, int size, int gap) {
        this.historyButton = this.addRenderableWidget(new FlatColorButton(leftX, y, size, size, Component.literal("🕑"), b -> {
            HistoryAIChatScreen screen = new HistoryAIChatScreen(this, this.maid);
            this.getMinecraft().setScreen(screen);
        }).setTooltips("ai.touhou_little_maid.chat.button.history.tip"));

        leftX = leftX + size + gap;
        this.settingButton = this.addRenderableWidget(new FlatColorButton(leftX, y, size, size, Component.literal("✎"), b -> {
            SettingEditScreen editScreen = new SettingEditScreen(this, this.maid);
            this.getMinecraft().setScreen(editScreen);
        }).setTooltips("ai.touhou_little_maid.chat.button.setting.tip"));

        leftX = leftX + size + gap;
        this.configButton = this.addRenderableWidget(new FlatColorButton(leftX, y, size, size, Component.literal("⚙"), b -> {
            OpenAIConfigMessage.sendToServer();
        }).setTooltips("ai.touhou_little_maid.chat.button.config.tip"));
    }

    private void addRightButtons(int rightX, int y, int size, int gap) {
        this.llmButton = this.addRenderableWidget(new FlatColorButton(rightX, y, size, size, Component.literal("✦"),
                b -> this.togglePopup(PopupType.LLM)).setTooltips("ai.touhou_little_maid.chat.button.llm.tip"));

        rightX = rightX + size + gap;
        this.ttsButton = this.addRenderableWidget(new FlatColorButton(rightX, y, size, size, Component.literal("🔊"),
                b -> this.togglePopup(PopupType.TTS)).setTooltips("ai.touhou_little_maid.chat.button.tts.tip"));

        rightX = rightX + size + gap;
        this.langButton = this.addRenderableWidget(new FlatColorButton(rightX, y, size, size, Component.literal("🌐"),
                b -> this.togglePopup(PopupType.LANGUAGE)).setTooltips("ai.touhou_little_maid.chat.button.language.tip"));
    }

    private void togglePopup(PopupType type) {
        if (this.openPopup == type) {
            // 类型相同，说明是二次点击，关闭
            this.openPopup = null;
        } else {
            this.openPopup = type;
        }
        this.refreshSelectorButtons();
    }

    private void refreshSelectorButtons() {
        this.llmButton.setSelect(this.openPopup == PopupType.LLM);
        this.ttsButton.setSelect(this.openPopup == PopupType.TTS);
        this.langButton.setSelect(this.openPopup == PopupType.LANGUAGE);

        this.llmButton.active = !ClientAvailableSitesSync.getClientLLMSites().isEmpty();
        this.ttsButton.active = !this.getPopupEntries(PopupType.TTS).isEmpty();
        this.langButton.active = !SupportLanguage.SUPPORTED_LANGUAGES.isEmpty();

        if (this.openPopup == null) {
            this.popupGeometry = null;
            this.popupScrollOffset = 0;
        } else {
            this.popupGeometry = this.getPopupGeometry(this.openPopup);
            this.popupScrollOffset = this.getInitialPopupOffset(this.openPopup);
        }
    }

    private void applyPopupSelection(PopupType type, PopupEntry entry) {
        if (entry.header()) {
            return;
        }

        switch (type) {
            case LLM -> {
                this.manager.llmSite = entry.site();
                this.manager.llmModel = entry.model();
            }
            case TTS -> {
                this.manager.ttsSite = entry.site();
                this.manager.ttsModel = entry.model();
            }
            case LANGUAGE -> this.manager.ttsLanguage = entry.language();
        }

        // 验证结果，并向服务端发送确认信息
        this.ensureValidSelections();
        NetworkHandler.CHANNEL.sendToServer(new SaveMaidAIDataMessage(this.maid.getId(), this.manager));

        // 关闭下拉框
        this.openPopup = null;
        this.refreshSelectorButtons();
    }

    private List<PopupEntry> getPopupEntries(PopupType type) {
        List<PopupEntry> entries = Lists.newArrayList();
        if (Objects.requireNonNull(type) == PopupType.LLM) {
            var llmSites = ClientAvailableSitesSync.getClientLLMSites();
            this.addSiteModelEntries(entries, llmSites, this.manager.llmSite, this.manager.llmModel);
            return entries;
        }

        if (type == PopupType.TTS) {
            boolean selected = MaidAIChatSerializable.isNoTTSSite(this.manager.ttsSite);
            MutableComponent noneName = Component.translatable("ai.touhou_little_maid.chat.site.none.name");
            entries.add(new PopupEntry(noneName, false, selected, NO_TTS_SITE, StringUtils.EMPTY, null));

            var ttsSites = ClientAvailableSitesSync.getClientTTSSites();
            String selectedSite = StringUtils.isBlank(this.manager.ttsSite) ? TTSSystemSite.API_TYPE : this.manager.ttsSite;
            this.addSiteModelEntries(entries, ttsSites, selectedSite, this.manager.ttsModel);
            return entries;
        }

        SupportLanguage.SUPPORTED_LANGUAGES.forEach(language -> {
            Component name = SupportLanguage.getLanguageName(language);
            boolean selected = language.equals(this.manager.ttsLanguage);
            PopupEntry entry = new PopupEntry(name, false, selected, null, null, language);
            entries.add(entry);
        });
        return entries;
    }

    private void addSiteModelEntries(List<PopupEntry> entries, Map<String, Map<String, String>> sites, String selectedSite, String selectedModel) {
        sites.forEach((site, models) -> {
            // 按站点添加主分类
            MutableComponent siteName = Component.literal(site);
            PopupEntry entry = new PopupEntry(siteName, true, false, site, null, null);
            entries.add(entry);

            // 如果模型为空，添加占位符
            if (models == null || models.isEmpty()) {
                boolean selected = site.equals(selectedSite) && StringUtils.isBlank(selectedModel);
                MutableComponent name = Component.literal("*");
                PopupEntry popupEntry = new PopupEntry(name, false, selected, site, StringUtils.EMPTY, null);
                entries.add(popupEntry);
                return;
            }

            // 否则正常添加模型
            models.forEach((modelId, modelName) -> {
                MutableComponent name = Component.literal(modelName);
                boolean selected = site.equals(selectedSite) && modelId.equals(selectedModel);
                PopupEntry popupEntry = new PopupEntry(name, false, selected, site, modelId, null);
                entries.add(popupEntry);
            });
        });
    }

    /**
     * 计算下拉列表第一次打开时的初始滚动偏移。
     * 目标是让当前已选中的条目尽量出现在可视范围内，
     * 如果选中项正好属于某个分组，则优先把分组标题也一起显示出来。
     */
    private int getInitialPopupOffset(PopupType type) {
        // 依据当前窗口大小，决定下拉列表的长度
        int popupMaxVisible = this.getPopupMaxVisible();

        List<PopupEntry> entries = this.getPopupEntries(type);
        if (entries.size() <= popupMaxVisible) {
            return 0;
        }
        int selectedIndex = this.getSelectedPopupIndex(entries);
        if (selectedIndex < 0) {
            return 0;
        }
        // 默认以当前选中项作为锚点，让弹窗打开时直接定位到它附近。
        int anchorIndex = selectedIndex;
        // 如果选中项前一行是分组标题，就把标题也带上，避免只看到子项看不到分组名。
        if (0 < selectedIndex && entries.get(selectedIndex - 1).header()) {
            anchorIndex = selectedIndex - 1;
        }
        return Math.max(0, Math.min(anchorIndex, entries.size() - popupMaxVisible));
    }

    private int getPopupMaxVisible() {
        return this.height / 2 / POPUP_ROW_HEIGHT;
    }

    /**
     * 在线性列表中查找第一个被选中的条目下标；找不到就返回 -1。
     */
    private int getSelectedPopupIndex(List<PopupEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).selected()) {
                return i;
            }
        }
        return -1;
    }

    private FlatColorButton getPopupAnchor(PopupType type) {
        return switch (type) {
            case LLM -> this.llmButton;
            case TTS -> this.ttsButton;
            case LANGUAGE -> this.langButton;
        };
    }

    private void ensureValidSelections() {
        var llmSites = ClientAvailableSitesSync.getClientLLMSites();
        var ttsSites = ClientAvailableSitesSync.getClientTTSSites();

        // 站点存在判定
        if (StringUtils.isBlank(this.manager.llmSite) || !llmSites.containsKey(this.manager.llmSite)) {
            this.manager.llmSite = llmSites.keySet().stream().findFirst().orElse(StringUtils.EMPTY);
        }
        if (MaidAIChatSerializable.isNoTTSSite(this.manager.ttsSite)) {
            this.manager.ttsModel = StringUtils.EMPTY;
        } else if (StringUtils.isNotBlank(this.manager.ttsSite) && !ttsSites.containsKey(this.manager.ttsSite)) {
            this.manager.ttsSite = ttsSites.keySet().stream().findFirst().orElse(StringUtils.EMPTY);
        }

        // 模型存在判定
        var llmModels = llmSites.get(this.manager.llmSite);
        this.manager.llmModel = this.ensureExistingModel(llmModels, this.manager.llmModel);
        if (MaidAIChatSerializable.isNoTTSSite(this.manager.ttsSite)) {
            this.manager.ttsModel = StringUtils.EMPTY;
        } else {
            String effectiveTtsSite = StringUtils.isBlank(this.manager.ttsSite) ? TTSSystemSite.API_TYPE : this.manager.ttsSite;
            var ttsModels = ttsSites.get(effectiveTtsSite);
            this.manager.ttsModel = this.ensureExistingModel(ttsModels, this.manager.ttsModel);
        }

        // 语言存在判定
        if (SupportLanguage.SUPPORTED_LANGUAGES.isEmpty() || StringUtils.isBlank(this.manager.ttsLanguage)) {
            this.manager.ttsLanguage = "en_us";
            return;
        }
        if (!SupportLanguage.SUPPORTED_LANGUAGES.contains(this.manager.ttsLanguage)) {
            this.manager.ttsLanguage = "en_us";
        }
    }

    private String ensureExistingModel(Map<String, String> models, String current) {
        if (models == null || models.isEmpty()) {
            return StringUtils.EMPTY;
        }
        if (StringUtils.isBlank(current) || !models.containsKey(current)) {
            return models.keySet().iterator().next();
        }
        return current;
    }

    @Override
    public void resize(Minecraft mc, int pWidth, int pHeight) {
        String chatText = this.input.getValue();
        super.resize(mc, pWidth, pHeight);
        this.input.setValue(chatText);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.input != null) {
            int x = this.input.getX();
            int y = this.input.getY();
            int w = this.input.getInnerWidth();
            int h = this.input.getHeight();
            String value = this.input.getValue();

            graphics.fill(x - 8, y - 8, x + w + 8, y + h - 6, 0xBF090909);
            this.input.render(graphics, mouseX, mouseY, partialTicks);

            if (StringUtils.isEmpty(value)) {
                MutableComponent text = Component.translatable("ai.touhou_little_maid.chat.input.tip")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                graphics.drawCenteredString(this.font, text, x + w / 2, y + (h - 22) / 2, 0xFFFFFF);
            }
        }

        this.renderSelectionSummaries(graphics);
        this.renderTokenUsage(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        if (this.openPopup != null) {
            this.renderPopup(graphics, mouseX, mouseY);
        } else {
            this.historyButton.renderToolTip(graphics, this, mouseX, mouseY);
            this.settingButton.renderToolTip(graphics, this, mouseX, mouseY);
            this.configButton.renderToolTip(graphics, this, mouseX, mouseY);
            this.llmButton.renderToolTip(graphics, this, mouseX, mouseY);
            this.ttsButton.renderToolTip(graphics, this, mouseX, mouseY);
            this.langButton.renderToolTip(graphics, this, mouseX, mouseY);
        }
    }

    private void renderSelectionSummaries(GuiGraphics graphics) {
        int left = this.input.getX() - 6;
        int right = this.input.getX() + this.input.getInnerWidth() + 6;
        int summaryY = this.input.getY() + 16;
        int halfWidth = (right - left) / 2;
        float scale = 0.5f;

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0f);

        int scaledLeft = Math.round(left / scale);
        int scaledRight = Math.round(right / scale);
        int scaledY = Math.round(summaryY / scale);
        int scaledHalfWidth = Math.round(halfWidth / scale);

        String llmModelSummary = "%s / %s".formatted(
                StringUtils.defaultIfEmpty(this.manager.llmSite, "*"),
                ClientAvailableSitesSync.getLLMModelName(this.manager.llmSite, this.manager.llmModel)
        );
        MutableComponent llmSummary = Component.translatable("ai.touhou_little_maid.chat.summary.llm", llmModelSummary);
        String trimmedLeft = this.trimToWidth(llmSummary.getString(), scaledHalfWidth);
        graphics.drawString(this.font, trimmedLeft, scaledLeft, scaledY, 0xFFADADAD);

        String ttsModelSummary;
        if (MaidAIChatSerializable.isNoTTSSite(this.manager.ttsSite)) {
            ttsModelSummary = Component.translatable("ai.touhou_little_maid.chat.site.none.name").getString();
        } else {
            String effectiveTtsSite = StringUtils.isBlank(this.manager.ttsSite) ? TTSSystemSite.API_TYPE : this.manager.ttsSite;
            ttsModelSummary = "%s / %s".formatted(
                    effectiveTtsSite,
                    ClientAvailableSitesSync.getTTSModelName(effectiveTtsSite, this.manager.ttsModel)
            );
        }
        MutableComponent ttsSummary = Component.translatable("ai.touhou_little_maid.chat.summary.tts",
                ttsModelSummary, SupportLanguage.getLanguageName(this.manager.ttsLanguage));
        String trimmedRight = this.trimToWidth(ttsSummary.getString(), scaledHalfWidth);
        int rightX = scaledRight - this.font.width(trimmedRight);
        graphics.drawString(this.font, trimmedRight, rightX, scaledY, 0xFFADADAD);

        graphics.pose().popPose();
    }

    private void renderTokenUsage(GuiGraphics graphics) {
        int left = this.input.getX() - 6;
        int right = this.input.getX() + this.input.getInnerWidth() + 6;
        int tokenY = this.input.getY() - 14;
        float scale = 0.5f;

        String currentStr = formatTokenCount(this.currentTokens);
        String text;
        if (this.maxTokens == Integer.MAX_VALUE) {
            text = "Token: %s / ∞".formatted(currentStr);
        } else {
            String maxStr = formatTokenCount(this.maxTokens);
            double percent = this.maxTokens > 0 ? (double) this.currentTokens / this.maxTokens * 100 : 0;
            text = "Token: %s / %s (%.1f%%)".formatted(currentStr, maxStr, percent);
        }

        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, 1.0f);
        int scaledX = Math.round((left + right) / 2.0f / scale) - this.font.width(text) / 2;
        int scaledY = Math.round(tokenY / scale);
        graphics.drawString(this.font, text, scaledX, scaledY, 0xFFADADAD, false);
        graphics.pose().popPose();
    }

    private static String formatTokenCount(int count) {
        if (count < 1000) {
            return String.valueOf(count);
        }
        if (count < 1_000_000) {
            return "%.1fK".formatted(count / 1000.0);
        }
        if (count < 1_000_000_000) {
            return "%.1fM".formatted(count / 1_000_000.0);
        }
        return "%.1fG".formatted(count / 1_000_000_000.0);
    }

    private String trimToWidth(String text, int maxWidth) {
        if (maxWidth <= 0 || this.font.width(text) <= maxWidth) {
            return text;
        }
        String ellipsis = "…";
        int trimWidth = Math.max(0, maxWidth - this.font.width(ellipsis));
        return this.font.plainSubstrByWidth(text, trimWidth) + ellipsis;
    }

    @Override
    public void tick() {
        this.input.tick();
        this.tickCounter++;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.openPopup != null) {
            // 执行正常下拉框按钮点击
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && this.tryClickPopup(mouseX, mouseY)) {
                return true;
            }

            // 如果悬浮于下拉框按钮上，正常触发开启与关闭
            if (this.isPopupTriggerHovered(mouseX, mouseY)) {
                return super.mouseClicked(mouseX, mouseY, button);
            }

            // 否者关闭下拉框按钮
            this.openPopup = null;
            this.refreshSelectorButtons();
        }

        // 输入框点击
        if (this.input.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.input);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isPopupTriggerHovered(double mouseX, double mouseY) {
        return this.isButtonHovered(this.llmButton, mouseX, mouseY)
               || this.isButtonHovered(this.ttsButton, mouseX, mouseY)
               || this.isButtonHovered(this.langButton, mouseX, mouseY);
    }

    private boolean isButtonHovered(FlatColorButton button, double mouseX, double mouseY) {
        return button != null && button.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        // GUI 刚打开的 5 tick 内，不允许输入，否则会把按键录入
        if (this.tickCounter < 5) {
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
            this.sendDoneMessage();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP) {
            return this.recallHistory(-1);
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            return this.recallHistory(1);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.openPopup == null) {
            return super.mouseScrolled(mouseX, mouseY, delta);
        }

        if (this.popupGeometry != null && this.popupGeometry.contains(mouseX, mouseY)) {
            int maxOffset = Math.max(0, this.popupGeometry.totalCount() - this.popupGeometry.visibleCount());
            if (delta < 0 && this.popupScrollOffset < maxOffset) {
                this.popupScrollOffset++;
                return true;
            }
            if (delta > 0 && this.popupScrollOffset > 0) {
                this.popupScrollOffset--;
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void sendDoneMessage() {
        String value = this.input.getValue();
        LocalPlayer player = this.getMinecraft().player;
        if (StringUtils.isNotBlank(value) && player != null) {
            ChatClientInfo clientInfo = ChatClientInfo.fromMaid(this.maid);
            NetworkHandler.CHANNEL.sendToServer(new SendUserChatMessage(this.maid.getId(), value, clientInfo));
            String format = "<%s> %s".formatted(player.getScoreboardName(), value);
            player.sendSystemMessage(Component.literal(format).withStyle(ChatFormatting.GRAY));
        }
        this.onClose();
    }

    private void renderPopup(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.popupGeometry == null || this.popupGeometry.entries().isEmpty()) {
            return;
        }

        List<PopupEntry> entries = this.popupGeometry.entries();

        int x = this.popupGeometry.x();
        int y = this.popupGeometry.y();
        int width = this.popupGeometry.width();
        int height = this.popupGeometry.height();
        int visible = this.popupGeometry.visibleCount();
        int endIndex = Math.min(entries.size(), this.popupScrollOffset + visible);

        // 背景
        graphics.fill(x, y, x + width, y + height, 0xF0101010);

        // 渲染每个条目
        for (int i = this.popupScrollOffset; i < endIndex; i++) {
            PopupEntry entry = entries.get(i);
            int renderIndex = i - this.popupScrollOffset;
            int top = y + renderIndex * POPUP_ROW_HEIGHT;
            boolean hover = x <= mouseX && mouseX < x + width && top <= mouseY && mouseY < top + POPUP_ROW_HEIGHT;

            // 如果是分组标题
            if (entry.header()) {
                this.drawPopupText(graphics, entry.label().getString(), x + 6, top + 4, width - 12, 0xFFF2F2F2);
                continue;
            }

            if (entry.selected()) {
                // 如果是选中条目
                graphics.fill(x + 1, top + 1, x + width - 1, top + POPUP_ROW_HEIGHT - 1, 0x1F55ff55);
                graphics.fill(x + 1, top + 1, x + 3, top + POPUP_ROW_HEIGHT - 1, 0xFF55ff55);
            } else if (hover) {
                // 如果是悬浮条目
                graphics.fill(x + 1, top + 1, x + width - 1, top + POPUP_ROW_HEIGHT - 1, 0xFF3C3C3C);
            }

            int textColor = entry.selected() ? 0xFF55ff55 : hover ? 0xFFF3EFE0 : 0xFF989898;
            this.drawPopupText(graphics, entry.label().getString(), x + 12, top + 4, width - 28, textColor);
        }

        // 渲染滚动条
        if (entries.size() > visible) {
            int barX = x + width - 4;
            int thumbHeight = Math.max(10, visible * visible * POPUP_ROW_HEIGHT / entries.size());
            int scrollRange = Math.max(1, entries.size() - visible);
            int thumbOffset = (height - thumbHeight) * this.popupScrollOffset / scrollRange;
            graphics.fill(barX - 1, y + thumbOffset + 1, barX + 2, y + thumbOffset + thumbHeight - 1, 0xFF55ff55);
        }
    }

    private void drawPopupText(GuiGraphics graphics, String text, int x, int y, int maxWidth, int color) {
        graphics.drawString(this.font, this.trimToWidth(text, maxWidth), x, y, color, false);
    }

    private boolean tryClickPopup(double mouseX, double mouseY) {
        if (this.popupGeometry == null || !this.popupGeometry.contains(mouseX, mouseY) || this.popupGeometry.entries().isEmpty()) {
            return false;
        }

        List<PopupEntry> entries = this.popupGeometry.entries();

        int x = this.popupGeometry.x();
        int y = this.popupGeometry.y();
        int w = this.popupGeometry.width();
        int visibleCount = this.popupGeometry.visibleCount();

        // 如果点击的是右侧滚动条，跳过
        if (entries.size() > visibleCount) {
            int scrollbarLeft = x + w - 6;
            if (mouseX >= scrollbarLeft) {
                return true;
            }
        }

        // 如果点击的索引超出范围，跳过
        int index = this.popupScrollOffset + (int) ((mouseY - y) / POPUP_ROW_HEIGHT);
        if (index < 0 || index >= entries.size()) {
            return true;
        }

        // 执行点击应用
        PopupEntry entry = entries.get(index);
        if (!entry.header()) {
            this.applyPopupSelection(this.openPopup, entry);
        }
        return true;
    }

    @Nullable
    private PopupGeometry getPopupGeometry(@Nullable PopupType type) {
        if (type == null) {
            return null;
        }

        FlatColorButton anchor = this.getPopupAnchor(type);
        List<PopupEntry> entries = this.getPopupEntries(type);
        if (anchor == null || entries.isEmpty()) {
            return null;
        }

        int popupMaxVisible = this.getPopupMaxVisible();
        int visible = Math.min(popupMaxVisible, entries.size());

        int width = this.getPopupWidth(type, anchor, entries);
        int height = visible * POPUP_ROW_HEIGHT;

        int x = anchor.getX() + anchor.getWidth() - width;
        int y = anchor.getY() - height - 4;

        // 防止超出屏幕边界
        if (x < 8) {
            x = 8;
        }
        if (x + width > this.width - 8) {
            x = this.width - 8 - width;
        }
        if (y < 8) {
            y = 8;
        }
        return new PopupGeometry(x, y, width, visible, entries);
    }

    private int getPopupWidth(PopupType type, FlatColorButton anchor, List<PopupEntry> entries) {
        int contentWidth = anchor.getWidth();
        for (PopupEntry entry : entries) {
            int rowWidth = this.font.width(entry.label());
            rowWidth += entry.header() ? 12 : 26;
            contentWidth = Math.max(contentWidth, rowWidth);
        }
        int minWidth = type == PopupType.LANGUAGE ? 96 : 150;
        int maxWidth = type == PopupType.LANGUAGE ? 152 : 220;
        return Math.max(minWidth, Math.min(maxWidth, contentWidth));
    }

    private boolean recallHistory(int direction) {
        List<String> history = this.getSentInputHistory();
        if (history.isEmpty()) {
            return false;
        }

        if (this.inputHistoryIndex < 0 || this.inputHistoryIndex > history.size()) {
            this.inputHistoryIndex = 0;
        }

        // 上翻历史
        if (direction < 0) {
            if (this.inputHistoryIndex < history.size() - 1) {
                this.inputHistoryIndex++;
                this.input.setValue(history.get(this.inputHistoryIndex));
                this.input.moveCursorToEnd();
                return true;
            }
            return false;
        }

        // 下翻历史
        if (0 < this.inputHistoryIndex) {
            this.inputHistoryIndex--;
            this.input.setValue(history.get(this.inputHistoryIndex));
            this.input.moveCursorToEnd();
            return true;
        }
        return false;
    }

    private List<String> getSentInputHistory() {
        // 第 0 个为当前输入项
        List<String> output = Lists.newArrayList(StringUtils.EMPTY);
        output.addAll(this.manager.getHistory()
                .getDeque().stream()
                .filter(msg -> msg.role() == Role.USER)
                .map(LLMMessage::message)
                .toList());
        return output;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    /**
     * 下拉框的范围信息，包括位置、宽度和可见条目数量；
     * <p>
     * 提供一个方法用于渲染、或判断鼠标是否在范围内。
     */
    private record PopupGeometry(int x, int y, int width, int visibleCount, List<PopupEntry> entries) {
        private int height() {
            return this.visibleCount * POPUP_ROW_HEIGHT;
        }

        private int totalCount() {
            return this.entries.size();
        }

        private boolean contains(double mouseX, double mouseY) {
            return this.x <= mouseX && mouseX < this.x + this.width
                   && this.y <= mouseY && mouseY < this.y + this.height();
        }
    }

    /**
     * 用于渲染的下拉框条目
     */
    private record PopupEntry(Component label, boolean header, boolean selected,
                              String site, String model, String language) {
    }

    /**
     * 下拉框类型
     */
    private enum PopupType {
        LLM,
        TTS,
        LANGUAGE
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.LLMOpenAISite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsLLMSiteScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.SaveLLMSiteMessage;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;
import static net.minecraft.network.chat.CommonComponents.GUI_BACK;

public class LLMSiteEditorScreen extends Screen {
    private static final int LABEL_COLOR = 0xFF777777;

    private static final int BASE_WIDTH = 400;
    private static final int BASE_HEIGHT = 230;

    private final AIChatSettingsLLMSiteScreen parent;
    private final LLMSite sourceSite;
    private final String siteDisplayName;

    /**
     * 是否是新增站点的状态，此状态可以自定义 site id
     */
    private final boolean createMode;
    /**
     * 是否能够设置模型的 “深度思考” 属性，部分国内 api 目前还不支持新版 openai api 格式
     */
    private final boolean supportsReasoning;
    /**
     * 模型列表
     */
    private final List<ModelRow> modelRows = Lists.newArrayList();

    private int startX;
    private int startY;

    private EditBox siteIdInput;
    private EditBox urlInput;
    private EditBox secretInput;

    /**
     * 模型列表框
     */
    private int modelScrollOffset;
    private Rectangle modelArea;

    /**
     * 保存时的提示信息
     */
    private long tipTimestamp = -1;
    private Component statusMessage = Component.empty();

    public LLMSiteEditorScreen(AIChatSettingsLLMSiteScreen parent, LLMSite sourceSite, boolean createMode, boolean supportsReasoning) {
        super(Component.literal("LLM OpenAI Site Editor"));

        this.parent = parent;
        this.sourceSite = sourceSite;
        this.createMode = createMode;
        this.supportsReasoning = supportsReasoning;

        String nameKey = sourceSite.getNameKey();
        this.siteDisplayName = I18n.exists(nameKey) ? I18n.get(nameKey) : sourceSite.id();

        if (!this.createMode && this.sourceSite instanceof LLMOpenAISite site) {
            // 如果非创建模式，那么需要预先填充 models 字段
            site.modelEntries().forEach((id, entry) -> {
                ModelRow modelRow = new ModelRow(entry.name(), entry.isReasoning());
                this.modelRows.add(modelRow);
            });
        }
    }

    public LLMSiteEditorScreen(AIChatSettingsLLMSiteScreen parent, LLMSite sourceSite, boolean createMode) {
        this(parent, sourceSite, createMode, false);
    }

    @Override
    protected void init() {
        // 输入框数值读取，这样在改变窗口时，数值不会丢失
        String siteIdValue = this.getEditBoxInitValue(this.siteIdInput, this.sourceSite.id());
        String urlValue = this.getEditBoxInitValue(this.urlInput, this.sourceSite.url());
        String secretValue = this.getEditBoxInitValue(this.secretInput, this.sourceSite instanceof LLMOpenAISite site ? site.secretKey() : StringUtils.EMPTY);

        this.clearWidgets();
        this.startX = (this.width - BASE_WIDTH) / 2;
        this.startY = (this.height - BASE_HEIGHT) / 2;

        int left = this.startX + 12;
        int contentWidth = BASE_WIDTH - 24;

        // 站点 ID，仅在新建模式下可修改
        this.siteIdInput = this.addInput(left, this.startY + 30, 124, SITE_ID_NAME, siteIdValue);
        this.siteIdInput.active = this.createMode;

        // URL
        this.urlInput = this.addInput(left + 132, this.startY + 30, contentWidth - 132, URL_NAME, urlValue);

        // 秘钥，隐藏显示
        this.secretInput = this.addInput(left, this.startY + 65, contentWidth, SECRET_KEY_NAME, secretValue);
        // 将秘钥输入框的字符显示为 ·，但末尾两个字符正常显示
        this.secretInput.setFormatter((text, pos) -> FormattedCharSequence.forward("·".repeat(text.length()), Style.EMPTY));

        // 模型列表
        this.modelArea = new Rectangle(left, this.startY + 104, contentWidth, BASE_HEIGHT - 103 - 34);
        this.createRows();

        // 底部按钮
        int bottomY = this.startY + BASE_HEIGHT - 24;

        this.addRenderableWidget(new FlatColorButton(left, bottomY, 96, 20, ADD_MODEL_NAME, b -> {
            this.modelRows.add(new ModelRow(StringUtils.EMPTY, false));
            int visibleCount = this.getVisibleModelCount();
            this.modelScrollOffset = Math.max(0, this.modelRows.size() - visibleCount);
            this.init();
        }));

        this.addRenderableWidget(new FlatColorButton(this.startX + BASE_WIDTH - 200, bottomY, 90, 20, SAVE_NAME, b -> this.saveSite()));
        this.addRenderableWidget(new FlatColorButton(this.startX + BASE_WIDTH - 102, bottomY, 90, 20, GUI_BACK, b -> this.onClose()));
    }

    private String getEditBoxInitValue(@Nullable EditBox editBox, String initValue) {
        if (editBox != null) {
            return editBox.getValue();
        }
        if (this.createMode) {
            return StringUtils.EMPTY;
        }
        return initValue;
    }

    private EditBox addInput(int x, int y, int width, Component title, String value) {
        EditBox box = new EditBox(this.font, x + 6, y + 8, width - 12, 16, title);
        box.setMaxLength(512);
        box.setBordered(false);
        box.setValue(value);
        this.addWidget(box);
        return box;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(0, 0, this.width, this.height, 0xc0101010, 0xc0101010);

        // 居中标题
        graphics.drawCenteredString(this.font, llmEditorTitle(this.siteDisplayName),
                this.startX + BASE_WIDTH / 2, this.startY + 4, 0xFFF3EFE0);

        this.renderInputField(graphics, this.siteIdInput, mouseX, mouseY, partialTick);
        this.renderInputField(graphics, this.urlInput, mouseX, mouseY, partialTick);
        this.renderInputField(graphics, this.secretInput, mouseX, mouseY, partialTick);

        this.renderModelArea(graphics, mouseX, mouseY, partialTick);

        super.render(graphics, mouseX, mouseY, partialTick);

        // 提示显示 2 秒
        if (System.currentTimeMillis() - this.tipTimestamp < 2000) {
            int x = this.startX + BASE_WIDTH - 155;
            int y = this.startY + BASE_HEIGHT - 35;
            graphics.drawCenteredString(this.font, this.statusMessage, x, y, 0xFFFF7777);
        }
    }

    private void renderInputField(GuiGraphics graphics, EditBox box, int mouseX, int mouseY, float partialTick) {
        int x = box.getX() - 6;
        int y = box.getY() - 6;
        int width = box.getInnerWidth() + 12;
        int height = box.getHeight() + 3;

        graphics.drawString(this.font, box.getMessage(), x + 2, y - 10, LABEL_COLOR, false);
        graphics.fill(x, y, x + width, y + height, 0xAA111111);
        box.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderModelArea(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int left = (int) this.modelArea.x;
        int top = (int) this.modelArea.y;

        // 标题
        graphics.drawString(this.font, MODELS_NAME, left + 2, top - 12, LABEL_COLOR);

        // 确定滚动起始值
        int visibleCount = this.getVisibleModelCount();
        int maxOffset = Math.max(0, this.modelRows.size() - visibleCount);
        if (this.modelScrollOffset > maxOffset) {
            this.modelScrollOffset = maxOffset;
        }

        // 渲染模型列表
        graphics.enableScissor(left, top - 2, (int) this.modelArea.right(), (int) this.modelArea.bottom() + 2);
        int startIndex = this.modelScrollOffset;
        int endIndex = Math.min(this.modelRows.size(), startIndex + visibleCount);
        for (int i = startIndex; i < endIndex; i++) {
            ModelRow row = this.modelRows.get(i);
            int rowY = top + 2 + (i - startIndex) * 22;
            if (row.nameBox != null) {
                int rowLeft = (int) this.modelArea.x;
                graphics.fill(rowLeft, rowY - 4, rowLeft + row.nameBox.getInnerWidth(), rowY + 20 - 4, 0xAA111111);
                row.nameBox.render(graphics, mouseX, mouseY, partialTick);
            }
        }
        graphics.disableScissor();

        // 渲染滚动条：仅当模型总数超过可见区域能容纳的数量时才显示
        if (this.modelRows.size() > visibleCount) {
            // 滚动条轨道的起始 Y 坐标
            int trackTop = top - 1;
            // 滚动条轨道的总高度
            int trackHeight = (int) this.modelArea.h - 9;
            // 滑块高度：按可见行数占总行数的比例缩放，但最小不低于 12px，防止滑块太小难以点击
            int thumbHeight = Math.max(12, visibleCount * trackHeight / this.modelRows.size());
            // 可滚动的最大偏移量（总行数 - 可见行数），至少为 1 防止除零
            int scrollRange = Math.max(1, this.modelRows.size() - visibleCount);
            // 根据当前滚动偏移量，按比例计算滑块在轨道中的 Y 偏移
            int thumbOffset = (trackHeight - thumbHeight) * this.modelScrollOffset / scrollRange;
            // 在模型区域的右侧绘制一个 2px 宽的绿色滑块
            graphics.fill((int) this.modelArea.right() - 4,
                    trackTop + thumbOffset,
                    (int) this.modelArea.right() - 2,
                    trackTop + thumbOffset + thumbHeight,
                    0xFF55FF55);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.modelArea.contains(mouseX, mouseY)) {
            int visibleCount = this.getVisibleModelCount();
            int maxOffset = Math.max(0, this.modelRows.size() - visibleCount);
            if (delta < 0 && this.modelScrollOffset < maxOffset) {
                this.modelScrollOffset++;
                this.init();
                return true;
            }
            if (delta > 0 && this.modelScrollOffset > 0) {
                this.modelScrollOffset--;
                this.init();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        this.siteIdInput.tick();
        this.urlInput.tick();
        this.secretInput.tick();
        this.modelRows.stream().map(row -> row.nameBox).filter(Objects::nonNull).forEach(EditBox::tick);
        super.tick();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public AIChatSettingsHubScreen getParentHub() {
        return this.parent;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    private void saveSite() {
        LLMSite site = this.buildSite();
        if (site == null) {
            return;
        }
        SaveLLMSiteMessage message = this.createMode ? SaveLLMSiteMessage.create(site) : SaveLLMSiteMessage.update(site);
        NetworkHandler.CHANNEL.sendToServer(message);
    }

    private LLMSite buildSite() {
        if (!(this.sourceSite instanceof LLMOpenAISite site)) {
            return null;
        }

        String siteId = StringUtils.trim(this.siteIdInput.getValue());
        if (StringUtils.isBlank(siteId)) {
            this.showStatus(SITE_ID_IS_EMPTY);
            return null;
        }
        if (this.createMode && this.parent.hasLLMSite(siteId)) {
            this.showStatus(SITE_ID_ALREADY_EXISTS);
            return null;
        }

        String url = StringUtils.trim(this.urlInput.getValue());
        if (StringUtils.isBlank(url)) {
            this.showStatus(URL_IS_EMPTY);
            return null;
        }

        List<ModelRow> rows = this.modelRows;
        boolean hasModel = rows.stream().anyMatch(row -> StringUtils.isNotBlank(row.name()));
        if (this.createMode && !hasModel) {
            this.showStatus(MODEL_IS_EMPTY);
            return null;
        }

        // 秘钥可以为空（部分本地模型没有秘钥）
        String secretKey = this.secretInput.getValue();

        // 普通 OpenAI 模型
        List<LLMOpenAISite.ModelEntry> models = Lists.newArrayList();
        HashSet<String> seen = Sets.newHashSet();
        for (ModelRow row : rows) {
            String modelName = row.name();
            if (StringUtils.isBlank(modelName) || !seen.add(modelName)) {
                continue;
            }
            models.add(new LLMOpenAISite.ModelEntry(modelName, row.reasoning()));
        }
        return new LLMOpenAISite(siteId, site.icon(), url, site.enabled(), secretKey, site.headers(), models);
    }


    /**
     * 创建模型列表
     */
    private void createRows() {
        int visibleCount = this.getVisibleModelCount();
        int maxOffset = Math.max(0, this.modelRows.size() - visibleCount);
        if (this.modelScrollOffset > maxOffset) {
            this.modelScrollOffset = maxOffset;
        }

        int startIndex = this.modelScrollOffset;
        int endIndex = Math.min(this.modelRows.size(), startIndex + visibleCount);

        int left = (int) this.modelArea.x;
        int top = (int) this.modelArea.y;
        int right = (int) this.modelArea.right();

        for (int i = startIndex; i < endIndex; i++) {
            int rowY = top + 3 + (i - startIndex) * 22;
            int inputWidth = this.supportsReasoning ? BASE_WIDTH - 100 : BASE_WIDTH - 38;
            ModelRow row = this.modelRows.get(i);

            String preValue = row.name();
            row.nameBox = new EditBox(this.font, left + 6, rowY + 2, inputWidth - 12, 16, Component.empty());
            row.nameBox.setMaxLength(512);
            row.nameBox.setBordered(false);
            row.nameBox.setValue(preValue);
            this.addWidget(row.nameBox);

            if (this.supportsReasoning) {
                String key = row.reasoning ? "ai.touhou_little_maid.chat.settings.hub.model.reasoning" : "ai.touhou_little_maid.chat.settings.hub.model.normal";
                row.toggleButton = this.addRenderableWidget(new FlatColorButton(right - 86, rowY - 4, 60, 18, Component.translatable(key), b -> {
                    row.reasoning = !row.reasoning;
                    this.init();
                }));
            } else {
                row.toggleButton = null;
            }

            final int index = i;
            row.deleteButton = this.addRenderableWidget(new FlatColorButton(right - 24, rowY - 4, 18, 18, Component.literal("✕"), b -> {
                this.modelRows.remove(index);
                this.init();
            }));
        }
    }

    private int getVisibleModelCount() {
        return (int) Math.max(1, (this.modelArea.h - 4) / 22);
    }

    private void showStatus(Component message) {
        this.statusMessage = message;
        this.tipTimestamp = System.currentTimeMillis();
    }

    private static final class ModelRow {
        /**
         * nameBox 创建前的初始值缓冲，一旦 nameBox 存在就不再使用
         */
        private final String initialName;

        private boolean reasoning;
        private EditBox nameBox;
        private FlatColorButton toggleButton;
        private FlatColorButton deleteButton;

        private ModelRow(String name, boolean reasoning) {
            this.initialName = name;
            this.reasoning = reasoning;
        }

        private String name() {
            return this.nameBox != null ? this.nameBox.getValue() : this.initialName;
        }

        private boolean reasoning() {
            return this.reasoning;
        }
    }
}

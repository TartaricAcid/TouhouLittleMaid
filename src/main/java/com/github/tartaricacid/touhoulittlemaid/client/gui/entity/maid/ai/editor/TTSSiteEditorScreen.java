package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.FieldDescriptor;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSSiteFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsTTSSiteScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.SaveTTSSitePacket;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;
import static net.minecraft.network.chat.CommonComponents.GUI_BACK;

/**
 * TTS 站点编辑器：固定字段区 + 可滚动模型列表。
 * 表单字段定义委托给 {@link TTSSiteFormLayout} 的各子类。
 */
public class TTSSiteEditorScreen extends Screen {
    private static final int LABEL_COLOR = 0xFF777777;
    private static final int BASE_WIDTH = 400;
    private static final int MIN_BASE_HEIGHT = 230;
    private static final int MAX_BASE_HEIGHT = 330;
    private static final int SCREEN_MARGIN_Y = 32;
    private static final int CONTENT_SCROLL_STEP = 24;
    private static final int FIELD_ROW_HEIGHT = 35;
    private static final int MODEL_ROW_HEIGHT = 22;
    private static final int MODEL_AREA_MAX_ROWS = 5;

    private final AIChatSettingsTTSSiteScreen parent;
    private final TTSSiteFormLayout layout;
    private final String siteDisplayName;

    /**
     * 固定字段列表（不滚动）
     */
    private final List<FormField> fields = Lists.newArrayList();
    /**
     * 模型行列表（可滚动）
     */
    private final List<ModelRow> modelRows = Lists.newArrayList();

    private int startX;
    private int startY;
    private int baseHeight;

    /**
     * 模型列表滚动区域
     */
    private Rectangle modelArea;
    private int modelScrollOffset;
    private Rectangle contentArea;
    private int contentScrollOffset;
    private int contentHeight;
    private boolean addingScrollingWidgets;
    private final List<Renderable> scrollingRenderables = Lists.newArrayList();
    private final List<AbstractWidget> scrollingWidgets = Lists.newArrayList();

    /**
     * 保存时的提示信息
     */
    private long tipTimestamp = -1;
    private Component statusMessage = Component.empty();

    public TTSSiteEditorScreen(AIChatSettingsTTSSiteScreen parent, TTSSite sourceSite) {
        super(Component.literal("TTS Site Editor"));
        this.parent = parent;
        this.layout = sourceSite.formLayout();

        String nameKey = sourceSite.getNameKey();
        this.siteDisplayName = I18n.exists(nameKey) ? I18n.get(nameKey) : sourceSite.id();

        this.initStateFromLayout();
    }

    private void initStateFromLayout() {
        this.fields.clear();
        this.modelRows.clear();

        for (FieldDescriptor desc : this.layout.getFieldDescriptors()) {
            this.fields.add(new FormField(desc.label(), desc.value(), desc.editable(), desc.secret()));
        }

        if (this.layout.supportsModelRows()) {
            Map<String, String> initialModels = this.layout.getInitialModels();
            initialModels.forEach((id, name) -> this.modelRows.add(new ModelRow(id, name)));
        }
    }

    @Override
    @SuppressWarnings("all")
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget) {
        T widget = super.addRenderableWidget(pWidget);
        if (this.addingScrollingWidgets) {
            this.scrollingRenderables.add(widget);
            if (widget instanceof AbstractWidget abstractWidget) {
                this.scrollingWidgets.add(abstractWidget);
            }
        }
        return widget;
    }

    @Override
    protected void init() {
        // 在缩放窗口时，更新输入框的值
        this.fields.forEach(FormField::syncFromBox);
        this.modelRows.forEach(ModelRow::syncFromBox);

        this.clearWidgets();
        this.scrollingRenderables.clear();
        this.scrollingWidgets.clear();

        this.baseHeight = Mth.clamp(this.height - SCREEN_MARGIN_Y, MIN_BASE_HEIGHT, MAX_BASE_HEIGHT);
        this.startX = (this.width - BASE_WIDTH) / 2;
        this.startY = (this.height - this.baseHeight) / 2;

        int left = this.startX + 12;
        int contentWidth = BASE_WIDTH - 24;
        int bottomY = this.startY + this.baseHeight - 24;
        int contentTop = this.startY + 28;
        int contentBottom = bottomY - 8;
        this.contentArea = new Rectangle(left, contentTop, contentWidth, Math.max(32, contentBottom - contentTop));

        // 内容区：字段、额外按钮和模型区整体滚动，底部保存/返回按钮固定
        int fieldY = contentTop;
        int fieldMaxSize = this.fields.size();
        // 奇数，那么最后一个占一整行，否则是均分，左右各一个
        boolean isOdd = fieldMaxSize % 2 == 1;
        int fieldIndex = 0;
        int fieldRow = 0;
        int extraAfterRows = this.layout.extraInitAfterFieldRows();
        boolean extraAdded = false;
        while (fieldIndex < fieldMaxSize) {
            if (isOdd && fieldIndex == fieldMaxSize - 1) {
                FormField field = this.fields.get(fieldIndex++);
                this.createFieldWidget(field, left, this.contentY(fieldY), contentWidth);
            } else {
                int fieldWidth = (contentWidth - 6) / 2;
                FormField leftField = this.fields.get(fieldIndex++);
                this.createFieldWidget(leftField, left, this.contentY(fieldY), fieldWidth);
                if (fieldIndex < fieldMaxSize) {
                    FormField rightField = this.fields.get(fieldIndex++);
                    this.createFieldWidget(rightField, left + fieldWidth + 6, this.contentY(fieldY), fieldWidth);
                }
            }
            fieldY += FIELD_ROW_HEIGHT;
            fieldRow++;
            if (!extraAdded && fieldRow >= extraAfterRows) {
                fieldY += this.createExtraWidgets(left, this.contentY(fieldY), contentWidth);
                extraAdded = true;
            }
        }
        if (!extraAdded) {
            fieldY += this.createExtraWidgets(left, this.contentY(fieldY), contentWidth);
        }

        // 可滚动模型区
        if (this.layout.supportsModelRows()) {
            int modelTop = fieldY + 14;
            int modelHeight = this.getPreferredModelAreaHeight();
            this.modelArea = new Rectangle(left, this.contentY(modelTop), contentWidth, modelHeight);
            this.addingScrollingWidgets = true;
            try {
                this.createModelRows(left, contentWidth);
            } finally {
                this.addingScrollingWidgets = false;
            }
            fieldY = modelTop + modelHeight + 10;

            this.addRenderableWidget(new FlatColorButton(left, bottomY, 96, 20, ADD_MODEL_NAME, b -> {
                this.modelRows.add(new ModelRow(StringUtils.EMPTY, StringUtils.EMPTY));
                int visibleCount = this.getVisibleModelCount();
                this.modelScrollOffset = Math.max(0, this.modelRows.size() - visibleCount);
                this.init();
            }));
        } else {
            this.modelArea = null;
        }

        this.contentHeight = Math.max((int) this.contentArea.h, fieldY - contentTop);
        int maxContentScrollOffset = this.getMaxContentScrollOffset();
        if (this.contentScrollOffset > maxContentScrollOffset) {
            this.contentScrollOffset = maxContentScrollOffset;
            this.init();
            return;
        }
        this.updateScrollingWidgetVisibility();

        // 底部按钮
        this.addRenderableWidget(new FlatColorButton(this.startX + BASE_WIDTH - 200, bottomY, 90, 20, SAVE_NAME, b -> this.saveSite()));
        this.addRenderableWidget(new FlatColorButton(this.startX + BASE_WIDTH - 102, bottomY, 90, 20, GUI_BACK, b -> this.onClose()));
    }

    private void createFieldWidget(FormField field, int left, int y, int width) {
        EditBox box = new EditBox(this.font, left + 6, y + 14, width - 12, 16, field.i18nName());
        box.setMaxLength(512);
        box.setBordered(false);
        box.active = field.editable;
        box.setValue(field.value);
        if (field.secret) {
            box.setFormatter((text, pos) -> FormattedCharSequence.forward("·".repeat(text.length()), Style.EMPTY));
        }
        boolean visible = this.contentIntersects(left, y - 14, width, FIELD_ROW_HEIGHT + 14);
        box.visible = visible;
        box.active = field.editable && visible;
        this.addWidget(box);
        field.box = box;
    }

    private void createModelRows(int left, int contentWidth) {
        int visibleCount = this.getVisibleModelCount();
        int maxOffset = Math.max(0, this.modelRows.size() - visibleCount);
        if (this.modelScrollOffset > maxOffset) {
            this.modelScrollOffset = maxOffset;
        }

        int top = (int) this.modelArea.y;
        int gap = 6;
        int inputWidth = contentWidth - 18 - gap;
        int idWidth = (inputWidth - gap) * 2 / 3;
        int nameWidth = inputWidth - gap - idWidth;

        int startIndex = this.modelScrollOffset;
        int endIndex = Math.min(this.modelRows.size(), startIndex + visibleCount);

        for (int i = startIndex; i < endIndex; i++) {
            int rowY = top + 2 + (i - startIndex) * MODEL_ROW_HEIGHT;
            ModelRow row = this.modelRows.get(i);

            // 先捕获值，再创建 box（避免返回新 box 的空值）
            String preId = row.id();
            String preName = row.name();

            row.idBox = new EditBox(this.font, left + 6, rowY + 2, idWidth, 16, Component.literal("Model Id"));
            row.idBox.setMaxLength(512);
            row.idBox.setBordered(false);
            row.idBox.setValue(preId);
            boolean visible = this.contentIntersects(left, rowY - 6, contentWidth, MODEL_ROW_HEIGHT);
            row.idBox.visible = visible;
            row.idBox.active = visible;
            this.addWidget(row.idBox);

            row.nameBox = new EditBox(this.font, left + 16 + idWidth + gap, rowY + 2, nameWidth - 26, 16, Component.literal("Display Name"));
            row.nameBox.setMaxLength(512);
            row.nameBox.setBordered(false);
            row.nameBox.setValue(preName);
            row.nameBox.visible = visible;
            row.nameBox.active = visible;
            this.addWidget(row.nameBox);

            final int index = i;
            this.addRenderableWidget(new FlatColorButton(left - 6 + inputWidth + gap, rowY - 4, 18, 18, Component.literal("✕"), b -> {
                this.modelRows.remove(index);
                this.init();
            }));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.fillGradient(0, 0, this.width, this.height, 0xc0101010, 0xc0101010);

        // 居中标题
        graphics.drawCenteredString(this.font, ttsEditorTitle(this.siteDisplayName),
                this.startX + BASE_WIDTH / 2, this.startY + 4, 0xFFF3EFE0);

        // 固定字段
        for (FormField field : this.fields) {
            this.renderInputField(graphics, field.box, mouseX, mouseY, partialTick);
        }

        // 模型区
        if (this.modelArea != null) {
            this.renderModelArea(graphics, mouseX, mouseY, partialTick);
        }

        for (Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }

        this.renderContentScrollbar(graphics);

        // 保存提示
        if (System.currentTimeMillis() - this.tipTimestamp < 2000) {
            int x = this.startX + BASE_WIDTH - 155;
            int y = this.startY + this.baseHeight - 35;
            graphics.drawCenteredString(this.font, this.statusMessage, x, y, 0xFFADADAD);
        }
    }

    private void renderInputField(GuiGraphics graphics, EditBox box, int mouseX, int mouseY, float partialTick) {
        if (box == null || !box.visible) {
            return;
        }

        int x = box.getX() - 6;
        int y = box.getY() - 6;
        int width = box.getWidth() + 12;
        int height = box.getHeight() + 3;

        graphics.drawString(this.font, box.getMessage(), x + 2, y - 12, LABEL_COLOR, false);
        graphics.fill(x, y, x + width, y + height, 0xAA111111);
        box.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderModelArea(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int left = (int) this.modelArea.x;
        int top = (int) this.modelArea.y;
        if (!this.contentIntersects(left, top - 18, (int) this.modelArea.w, (int) this.modelArea.h + 20)) {
            return;
        }

        // 主标题
        if (this.contentIntersects(left, top - 18, (int) this.modelArea.w, 16)) {
            graphics.drawString(this.font, this.layout.modelsTitle(), left + 2, top - 14, LABEL_COLOR, false);
        }

        int visibleCount = this.getVisibleModelCount();
        int startIndex = this.modelScrollOffset;
        int endIndex = Math.min(this.modelRows.size(), startIndex + visibleCount);

        int scissorTop = Math.max(top - 2, (int) this.contentArea.y);
        int scissorBottom = Math.min((int) this.modelArea.bottom() + 2, (int) this.contentArea.bottom());
        if (scissorBottom <= scissorTop) {
            return;
        }
        graphics.enableScissor(left, scissorTop, (int) this.modelArea.right(), scissorBottom);
        for (int i = startIndex; i < endIndex; i++) {
            ModelRow row = this.modelRows.get(i);
            if (row.idBox != null && row.nameBox != null) {
                graphics.fill(row.idBox.getX() - 6, row.idBox.getY() - 6,
                        row.idBox.getX() + row.idBox.getInnerWidth() + 8,
                        row.idBox.getY() + row.idBox.getHeight() - 3,
                        0xAA111111
                );

                graphics.fill(row.nameBox.getX() - 6, row.nameBox.getY() - 6,
                        row.nameBox.getX() + row.nameBox.getInnerWidth() + 8,
                        row.nameBox.getY() + row.nameBox.getHeight() - 3,
                        0xAA111111
                );

                row.idBox.render(graphics, mouseX, mouseY, partialTick);
                row.nameBox.render(graphics, mouseX, mouseY, partialTick);
            }
        }
        graphics.disableScissor();

        // 模型区滚动条
        if (this.modelRows.size() > visibleCount) {
            // 滚动条轨道的起始 Y 坐标
            int trackTop = Math.max(top - 2, (int) this.contentArea.y);
            // 滚动条轨道的总高度
            int trackBottom = Math.min((int) this.modelArea.bottom() - 2, (int) this.contentArea.bottom());
            int trackHeight = Math.max(1, trackBottom - trackTop);
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
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalDelta, double verticalDelta) {
        if (this.modelArea != null && this.modelArea.contains(mouseX, mouseY)
                && this.contentArea != null && this.contentArea.contains(mouseX, mouseY)
                && this.scrollModelRows(verticalDelta)) {
            return true;
        }
        if (this.contentArea != null && this.contentArea.contains(mouseX, mouseY) && this.scrollContent(verticalDelta)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalDelta, verticalDelta);
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

    private int getVisibleModelCount() {
        return Math.max(1, (int) ((this.modelArea.h - 4) / MODEL_ROW_HEIGHT));
    }

    private int createExtraWidgets(int x, int y, int width) {
        this.addingScrollingWidgets = true;
        try {
            return this.layout.extraInit(x, y, width, this);
        } finally {
            this.addingScrollingWidgets = false;
        }
    }

    private int contentY(int logicalY) {
        return logicalY - this.contentScrollOffset;
    }

    private int getPreferredModelAreaHeight() {
        int visibleRows = Math.min(Math.max(this.modelRows.size(), 3), MODEL_AREA_MAX_ROWS);
        return visibleRows * MODEL_ROW_HEIGHT + 6;
    }

    private int getMaxContentScrollOffset() {
        if (this.contentArea == null) {
            return 0;
        }
        return Math.max(0, this.contentHeight - (int) this.contentArea.h);
    }

    private boolean scrollModelRows(double verticalDelta) {
        int visibleCount = this.getVisibleModelCount();
        int maxOffset = Math.max(0, this.modelRows.size() - visibleCount);
        if (verticalDelta < 0 && this.modelScrollOffset < maxOffset) {
            this.modelScrollOffset++;
            this.init();
            return true;
        }
        if (verticalDelta > 0 && this.modelScrollOffset > 0) {
            this.modelScrollOffset--;
            this.init();
            return true;
        }
        return false;
    }

    private boolean scrollContent(double verticalDelta) {
        int maxOffset = this.getMaxContentScrollOffset();
        if (maxOffset <= 0) {
            return false;
        }
        int oldOffset = this.contentScrollOffset;
        if (verticalDelta < 0) {
            this.contentScrollOffset = Math.min(maxOffset, this.contentScrollOffset + CONTENT_SCROLL_STEP);
        } else if (verticalDelta > 0) {
            this.contentScrollOffset = Math.max(0, this.contentScrollOffset - CONTENT_SCROLL_STEP);
        }
        if (oldOffset != this.contentScrollOffset) {
            this.init();
            return true;
        }
        return false;
    }

    private void renderContentScrollbar(GuiGraphics graphics) {
        if (this.contentArea == null || this.contentHeight <= this.contentArea.h) {
            return;
        }
        int trackLeft = this.startX + BASE_WIDTH - 7;
        int trackTop = (int) this.contentArea.y;
        int trackHeight = (int) this.contentArea.h;
        int thumbHeight = Math.max(24, (int) (this.contentArea.h * trackHeight / this.contentHeight));
        int scrollRange = Math.max(1, this.getMaxContentScrollOffset());
        int thumbOffset = (trackHeight - thumbHeight) * this.contentScrollOffset / scrollRange;

        graphics.fill(trackLeft, trackTop, trackLeft + 3, trackTop + trackHeight, 0x66333333);
        graphics.fill(trackLeft - 1, trackTop + thumbOffset,
                trackLeft + 4, trackTop + thumbOffset + thumbHeight, 0xFF55FF55);
    }

    private void updateScrollingWidgetVisibility() {
        for (AbstractWidget widget : this.scrollingWidgets) {
            boolean visible = this.contentIntersects(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
            widget.visible = visible;
            widget.active = visible;
        }
    }

    private boolean contentIntersects(int x, int y, int width, int height) {
        if (this.contentArea == null) {
            return false;
        }
        return x < this.contentArea.right()
                && x + width > this.contentArea.x
                && y < this.contentArea.bottom()
                && y + height > this.contentArea.y;
    }

    private void saveSite() {
        Map<String, String> models = this.buildModels();
        TTSSite site = this.layout.buildSite(this::getFieldValue, models, this::showStatus);
        if (site == null) {
            return;
        }
        PacketDistributor.sendToServer(SaveTTSSitePacket.update(site));
    }

    private Map<String, String> buildModels() {
        Map<String, String> models = Maps.newLinkedHashMap();
        for (ModelRow row : this.modelRows) {
            String id = StringUtils.trimToEmpty(row.id());
            String name = StringUtils.trimToEmpty(row.name());
            // 需要 id 和名称都不为空，并且 id 不能重复
            if (StringUtils.isBlank(id) || StringUtils.isBlank(name) || models.containsKey(id)) {
                continue;
            }
            models.put(id, name);
        }
        return models;
    }

    private String getFieldValue(String label) {
        return this.fields.stream()
                .filter(field -> field.label.equals(label))
                .findFirst()
                .map(FormField::value)
                .orElse(StringUtils.EMPTY);
    }

    private void showStatus(Component message) {
        this.statusMessage = message;
        this.tipTimestamp = System.currentTimeMillis();
    }

    private static final class ModelRow {
        private String id;
        private String name;
        private EditBox idBox;
        private EditBox nameBox;

        private ModelRow(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private void syncFromBox() {
            if (this.idBox != null) {
                this.id = this.idBox.getValue();
            }
            if (this.nameBox != null) {
                this.name = this.nameBox.getValue();
            }
        }

        private String id() {
            return this.idBox != null ? this.idBox.getValue() : this.id;
        }

        private String name() {
            return this.nameBox != null ? this.nameBox.getValue() : this.name;
        }
    }
}

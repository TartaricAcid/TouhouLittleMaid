package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.FieldDescriptor;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.TTSSiteFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsHubScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsTTSSiteScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.ai.SaveTTSSiteMessage;
import com.github.tartaricacid.touhoulittlemaid.util.Rectangle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;
import static net.minecraft.network.chat.CommonComponents.GUI_BACK;

/**
 * TTS 站点编辑器：固定字段区 + 可滚动模型列表。
 * 表单字段定义委托给 {@link TTSSiteFormLayout} 的各子类。
 */
public class TTSSiteEditorScreen extends Screen {
    private static final int LABEL_COLOR = 0xFF777777;
    private static final int BASE_WIDTH = 400;
    private static final int BASE_HEIGHT = 230;
    private static final int FIELD_ROW_HEIGHT = 35;
    private static final int MODEL_ROW_HEIGHT = 22;

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

    /**
     * 模型列表滚动区域
     */
    private Rectangle modelArea;
    private int modelScrollOffset;

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
        return super.addRenderableWidget(pWidget);
    }

    @Override
    protected void init() {
        // 在缩放窗口时，更新输入框的值
        this.fields.forEach(FormField::syncFromBox);
        this.modelRows.forEach(ModelRow::syncFromBox);

        this.clearWidgets();

        this.startX = (this.width - BASE_WIDTH) / 2;
        this.startY = (this.height - BASE_HEIGHT) / 2;

        int left = this.startX + 12;
        int contentWidth = BASE_WIDTH - 24;
        int bottomY = this.startY + BASE_HEIGHT - 24;

        // 固定字段区（不滚动）
        int fieldY = this.startY + 28;
        int fieldMaxSize = this.fields.size();
        // 奇数，那么最后一个占一整行，否则是均分，左右各一个
        boolean isOdd = fieldMaxSize % 2 == 1;
        for (int i = 0; i < fieldMaxSize; i++) {
            FormField field = this.fields.get(i);
            // 最后一行，奇数，占一整行
            if (isOdd && i == fieldMaxSize - 1) {
                // 偶数且在中间位置，跳过到下一行
                this.createFieldWidget(field, left, fieldY, contentWidth);
                fieldY += FIELD_ROW_HEIGHT;
            } else {
                boolean isLeft = i % 2 == 0;
                int fieldWidth = (contentWidth - 6) / 2;
                this.createFieldWidget(field, left + (isLeft ? 0 : fieldWidth + 6), fieldY, fieldWidth);
                if (!isLeft) {
                    fieldY += FIELD_ROW_HEIGHT;
                }
            }
        }

        // 额外组件
        fieldY += layout.extraInit(left, fieldY, contentWidth, this);

        // 可滚动模型区
        if (this.layout.supportsModelRows()) {
            int modelTop = fieldY + 14;
            int modelBottom = this.startY + BASE_HEIGHT - 48;
            this.modelArea = new Rectangle(left, modelTop, contentWidth, modelBottom - modelTop);
            this.createModelRows(left, contentWidth);

            this.addRenderableWidget(new FlatColorButton(left, bottomY, 96, 20, ADD_MODEL_NAME, b -> {
                this.modelRows.add(new ModelRow(StringUtils.EMPTY, StringUtils.EMPTY));
                int visibleCount = this.getVisibleModelCount();
                this.modelScrollOffset = Math.max(0, this.modelRows.size() - visibleCount);
                this.init();
            }));
        }

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
            this.addWidget(row.idBox);

            row.nameBox = new EditBox(this.font, left + 16 + idWidth + gap, rowY + 2, nameWidth - 26, 16, Component.literal("Display Name"));
            row.nameBox.setMaxLength(512);
            row.nameBox.setBordered(false);
            row.nameBox.setValue(preName);
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

        super.render(graphics, mouseX, mouseY, partialTick);

        // 保存提示
        if (System.currentTimeMillis() - this.tipTimestamp < 2000) {
            int x = this.startX + BASE_WIDTH - 155;
            int y = this.startY + BASE_HEIGHT - 35;
            graphics.drawCenteredString(this.font, this.statusMessage, x, y, 0xFFADADAD);
        }
    }

    private void renderInputField(GuiGraphics graphics, EditBox box, int mouseX, int mouseY, float partialTick) {
        if (box == null) {
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

        // 主标题
        graphics.drawString(this.font, this.layout.modelsTitle(), left + 2, top - 14, LABEL_COLOR, false);

        int visibleCount = this.getVisibleModelCount();
        int startIndex = this.modelScrollOffset;
        int endIndex = Math.min(this.modelRows.size(), startIndex + visibleCount);

        graphics.enableScissor(left, top - 2, (int) this.modelArea.right(), (int) this.modelArea.bottom() + 2);
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
            int trackTop = top - 2;
            // 滚动条轨道的总高度
            int trackHeight = (int) this.modelArea.h - 8;
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
        if (this.modelArea != null && this.modelArea.contains(mouseX, mouseY)) {
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
        this.fields.stream().map(field -> field.box).filter(Objects::nonNull).forEach(EditBox::tick);
        this.modelRows.stream().map(row -> row.idBox).filter(Objects::nonNull).forEach(EditBox::tick);
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

    private int getVisibleModelCount() {
        return Math.max(1, (int) ((this.modelArea.h - 4) / MODEL_ROW_HEIGHT));
    }

    private void saveSite() {
        Map<String, String> models = this.buildModels();
        TTSSite site = this.layout.buildSite(this::getFieldValue, models, this::showStatus);
        if (site == null) {
            return;
        }
        NetworkHandler.CHANNEL.sendToServer(SaveTTSSiteMessage.update(site));
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

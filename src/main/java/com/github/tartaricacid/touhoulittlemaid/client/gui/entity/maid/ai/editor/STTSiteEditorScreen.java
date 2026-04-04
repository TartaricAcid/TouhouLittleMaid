package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.FieldDescriptor;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout.STTSiteFormLayout;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings.AIChatSettingsSTTSiteScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.SAVE_NAME;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.sttEditorTitle;
import static net.minecraft.network.chat.CommonComponents.GUI_BACK;

public class STTSiteEditorScreen extends Screen {
    private static final int BASE_WIDTH = 400;
    private static final int BASE_HEIGHT = 230;
    private static final int FIELD_ROW_HEIGHT = 35;

    private final AIChatSettingsSTTSiteScreen parent;
    private final STTSiteFormLayout layout;
    private final String siteDisplayName;

    private final List<FormField> fields = Lists.newArrayList();

    private int startX;
    private int startY;

    /**
     * 保存时的提示信息
     */
    private long tipTimestamp = -1;
    private Component statusMessage = Component.empty();

    public STTSiteEditorScreen(AIChatSettingsSTTSiteScreen parent, STTSite sourceSite) {
        super(Component.literal("STT Site Editor"));
        this.parent = parent;
        this.layout = sourceSite.formLayout();

        String nameKey = sourceSite.getNameKey();
        this.siteDisplayName = I18n.exists(nameKey) ? I18n.get(nameKey) : sourceSite.id();

        this.initStateFromLayout();
    }

    private void initStateFromLayout() {
        this.fields.clear();

        for (FieldDescriptor desc : this.layout.getFieldDescriptors()) {
            this.fields.add(new FormField(desc.label(), desc.value(), desc.editable(), desc.secret()));
        }
    }

    @Override
    protected void init() {
        // 在缩放窗口时，更新输入框的值
        this.fields.forEach(FormField::syncFromBox);

        this.clearWidgets();

        this.startX = (this.width - BASE_WIDTH) / 2;
        this.startY = (this.height - BASE_HEIGHT) / 2;

        int left = this.startX + 12;
        int contentWidth = BASE_WIDTH - 24;

        int fieldY = this.startY + 28;
        int fieldMaxSize = this.fields.size();
        boolean isOdd = fieldMaxSize % 2 == 1;
        for (int i = 0; i < fieldMaxSize; i++) {
            FormField field = this.fields.get(i);
            if (isOdd && i == fieldMaxSize - 1) {
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

        int bottomY = this.startY + BASE_HEIGHT - 24;
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

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(0, 0, this.width, this.height, 0xc0101010, 0xc0101010);

        // 居中标题
        graphics.drawCenteredString(this.font, sttEditorTitle(this.siteDisplayName),
                this.startX + BASE_WIDTH / 2, this.startY + 4, 0xFFF3EFE0);

        // 文本框
        for (FormField field : this.fields) {
            this.renderInputField(graphics, field.box, mouseX, mouseY, partialTick);
        }

        super.render(graphics, mouseX, mouseY, partialTick);

        // 警告信息
        if (System.currentTimeMillis() - this.tipTimestamp < 2000) {
            int x = this.startX + BASE_WIDTH - 155;
            int y = this.startY + BASE_HEIGHT - 35;
            graphics.drawCenteredString(this.font, this.statusMessage, x, y, 0xFFFF7777);
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

        graphics.drawString(this.font, box.getMessage(), x + 2, y - 12, 0xFF777777, false);
        graphics.fill(x, y, x + width, y + height, 0xAA111111);
        box.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        this.fields.stream().map(f -> f.box)
                .filter(Objects::nonNull)
                .forEach(EditBox::tick);
        super.tick();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    private void saveSite() {
        STTSite site = this.layout.buildSite(this::getFieldValue, this::showStatus);
        if (site == null) {
            return;
        }
        this.parent.saveLocalSTTSite(site);
        this.onClose();
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
}

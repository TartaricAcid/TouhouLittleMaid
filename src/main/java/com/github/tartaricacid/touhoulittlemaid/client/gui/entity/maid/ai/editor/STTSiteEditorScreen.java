package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.STTAliyunSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.STTPlayer2Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.siliconflow.STTSiliconflowSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.*;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;
import static net.minecraft.network.chat.CommonComponents.GUI_BACK;

/**
 * STT 站点编辑器，根据站点类型展示不同的表单字段
 */
public class STTSiteEditorScreen extends Screen {
    private static final int BASE_WIDTH = 400;
    private static final int BASE_HEIGHT = 230;
    private static final int ROW_HEIGHT = 38;

    private final AIChatSettingsSTTSiteScreen parent;
    private final STTSite sourceSite;
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
        this.sourceSite = sourceSite;

        String nameKey = sourceSite.getNameKey();
        this.siteDisplayName = I18n.exists(nameKey) ? I18n.get(nameKey) : sourceSite.id();

        this.initStateFromSite();
    }

    private void initStateFromSite() {
        this.fields.clear();

        // Player2：仅 URL 可编辑，提示文本在 render 中直接画
        if (this.sourceSite instanceof STTPlayer2Site site) {
            this.fields.add(FormField.urlField(site.url()));
            return;
        }

        // SiliconFlow：URL + Key + 单个模型名
        if (this.sourceSite instanceof STTSiliconflowSite site) {
            this.fields.add(FormField.urlField(site.url()));
            this.fields.add(FormField.secretKeyField(site.getSecretKey()));
            this.fields.add(FormField.modelsField(site.getModel()));
            return;
        }

        // 阿里云：URL + Key + App Key
        if (this.sourceSite instanceof STTAliyunSite site) {
            this.fields.add(FormField.urlField(site.getBaseUrl()));
            this.fields.add(FormField.secretKeyField(site.getSecretKey()));
            this.fields.add(FormField.appKeyField(site.getAppKey()));
        }
    }

    @Override
    protected void init() {
        // 在缩放窗口时，更新输入框的值
        this.fields.forEach(FormField::syncFromBox);

        this.clearWidgets();

        this.startX = (this.width - BASE_WIDTH) / 2;
        this.startY = (this.height - BASE_HEIGHT) / 2;

        for (int i = 0; i < this.fields.size(); i++) {
            int y = this.startY + 34 + i * ROW_HEIGHT;
            this.createFieldWidget(this.fields.get(i), this.startX + 12, y, BASE_WIDTH - 24);
        }

        int bottomY = this.startY + BASE_HEIGHT - 24;
        this.addRenderableWidget(new FlatColorButton(this.startX + BASE_WIDTH - 200, bottomY, 90, 20, SAVE_NAME, b -> this.saveSite()));
        this.addRenderableWidget(new FlatColorButton(this.startX + BASE_WIDTH - 102, bottomY, 90, 20, GUI_BACK, b -> this.onClose()));
    }

    private void createFieldWidget(FormField field, int left, int y, int width) {
        EditBox box = new EditBox(this.font, left + 6, y + 14, width - 12, 16, field.i18nName());
        box.setMaxLength(512);
        box.setBordered(false);
        box.visible = field.editable;
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
        STTSite site = this.buildSite();
        if (site == null) {
            return;
        }
        this.parent.saveLocalSTTSite(site);
        this.onClose();
    }

    @Nullable
    private STTSite buildSite() {
        if (this.sourceSite instanceof STTPlayer2Site site) {
            String url = StringUtils.trimToEmpty(this.getFieldValue(URL));
            if (StringUtils.isBlank(url)) {
                this.showStatus(URL_IS_EMPTY);
                return null;
            }
            return new STTPlayer2Site(site.id(), site.icon(), url, site.enabled(), site.headers());
        }

        if (this.sourceSite instanceof STTSiliconflowSite site) {
            String url = StringUtils.trimToEmpty(this.getFieldValue(URL));
            if (StringUtils.isBlank(url)) {
                this.showStatus(URL_IS_EMPTY);
                return null;
            }
            return new STTSiliconflowSite(site.id(), site.icon(), site.enabled(), url,
                    StringUtils.trimToEmpty(this.getFieldValue(SECRET_KEY)),
                    StringUtils.trimToEmpty(this.getFieldValue(MODELS)));
        }

        if (this.sourceSite instanceof STTAliyunSite site) {
            String baseUrl = StringUtils.trimToEmpty(this.getFieldValue(URL));
            if (StringUtils.isBlank(baseUrl)) {
                this.showStatus(URL_IS_EMPTY);
                return null;
            }
            return new STTAliyunSite(site.id(), site.icon(), site.enabled(), baseUrl,
                    StringUtils.trimToEmpty(this.getFieldValue(SECRET_KEY)),
                    StringUtils.trimToEmpty(this.getFieldValue(APP_KEY)),
                    site.getVocabularyId(),
                    site.getCustomizationId(),
                    site.isEnablePunctuationPrediction(),
                    site.isEnableInverseTextNormalization(),
                    site.isEnableVoiceDetection(),
                    site.isDisfluency());
        }
        return null;
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

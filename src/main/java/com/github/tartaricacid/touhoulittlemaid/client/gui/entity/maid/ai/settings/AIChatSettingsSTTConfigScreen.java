package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.settings;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.ai.MaidChatDistanceSlider;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.*;

/**
 * STT 全局配置标签页：麦克风选择、识别距离、代理地址等
 */
public class AIChatSettingsSTTConfigScreen extends AIChatSettingsHubScreen {
    private static final int LABEL_COLOR = 0xFF777777;

    private String[] microphoneNames = new String[0];

    private FlatColorButton enableBtn;
    private FlatColorButton typeBtn;
    private FlatColorButton microphoneBtn;
    private MaidChatDistanceSlider distanceSlider;
    private EditBox proxyInput;

    public AIChatSettingsSTTConfigScreen(@Nullable Screen parent, SharedState state, boolean insufficientPermissions) {
        super(parent, state, insufficientPermissions);
    }

    @Override
    protected Type getType() {
        return Type.STT_CONFIG;
    }

    @Override
    protected void init() {
        this.syncInputsToState();
        super.init();
    }

    @Override
    protected void initContent() {
        int x = this.getContentX();
        int width = this.getContentWidth();

        this.enableBtn = this.addRenderableWidget(new FlatColorButton(x, this.getContentY() + 12, 96, 20, sttEnable(this.state.sttEnabled), b -> {
            this.state.sttEnabled = !this.state.sttEnabled;
            this.init();
        }));

        MutableComponent siteName = Component.translatable("ai.touhou_little_maid.chat.site.%s.name".formatted(this.state.sttType.getName()));
        this.typeBtn = this.addRenderableWidget(new FlatColorButton(x + 104, this.getContentY() + 12, width - 104, 20, siteName, b -> {
            STTApiType[] values = STTApiType.values();
            int ordinal = this.state.sttType.ordinal();
            this.state.sttType = values[(ordinal + 1) % values.length];
            this.init();
        }));

        MutableComponent microphoneName = Component.literal(this.normalizeMicrophone(this.state.sttMicrophone));
        this.microphoneBtn = this.addRenderableWidget(new FlatColorButton(x, this.getContentY() + 50, width, 20, microphoneName, b -> {
            this.state.sttMicrophone = this.getNextMicrophone(this.state.sttMicrophone);
            this.init();
        }));

        this.distanceSlider = new MaidChatDistanceSlider(x, this.getContentY() + 86, width, 20, this.state);
        this.addRenderableWidget(this.distanceSlider);

        MutableComponent proxyTitle = Component.translatable("config.touhou_little_maid.global_ai.stt_proxy_address");
        this.proxyInput = this.addInput(x, this.getContentY() + 122, width, proxyTitle, this.state.sttProxyAddress);
    }

    @Override
    protected void addFooterButtons() {
        super.addFooterButtons();

        int x = this.getContentX();
        int y = this.getFooterY();

        this.addRenderableWidget(new FlatColorButton(x, y, 80, 20, SAVE_NAME, b -> this.saveChanges()));
        this.addRenderableWidget(new FlatColorButton(x + 82, y, 80, 20, SAVE_QUIT_NAME, b -> {
            this.saveChanges();
            this.onClose();
        }));
    }

    private void saveChanges() {
        this.syncInputsToState();

        AIConfig.STT_ENABLED.set(this.state.sttEnabled);
        AIConfig.STT_TYPE.set(this.state.sttType);
        AIConfig.STT_MICROPHONE.set(this.state.sttMicrophone);
        AIConfig.MAID_CAN_CHAT_DISTANCE.set(this.state.maidCanChatDistance);
        AIConfig.STT_PROXY_ADDRESS.set(this.state.sttProxyAddress);

        if (GeneralConfig.CONFIG != null) {
            GeneralConfig.CONFIG.save();
        }
    }

    @Override
    protected void persistTransientState() {
        this.syncInputsToState();
    }

    private void syncInputsToState() {
        this.microphoneNames = MicrophoneManager.getAllMicrophoneName();
        // 麦克风可能已被拔出，需要校验并回退到可用设备
        this.state.sttMicrophone = this.normalizeMicrophone(this.state.sttMicrophone);
        if (this.distanceSlider != null) {
            this.state.maidCanChatDistance = this.distanceSlider.getDistanceValue();
        }
        if (this.proxyInput != null) {
            this.state.sttProxyAddress = StringUtils.trimToEmpty(this.proxyInput.getValue());
        }
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
    public void tick() {
        if (this.proxyInput != null) {
            this.proxyInput.tick();
        }
        super.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        MutableComponent enable = Component.translatable("config.touhou_little_maid.global_ai.stt_enable");
        graphics.drawString(this.font, enable, this.enableBtn.getX() + 2, this.enableBtn.getY() - 12, LABEL_COLOR, false);

        MutableComponent type = Component.translatable("config.touhou_little_maid.global_ai.stt_type");
        graphics.drawString(this.font, type, this.typeBtn.getX() + 2, this.typeBtn.getY() - 12, LABEL_COLOR, false);

        MutableComponent microphone = Component.translatable("config.touhou_little_maid.global_ai.stt_microphone");
        graphics.drawString(this.font, microphone, this.microphoneBtn.getX() + 2, this.microphoneBtn.getY() - 12, LABEL_COLOR, false);

        MutableComponent distance = Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance");
        graphics.drawString(this.font, distance, this.distanceSlider.getX() + 2, this.distanceSlider.getY() - 12, LABEL_COLOR, false);

        this.renderInputField(graphics, this.proxyInput, mouseX, mouseY, partialTick);
    }

    private void renderInputField(GuiGraphics graphics, @Nullable EditBox box, int mouseX, int mouseY, float partialTick) {
        if (box == null) {
            return;
        }
        int x = box.getX() - 6;
        int y = box.getY() - 6;
        int width = box.getInnerWidth() + 12;
        int height = box.getHeight() + 3;

        graphics.drawString(this.font, box.getMessage(), x + 2, y - 12, LABEL_COLOR, false);
        graphics.fill(x, y, x + width, y + height, 0xAA111111);
        box.render(graphics, mouseX, mouseY, partialTick);
    }

    private String normalizeMicrophone(String current) {
        if (this.microphoneNames.length == 0) {
            return StringUtils.EMPTY;
        }
        for (String microphoneName : this.microphoneNames) {
            if (microphoneName.equals(current)) {
                return microphoneName;
            }
        }
        return this.microphoneNames[0];
    }

    private String getNextMicrophone(String current) {
        if (this.microphoneNames.length == 0) {
            return StringUtils.EMPTY;
        }
        String normalized = this.normalizeMicrophone(current);
        for (int i = 0; i < this.microphoneNames.length; i++) {
            if (this.microphoneNames[i].equals(normalized)) {
                return this.microphoneNames[(i + 1) % this.microphoneNames.length];
            }
        }
        return this.microphoneNames[0];
    }
}

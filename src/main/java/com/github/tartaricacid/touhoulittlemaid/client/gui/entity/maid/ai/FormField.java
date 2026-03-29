package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 表单文本输入字段，用于 TTS / STT 编辑器中的通用输入行
 */
public class FormField {
    public static final String URL = "url";
    public static final String SECRET_ID = "secret_id";
    public static final String SECRET_KEY = "secret_key";
    public static final String MODEL = "model";
    public static final String MODELS = "models";
    public static final String APP_KEY = "app_key";
    public static final String ENG_SER_VICE_TYPE = "eng_ser_vice_type";
    public static final String HOT_WORD = "hot_word";
    public static final String REF_AUDIO_PATH = "ref_audio_path";
    public static final String PROMPT_TEXT = "prompt_text";

    public final String label;
    public final boolean editable;
    public final boolean secret;

    public String value;
    public EditBox box;

    public FormField(String label, String value, boolean editable, boolean secret) {
        this.label = label;
        this.value = value;
        this.editable = editable;
        this.secret = secret;
    }

    public void syncFromBox() {
        if (this.box != null) {
            this.value = this.box.getValue();
        }
    }

    public String value() {
        return this.box != null ? this.box.getValue() : this.value;
    }

    public MutableComponent i18nName() {
        return Component.translatable("ai.touhou_little_maid.chat.settings.hub.%s".formatted(this.label));
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.layout;

import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSite;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.TTSGptSovitsSite;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.editor.TTSSiteEditorScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.FormField.*;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.Translations.URL_IS_EMPTY;


/**
 * GPT-SoVITS TTS：URL + Secret Key + 参考音频 + 提示文本 + 语言/切分选项
 */
public class TTSGptSovitsFormLayout extends TTSSiteFormLayout {
    private static final List<String> PROMPT_LANG_OPTIONS = List.of("en", "zh", "jp", "auto");
    private static final List<String> TEXT_SPLIT_METHOD_OPTIONS = List.of("cut0", "cut1", "cut2", "cut3", "cut4", "cut5");

    private String promptLangValue;
    private String textSplitMethodValue;

    public TTSGptSovitsFormLayout(TTSSite sourceSite) {
        super(sourceSite);
        TTSGptSovitsSite site = (TTSGptSovitsSite) sourceSite;
        this.promptLangValue = site.promptLang();
        this.textSplitMethodValue = site.textSplitMethod();
    }

    @Override
    public List<FieldDescriptor> getFieldDescriptors() {
        TTSGptSovitsSite site = (TTSGptSovitsSite) this.sourceSite;
        return List.of(
                new FieldDescriptor(URL, site.url(), true, false),
                new FieldDescriptor(SECRET_KEY, site.secretKey(), true, true),
                new FieldDescriptor(REF_AUDIO_PATH, site.refAudioPath(), true, false),
                new FieldDescriptor(PROMPT_TEXT, site.promptText(), true, false)
        );
    }

    @Override
    public int extraInit(int x, int y, int width, TTSSiteEditorScreen screen) {
        int oneThirdWidth = width / 3;

        screen.addRenderableWidget(new FlatColorButton(x, y + 2, oneThirdWidth, 18,
                this.promptLangName(), b -> b.setMessage(this.nextPromptLang())
        ));

        screen.addRenderableWidget(new FlatColorButton(x + oneThirdWidth + 4, y + 2, oneThirdWidth * 2 - 4, 18,
                this.textSplitMethodName(), b -> b.setMessage(this.nextTextSplitMethod())
        ));

        return 35;
    }

    public MutableComponent promptLangName() {
        return Component.translatable("ai.touhou_little_maid.chat.settings.hub.gpt_sovits.lang.%s".formatted(this.promptLangValue));
    }

    public MutableComponent textSplitMethodName() {
        return Component.translatable("ai.touhou_little_maid.chat.settings.hub.gpt_sovits.split.%s".formatted(this.textSplitMethodValue));
    }

    public MutableComponent nextPromptLang() {
        int index = PROMPT_LANG_OPTIONS.indexOf(this.promptLangValue);
        if (index == -1) {
            this.promptLangValue = PROMPT_LANG_OPTIONS.get(0);
        } else {
            int nextIndex = (index + 1) % PROMPT_LANG_OPTIONS.size();
            this.promptLangValue = PROMPT_LANG_OPTIONS.get(nextIndex);
        }
        return promptLangName();
    }

    public MutableComponent nextTextSplitMethod() {
        int index = TEXT_SPLIT_METHOD_OPTIONS.indexOf(this.textSplitMethodValue);
        if (index == -1) {
            this.textSplitMethodValue = TEXT_SPLIT_METHOD_OPTIONS.get(0);
        } else {
            int nextIndex = (index + 1) % TEXT_SPLIT_METHOD_OPTIONS.size();
            this.textSplitMethodValue = TEXT_SPLIT_METHOD_OPTIONS.get(nextIndex);
        }
        return textSplitMethodName();
    }

    @Override
    public @Nullable TTSSite buildSite(Function<String, String> fieldValues, Map<String, String> models, Consumer<Component> showStatus) {
        TTSGptSovitsSite site = (TTSGptSovitsSite) this.sourceSite;
        String url = fieldValues.apply(URL);
        if (StringUtils.isBlank(url)) {
            showStatus.accept(URL_IS_EMPTY);
            return null;
        }
        return new TTSGptSovitsSite(site.id(), site.icon(), url, site.enabled(),
                fieldValues.apply(SECRET_KEY),
                fieldValues.apply(REF_AUDIO_PATH),
                fieldValues.apply(PROMPT_TEXT),
                this.promptLangValue,
                this.textSplitMethodValue,
                site.auxRefAudioPaths(),
                site.headers()
        );
    }
}

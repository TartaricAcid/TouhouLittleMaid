package com.github.tartaricacid.touhoulittlemaid.compat.cloth;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.SortedMap;

public class GlobalAIIntegration {
    private static final String DEFAULT_LANGUAGE = "en_us";

    public static void aiChat(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        ConfigCategory aiChat = root.getOrCreateCategory(Component.translatable("config.touhou_little_maid.global_ai"));
        llmConfig(entryBuilder, aiChat);
        ttsConfig(entryBuilder, aiChat);
        sttConfig(entryBuilder, aiChat);
    }

    private static void sttConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.stt"));
        builder.setExpanded(true);

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.stt_enable"), AIConfig.STT_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_enable.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.STT_ENABLED.set(s);
                    AIConfig.STT_ENABLED.save();
                }).build());

        builder.add(entryBuilder.startEnumSelector(Component.translatable("config.touhou_little_maid.global_ai.stt_type"), STTApiType.class, AIConfig.STT_TYPE.get())
                .setDefaultValue(STTApiType.PLAYER2).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_type.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.STT_TYPE.set(s);
                    AIConfig.STT_TYPE.save();
                }).build());

        builder.add(entryBuilder.startSelector(Component.translatable("config.touhou_little_maid.global_ai.stt_microphone"),
                        MicrophoneManager.getAllMicrophoneName(), AIConfig.STT_MICROPHONE.get())
                .setDefaultValue(StringUtils.EMPTY).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_microphone.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.STT_MICROPHONE.set(s);
                    AIConfig.STT_MICROPHONE.save();
                }).build());

        builder.add(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance"),
                        AIConfig.MAID_CAN_CHAT_DISTANCE.get(), 1, 256).setDefaultValue(12)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.MAID_CAN_CHAT_DISTANCE.set(s);
                    AIConfig.MAID_CAN_CHAT_DISTANCE.save();
                }).build());

        builder.add(entryBuilder.startStrField(Component.translatable("config.touhou_little_maid.global_ai.stt_proxy_address"), AIConfig.STT_PROXY_ADDRESS.get())
                .setDefaultValue(StringUtils.EMPTY)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_proxy_address.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.STT_PROXY_ADDRESS.set(s);
                    AIConfig.STT_PROXY_ADDRESS.save();
                }).build());

        aiChat.addEntry(builder.build());
    }

    private static void ttsConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.tts"));
        builder.setExpanded(true);

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.tts_enable"), AIConfig.TTS_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_enable.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.TTS_ENABLED.set(s);
                    AIConfig.TTS_ENABLED.save();
                }).build());

        builder.add(entryBuilder.startStrField(Component.translatable("config.touhou_little_maid.global_ai.tts_proxy_address"), AIConfig.TTS_PROXY_ADDRESS.get())
                .setDefaultValue(StringUtils.EMPTY)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_proxy_address.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.TTS_PROXY_ADDRESS.set(s);
                    AIConfig.TTS_PROXY_ADDRESS.save();
                }).build());

        SortedMap<String, LanguageInfo> languages = Minecraft.getInstance().getLanguageManager().getLanguages();
        builder.add(entryBuilder.startStringDropdownMenu(Component.translatable("config.touhou_little_maid.global_ai.tts_language"),
                        AIConfig.TTS_LANGUAGE.get(), Component::literal, cell(languages)).setSelections(languages.keySet())
                .setDefaultValue(DEFAULT_LANGUAGE).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_language.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.TTS_LANGUAGE.set(s);
                    AIConfig.TTS_LANGUAGE.save();
                }).build());

        aiChat.addEntry(builder.build());
    }

    private static void llmConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.llm"));
        builder.setExpanded(true);

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.llm_enable"), AIConfig.LLM_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.llm_enable.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.LLM_ENABLED.set(s);
                    AIConfig.LLM_ENABLED.save();
                }).build());

        builder.add(entryBuilder.startDoubleField(Component.translatable("config.touhou_little_maid.global_ai.llm_temperature"), AIConfig.LLM_TEMPERATURE.get())
                .setDefaultValue(AIConfig.LLM_TEMPERATURE.getDefault()).setMin(0.0).setMax(2.0)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.llm_temperature.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.LLM_TEMPERATURE.set(s);
                    AIConfig.LLM_TEMPERATURE.save();
                }).build());

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.function_call_enabled"), AIConfig.FUNCTION_CALL_ENABLED.get())
                .setDefaultValue(false).setTooltip(
                        Component.translatable("config.touhou_little_maid.global_ai.function_call_enabled.1.tooltip"),
                        Component.translatable("config.touhou_little_maid.global_ai.function_call_enabled.2.tooltip")
                ).setSaveConsumer(s -> {
                    AIConfig.FUNCTION_CALL_ENABLED.set(s);
                    AIConfig.FUNCTION_CALL_ENABLED.save();
                }).build());

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.auto_gen_setting_enabled"), AIConfig.AUTO_GEN_SETTING_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.auto_gen_setting_enabled.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.AUTO_GEN_SETTING_ENABLED.set(s);
                    AIConfig.AUTO_GEN_SETTING_ENABLED.save();
                }).build());

        builder.add(entryBuilder.startStrField(Component.translatable("config.touhou_little_maid.global_ai.llm_proxy_address"), AIConfig.LLM_PROXY_ADDRESS.get())
                .setDefaultValue(StringUtils.EMPTY)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.llm_proxy_address.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.LLM_PROXY_ADDRESS.set(s);
                    AIConfig.LLM_PROXY_ADDRESS.save();
                }).build());

        builder.add(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.global_ai.llm_max_token"), AIConfig.LLM_MAX_TOKEN.get())
                .setDefaultValue(4096)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.llm_max_token.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.LLM_MAX_TOKEN.set(s);
                    AIConfig.LLM_MAX_TOKEN.save();
                }).build());

        builder.add(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.global_ai.maid_max_history_llm_size"),
                        AIConfig.MAID_MAX_HISTORY_LLM_SIZE.get(), 1, 128).setDefaultValue(16)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.maid_max_history_llm_size.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.MAID_MAX_HISTORY_LLM_SIZE.set(s);
                    AIConfig.MAID_MAX_HISTORY_LLM_SIZE.save();
                }).build());

        builder.add(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.global_ai.max_tokens_per_player"), AIConfig.MAX_TOKENS_PER_PLAYER.get())
                .setDefaultValue(Integer.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.max_tokens_per_player.tooltip"))
                .setSaveConsumer(s -> {
                    AIConfig.MAX_TOKENS_PER_PLAYER.set(s);
                    AIConfig.MAX_TOKENS_PER_PLAYER.save();
                }).build());

        aiChat.addEntry(builder.build());
    }

    private static DropdownBoxEntry.SelectionCellCreator<String> cell(SortedMap<String, LanguageInfo> languages) {
        LanguageInfo defaultLanguage = languages.get(DEFAULT_LANGUAGE);
        return new DropdownBoxEntry.DefaultSelectionCellCreator<>(i -> languages.getOrDefault(i, defaultLanguage).toComponent());
    }
}

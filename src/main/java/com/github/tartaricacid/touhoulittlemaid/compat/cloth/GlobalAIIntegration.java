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
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.SortedMap;

public class GlobalAIIntegration {
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
                .setSaveConsumer(AIConfig.STT_ENABLED::set).build());

        builder.add(entryBuilder.startEnumSelector(Component.translatable("config.touhou_little_maid.global_ai.stt_type"), STTApiType.class, AIConfig.STT_TYPE.get())
                .setDefaultValue(STTApiType.PLAYER2).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_type.tooltip"))
                .setSaveConsumer(AIConfig.STT_TYPE::set).build());

        builder.add(entryBuilder.startSelector(Component.translatable("config.touhou_little_maid.global_ai.stt_microphone"),
                        MicrophoneManager.getAllMicrophoneName(), AIConfig.STT_MICROPHONE.get())
                .setDefaultValue(StringUtils.EMPTY).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_microphone.tooltip"))
                .setSaveConsumer(AIConfig.STT_MICROPHONE::set).build());

        builder.add(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance"),
                        AIConfig.MAID_CAN_CHAT_DISTANCE.get(), 1, 256).setDefaultValue(12)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance.tooltip"))
                .setSaveConsumer(AIConfig.MAID_CAN_CHAT_DISTANCE::set).build());

        builder.add(entryBuilder.startStrField(Component.translatable("config.touhou_little_maid.global_ai.stt_proxy_address"), AIConfig.STT_PROXY_ADDRESS.get())
                .setDefaultValue(StringUtils.EMPTY)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_proxy_address.tooltip"))
                .setSaveConsumer(AIConfig.STT_PROXY_ADDRESS::set).build());

        aiChat.addEntry(builder.build());
    }

    private static void ttsConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.tts"));
        builder.setExpanded(true);

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.tts_enable"), AIConfig.TTS_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_enable.tooltip"))
                .setSaveConsumer(AIConfig.TTS_ENABLED::set).build());

        builder.add(entryBuilder.startStrField(Component.translatable("config.touhou_little_maid.global_ai.tts_proxy_address"), AIConfig.TTS_PROXY_ADDRESS.get())
                .setDefaultValue(StringUtils.EMPTY)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_proxy_address.tooltip"))
                .setSaveConsumer(AIConfig.TTS_PROXY_ADDRESS::set).build());

        SortedMap<String, LanguageInfo> languages = Minecraft.getInstance().getLanguageManager().getLanguages();
        builder.add(entryBuilder.startStringDropdownMenu(Component.translatable("config.touhou_little_maid.global_ai.tts_language"),
                        AIConfig.TTS_LANGUAGE.get(), Component::literal, cell(languages)).setSelections(languages.keySet())
                .setDefaultValue(LanguageManager.DEFAULT_LANGUAGE_CODE).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_language.tooltip"))
                .setSaveConsumer(info -> AIConfig.TTS_LANGUAGE.set(info)).build());

        aiChat.addEntry(builder.build());
    }

    private static void llmConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.llm"));
        builder.setExpanded(true);

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.llm_enable"), AIConfig.LLM_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.llm_enable.tooltip"))
                .setSaveConsumer(AIConfig.LLM_ENABLED::set).build());

        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.auto_gen_setting_enabled"), AIConfig.AUTO_GEN_SETTING_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.auto_gen_setting_enabled.tooltip"))
                .setSaveConsumer(AIConfig.AUTO_GEN_SETTING_ENABLED::set).build());

        builder.add(entryBuilder.startStrField(Component.translatable("config.touhou_little_maid.global_ai.llm_proxy_address"), AIConfig.LLM_PROXY_ADDRESS.get())
                .setDefaultValue(StringUtils.EMPTY)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.llm_proxy_address.tooltip"))
                .setSaveConsumer(AIConfig.LLM_PROXY_ADDRESS::set).build());

        builder.add(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.global_ai.maid_max_history_llm_size"),
                        AIConfig.MAID_MAX_HISTORY_LLM_SIZE.get(), 1, 128).setDefaultValue(24)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.maid_max_history_llm_size.tooltip"))
                .setSaveConsumer(AIConfig.MAID_MAX_HISTORY_LLM_SIZE::set).build());

        builder.add(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid.global_ai.max_tokens_per_player"), AIConfig.MAX_TOKENS_PER_PLAYER.get())
                .setDefaultValue(Integer.MAX_VALUE)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.max_tokens_per_player.tooltip"))
                .setSaveConsumer(AIConfig.MAX_TOKENS_PER_PLAYER::set).build());

        aiChat.addEntry(builder.build());
    }

    private static DropdownBoxEntry.SelectionCellCreator<String> cell(SortedMap<String, LanguageInfo> languages) {
        LanguageInfo defaultLanguage = languages.get(LanguageManager.DEFAULT_LANGUAGE_CODE);
        return new DropdownBoxEntry.DefaultSelectionCellCreator<>(i -> languages.getOrDefault(i, defaultLanguage).toComponent());
    }
}

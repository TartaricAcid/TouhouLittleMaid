package com.github.tartaricacid.touhoulittlemaid.compat.cloth;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.STTApiType;
import com.github.tartaricacid.touhoulittlemaid.client.sound.record.MicrophoneManager;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabClientState;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.SortedMap;
import java.util.function.Consumer;

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

        // 宿主说自己能听写：绑了角色的女仆按 X 那一句直接发给 WAB，游戏里这一整套识别配置对她们不再走。
        boolean taken = WabClientState.sttByWab();
        note(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.stt.wab_taken");

        toggle(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.stt_enable",
                true, AIConfig.STT_ENABLED.get(), AIConfig.STT_ENABLED::set);

        if (taken) {
            readonly(builder, entryBuilder, "config.touhou_little_maid.global_ai.stt_type",
                    Component.literal(AIConfig.STT_TYPE.get().getName()));
        } else {
            builder.add(entryBuilder.startEnumSelector(Component.translatable("config.touhou_little_maid.global_ai.stt_type"), STTApiType.class, AIConfig.STT_TYPE.get())
                    .setDefaultValue(STTApiType.PLAYER2).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_type.tooltip"))
                    .setSaveConsumer(AIConfig.STT_TYPE::set).build());
        }

        if (taken) {
            readonly(builder, entryBuilder, "config.touhou_little_maid.global_ai.stt_microphone",
                    Component.literal(AIConfig.STT_MICROPHONE.get()));
        } else {
            builder.add(entryBuilder.startSelector(Component.translatable("config.touhou_little_maid.global_ai.stt_microphone"),
                            MicrophoneManager.getAllMicrophoneName(), AIConfig.STT_MICROPHONE.get())
                    .setDefaultValue(StringUtils.EMPTY).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.stt_microphone.tooltip"))
                    .setSaveConsumer(AIConfig.STT_MICROPHONE::set).build());
        }

        // 这一项管的是「离得多近才算在跟她说话」，没绑角色的女仆照旧用它，不跟着一起置灰。
        builder.add(entryBuilder.startIntSlider(Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance"),
                        AIConfig.MAID_CAN_CHAT_DISTANCE.get(), 1, 256).setDefaultValue(12)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.maid_can_chat_distance.tooltip"))
                .setSaveConsumer(AIConfig.MAID_CAN_CHAT_DISTANCE::set).build());

        field(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.stt_proxy_address",
                StringUtils.EMPTY, AIConfig.STT_PROXY_ADDRESS.get(), AIConfig.STT_PROXY_ADDRESS::set);

        aiChat.addEntry(builder.build());
    }

    private static void ttsConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.tts"));
        builder.setExpanded(true);

        // 宿主宣告能合成语音：绑了角色的女仆改由 WAB 按角色音色念，游戏里这套 TTS 对她们不再走。
        boolean taken = WabClientState.ttsByWab();
        note(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.tts.wab_taken");

        toggle(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.tts_enable",
                true, AIConfig.TTS_ENABLED.get(), AIConfig.TTS_ENABLED::set);

        field(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.tts_proxy_address",
                StringUtils.EMPTY, AIConfig.TTS_PROXY_ADDRESS.get(), AIConfig.TTS_PROXY_ADDRESS::set);

        SortedMap<String, LanguageInfo> languages = Minecraft.getInstance().getLanguageManager().getLanguages();
        if (taken) {
            readonly(builder, entryBuilder, "config.touhou_little_maid.global_ai.tts_language",
                    Component.literal(AIConfig.TTS_LANGUAGE.get()));
        } else {
            builder.add(entryBuilder.startStringDropdownMenu(Component.translatable("config.touhou_little_maid.global_ai.tts_language"),
                            AIConfig.TTS_LANGUAGE.get(), Component::literal, cell(languages)).setSelections(languages.keySet())
                    .setDefaultValue(LanguageManager.DEFAULT_LANGUAGE_CODE).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.tts_language.tooltip"))
                    .setSaveConsumer(info -> AIConfig.TTS_LANGUAGE.set(info)).build());
        }

        aiChat.addEntry(builder.build());
    }

    private static void llmConfig(ConfigEntryBuilder entryBuilder, ConfigCategory aiChat) {
        SubCategoryBuilder builder = entryBuilder.startSubCategory(Component.translatable("config.touhou_little_maid.global_ai.llm"));
        builder.setExpanded(true);

        // 桥挂着：绑了角色的女仆那句话交给 WAB 作答，模组自己的站点、压缩阈值、额度上限对她们都不再被读到。
        // 按「连桥时标注并置灰、断桥照旧」处理——留着能看见的值，但这一栏暂时点不动。
        boolean taken = WabClientState.connected();
        note(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.llm.wab_taken");

        toggle(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.llm_enable",
                true, AIConfig.LLM_ENABLED.get(), AIConfig.LLM_ENABLED::set);

        // 按名字自动生成女仆设定，跟答话是两回事，WAB 接管了也照旧归这里管。
        builder.add(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid.global_ai.auto_gen_setting_enabled"), AIConfig.AUTO_GEN_SETTING_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid.global_ai.auto_gen_setting_enabled.tooltip"))
                .setSaveConsumer(AIConfig.AUTO_GEN_SETTING_ENABLED::set).build());

        field(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.llm_proxy_address",
                StringUtils.EMPTY, AIConfig.LLM_PROXY_ADDRESS.get(), AIConfig.LLM_PROXY_ADDRESS::set);

        number(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.maid_history_compress_token_limit",
                48, AIConfig.MAID_HISTORY_COMPRESS_TOKEN_LIMIT.get(), AIConfig.MAID_HISTORY_COMPRESS_TOKEN_LIMIT::set);

        number(builder, entryBuilder, taken, "config.touhou_little_maid.global_ai.max_tokens_per_player",
                Integer.MAX_VALUE, AIConfig.MAX_TOKENS_PER_PLAYER.get(), AIConfig.MAX_TOKENS_PER_PLAYER::set);

        aiChat.addEntry(builder.build());
    }

    /** 归 WAB 的那几项先给一行说明，免得玩家只看到一栏灰字猜是坏了。 */
    private static void note(SubCategoryBuilder builder, ConfigEntryBuilder entryBuilder, boolean taken, String key) {
        if (!taken) return;
        builder.add(entryBuilder.startTextDescription(Component.translatable(key).withStyle(ChatFormatting.GOLD)).build());
    }

    private static void toggle(SubCategoryBuilder builder, ConfigEntryBuilder entryBuilder, boolean taken,
                               String key, boolean defaultValue, boolean current, Consumer<Boolean> save) {
        if (taken) {
            readonly(builder, entryBuilder, key, Component.translatable(current
                    ? "config.touhou_little_maid.wab.value_on" : "config.touhou_little_maid.wab.value_off"));
            return;
        }
        builder.add(entryBuilder.startBooleanToggle(Component.translatable(key), current)
                .setDefaultValue(defaultValue).setTooltip(Component.translatable(key + ".tooltip"))
                .setSaveConsumer(save).build());
    }

    private static void field(SubCategoryBuilder builder, ConfigEntryBuilder entryBuilder, boolean taken,
                              String key, String defaultValue, String current, Consumer<String> save) {
        if (taken) {
            readonly(builder, entryBuilder, key, Component.literal(current.isEmpty() ? "—" : current));
            return;
        }
        builder.add(entryBuilder.startStrField(Component.translatable(key), current)
                .setDefaultValue(defaultValue).setTooltip(Component.translatable(key + ".tooltip"))
                .setSaveConsumer(save).build());
    }

    private static void number(SubCategoryBuilder builder, ConfigEntryBuilder entryBuilder, boolean taken,
                               String key, int defaultValue, int current, Consumer<Integer> save) {
        if (taken) {
            readonly(builder, entryBuilder, key, Component.literal(String.valueOf(current)));
            return;
        }
        builder.add(entryBuilder.startIntField(Component.translatable(key), current)
                .setDefaultValue(defaultValue).setTooltip(Component.translatable(key + ".tooltip"))
                .setSaveConsumer(save).build());
    }

    /**
     * 只读的一行充当「置灰」：Cloth 的 setEditable(false) 只把字画灰，点击照样写进配置，
     * 看着像锁死其实还能改，比不灰更骗人，所以这里换成不能输入的一行，值仍然看得见。
     */
    private static void readonly(SubCategoryBuilder builder, ConfigEntryBuilder entryBuilder, String key, MutableComponent value) {
        Component line = Component.translatable(key).withStyle(ChatFormatting.GRAY)
                .append(Component.translatable("config.touhou_little_maid.wab.value_separator"))
                .append(value.withStyle(ChatFormatting.GRAY));
        builder.add(entryBuilder.startTextDescription(line)
                .setTooltip(Component.translatable("config.touhou_little_maid.global_ai.wab_taken.tooltip")).build());
    }

    private static DropdownBoxEntry.SelectionCellCreator<String> cell(SortedMap<String, LanguageInfo> languages) {
        LanguageInfo defaultLanguage = languages.get(LanguageManager.DEFAULT_LANGUAGE_CODE);
        return new DropdownBoxEntry.DefaultSelectionCellCreator<>(i -> languages.getOrDefault(i, defaultLanguage).toComponent());
    }
}

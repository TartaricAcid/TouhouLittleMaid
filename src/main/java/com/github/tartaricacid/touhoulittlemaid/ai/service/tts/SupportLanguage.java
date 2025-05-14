package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public final class SupportLanguage {
    public static final List<String> SUPPORTED_LANGUAGES = List.of(
            "en_us", // 英语（美国）
            "zh_cn", // 中文（中国）
            "ja_jp", // 日语（日本）
            "ru_ru", // 俄语（俄罗斯）
            "vi_vn", // 越南语（越南）
            "ko_kr", // 韩语（韩国）
            "es_es", // 西班牙语（西班牙）
            "pt_br", // 葡萄牙语（巴西）
            "fr_fr", // 法语（法国）
            "de_de", // 德语（德国）
            "tr_tr"  // 土耳其语（土耳其）
    );

    public static String findNext(String language) {
        int index = SUPPORTED_LANGUAGES.indexOf(language);
        if (index == -1) {
            return SUPPORTED_LANGUAGES.get(0);
        }
        index++;
        return SUPPORTED_LANGUAGES.get(index % SUPPORTED_LANGUAGES.size());
    }

    public static String findPrev(String language) {
        int index = SUPPORTED_LANGUAGES.indexOf(language);
        if (index == -1) {
            return SUPPORTED_LANGUAGES.get(0);
        }
        index--;
        if (index < 0) {
            index = SUPPORTED_LANGUAGES.size() - 1;
        }
        return SUPPORTED_LANGUAGES.get(index);
    }

    @OnlyIn(Dist.CLIENT)
    public static Component getLanguageName(String language) {
        if (StringUtils.isBlank(language)) {
            return Component.literal("English (US)");
        }
        LanguageManager languageManager = Minecraft.getInstance().getLanguageManager();
        LanguageInfo info = languageManager.getLanguage(language);
        if (info != null) {
            return info.toComponent();
        } else {
            return Component.literal("English (US)");
        }
    }
}

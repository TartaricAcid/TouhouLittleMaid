package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.SkillLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.util.Locale;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant.*;

/**
 * 因为现在的大语言模型基本都有多语言支持，故直接用英文写设定文件
 * <p>
 * 此类仅负责部分固定的提示词中的关键词替换与语言格式化
 * <p>
 * 女仆实时上下文的注册与取值由 context skill 系统负责
 */
public class PapiReplacer {
    private PapiReplacer() {
    }

    /**
     * 基础设定提示词的关键字替换
     */
    public static String replaceSetting(String input, EntityMaid maid, String language) {
        Map<String, String> valueMap = Util.make(Maps.newHashMap(), map -> {
            map.put("main_setting", input);
            map.put("owner_name", getOwnerName(maid));
            map.put("chat_language", getChatLanguage(language));
            map.put("tts_language", getTtsLanguage(maid));
            map.put("available_skills", SkillLoader.getSkillSummary());
        });

        String base = new StrSubstitutor(valueMap).replace(FULL_SETTING);
        if (language.equals(maid.getAiChatManager().getTTSLanguage())) {
            base += new StrSubstitutor(valueMap).replace(OUTPUT_FORMAT_REQUIREMENTS_SAME_LANGUAGES);
        } else {
            base += new StrSubstitutor(valueMap).replace(OUTPUT_FORMAT_REQUIREMENTS_DIFFERENT_LANGUAGES);
        }

        return base;
    }

    public static String getChatLanguage(String languageTag) {
        return language(languageTag);
    }

    public static String getTtsLanguage(EntityMaid maid) {
        return language(maid.getAiChatManager().getTTSLanguage());
    }

    public static String getOwnerName(EntityMaid maid) {
        String ownerName = maid.getAiChatManager().ownerName;
        if (StringUtils.isBlank(ownerName)) {
            return DEFAULT_OWNER_NAME;
        }
        return ownerName;
    }

    /**
     * 不能调用 LanguageManager，那个是客户端方法
     */
    private static String language(String languageTag) {
        String[] parts = languageTag.split("_");
        if (parts.length == 2) {
            languageTag = parts[0] + "-" + parts[1].toUpperCase(Locale.ENGLISH);
        }
        Locale locale = Locale.forLanguageTag(languageTag);
        return LANGUAGE_FORMAT.formatted(locale.getDisplayLanguage(), locale.getDisplayCountry());
    }

}

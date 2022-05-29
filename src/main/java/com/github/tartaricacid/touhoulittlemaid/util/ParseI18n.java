package com.github.tartaricacid.touhoulittlemaid.util;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public final class ParseI18n {
    private static final String I18N_START_CHAR = "{";
    private static final String I18N_END_CHAR = "}";

    private ParseI18n() {
    }

    /**
     * 获取本地化的 key
     */
    public static String getI18nKey(String strIn) {
        if (strIn.startsWith(I18N_START_CHAR) && strIn.endsWith(I18N_END_CHAR)) {
            return strIn.substring(1, strIn.length() - 1);
        } else {
            return strIn;
        }
    }

    /**
     * 将传入的字符串进行国际化
     */
    public static MutableComponent parse(String strIn) {
        // 如果是“{ ”开头，“} ”结尾
        if (strIn.startsWith(I18N_START_CHAR) && strIn.endsWith(I18N_END_CHAR)) {
            // 将剔除大括号后的字符进行国际化
            return new TranslatableComponent(strIn.substring(1, strIn.length() - 1));
        } else {
            // 否则装填原字符串（这算硬编码么？）
            return new TextComponent(strIn);
        }
    }

    /**
     * 将传入的字符串列表进行国际化
     */
    public static List<Component> parse(List<String> strIn) {
        List<Component> strOut = Lists.newArrayList();
        for (String str : strIn) {
            strOut.add(parse(str));
        }
        return strOut;
    }

    /**
     * 将 key 列表转换成对应的翻译文本
     */
    public static List<Component> keysToTrans(List<String> keys, ChatFormatting... formatting) {
        List<Component> out = Lists.newArrayList();
        for (String k : keys) {
            out.add(new TranslatableComponent(k).withStyle(formatting));
        }
        return out;
    }
}

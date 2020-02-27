package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/18 18:13
 **/
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
    public static String parse(String strIn) {
        // 如果是“{ ”开头，“} ”结尾
        if (strIn.startsWith(I18N_START_CHAR) && strIn.endsWith(I18N_END_CHAR)) {
            // 将剔除大括号后的字符进行国际化
            return TouhouLittleMaid.PROXY.translate(strIn.substring(1, strIn.length() - 1));
        } else {
            // 否则装填原字符串（这算硬编码么？）
            return strIn;
        }
    }

    /**
     * 将传入的字符串列表进行国际化
     */
    public static List<String> parse(List<String> strIn) {
        List<String> strOut = new ArrayList<>();
        for (String str : strIn) {
            strOut.add(parse(str));
        }
        return strOut;
    }
}

package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;

/**
 * @author TartaricAcid
 * @date 2019/7/18 18:13
 **/
public final class ParseI18n {
    /**
     * 将传入的字符串进行国际化
     */
    public static String parse(String strIn) {
        // 如果是“{ ”开头，“} ”结尾
        if (strIn.startsWith("{") && strIn.endsWith("}")) {
            // 将剔除大括号后的字符进行国际化
            return TouhouLittleMaid.proxy.translate(strIn.substring(1, strIn.length() - 1));
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

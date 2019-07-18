package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/18 18:13
 **/
public final class ParseI18n {
    /**
     * 将传入的字符串进行国际化
     */
    @SideOnly(Side.CLIENT)
    public static String parse(String strIn) {
        // 如果是“{ ”开头，“} ”结尾
        if (strIn.startsWith("{") && strIn.endsWith("}")) {
            // 将剔除大括号后的字符进行国际化
            return net.minecraft.client.resources.I18n.format(strIn.substring(1, strIn.length() - 1));
        } else {
            // 否则装填原字符串（这算硬编码么？）
            return strIn;
        }
    }

    /**
     * 将传入的字符串列表进行国际化
     */
    @SideOnly(Side.CLIENT)
    public static List<String> parse(List<String> strIn) {
        List<String> strOut = new ArrayList<>();
        for (String str : strIn) {
            strOut.add(parse(str));
        }
        return strOut;
    }

    /**
     * 将传入的字符串进行国际化<br>
     * 服务器用，是的，实体名称在服务端调用了，我能怎么办，我也很绝望啊
     */
    @Deprecated
    public static String parseServer(String strIn) {
        // 如果是“{ ”开头，“} ”结尾
        if (strIn.startsWith("{") && strIn.endsWith("}")) {
            // 将剔除大括号后的字符进行国际化
            return net.minecraft.util.text.translation.I18n.translateToLocal(strIn.substring(1, strIn.length() - 1));
        } else {
            // 否则装填原字符串（这算硬编码么？）
            return strIn;
        }
    }

    /**
     * 将传入的字符串列表进行国际化<br>
     * 服务器用，是的，实体名称在服务端调用了，我能怎么办，我也很绝望啊
     */
    @Deprecated
    public static List<String> parseServer(List<String> strIn) {
        List<String> strOut = new ArrayList<>();
        for (String str : strIn) {
            strOut.add(parseServer(str));
        }
        return strOut;
    }
}

package com.github.tartaricacid.touhoulittlemaid.compat.wab;

/**
 * 悬停提示的收敛规则：一行读完，细节挪到「详细说明」。
 *
 * <p>WAB 的配置说明原本是整段解释，鼠标一停就盖住半屏。现在统一走这里：
 * 悬停只留第一句（宽度按汉字算，最多 {@link #MAX_WIDTH} 个半角位 ≈ 40 个汉字），
 * 放不下的原文进设置页底部那一栏 {@code config.touhou_little_maid.wab.help}，
 * 入口是设置项名后面的 {@code (?)}。
 *
 * <p>只做文本裁剪，不编造内容：原文第一句不够用时才会被截断并加省略号，
 * 也就是说 {@link #brief} 的输出永远是原文的前缀。
 */
public final class WabTips {
    /** 一行能读完的宽度（半角单位）。中文按 2 计，所以 80 ≈ 40 个汉字。 */
    private static final int MAX_WIDTH = 80;
    private static final String ELLIPSIS = "…";

    private WabTips() {
    }

    /** 取第一句并按宽度夹好；空串原样返回，其余永远是原文前缀（可能带省略号）。 */
    public static String brief(String raw) {
        if (raw == null) return "";
        String text = raw.replaceAll("\\s+", " ").trim();
        if (text.isEmpty()) return "";
        int end = sentenceEnd(text);
        if (end > 0) text = text.substring(0, end).trim();
        return clamp(text);
    }

    /** 原文被砍过就返回 true——设置项名后面要加 {@code (?)}，并把它收进详细说明里。 */
    public static boolean truncated(String raw) {
        if (raw == null) return false;
        String text = raw.replaceAll("\\s+", " ").trim();
        return !brief(raw).equals(text);
    }

    /** 第一处句末标点（中文句号/叹号/问号/分号，或拉丁文的「. 」），没有就返回 -1。 */
    private static int sentenceEnd(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '。' || c == '！' || c == '？' || c == '；' || c == '\n') return i;
            if ((c == '.' || c == '!' || c == '?') && i + 1 < text.length() && text.charAt(i + 1) == ' ') {
                if (c == '.' && i > 0 && Character.isDigit(text.charAt(i - 1))
                        && Character.isDigit(text.charAt(i + 1))) {
                    continue;
                }
                return i;
            }
        }
        return -1;
    }

    private static String clamp(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            int next = width + visualWidth(text.charAt(i));
            if (next > MAX_WIDTH) {
                return text.substring(0, i).stripTrailing() + ELLIPSIS;
            }
            width = next;
        }
        return text;
    }

    private static int visualWidth(char c) {
        return c > 0x2E7F ? 2 : 1;
    }
}

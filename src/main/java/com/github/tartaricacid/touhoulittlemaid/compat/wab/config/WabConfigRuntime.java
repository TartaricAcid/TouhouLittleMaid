package com.github.tartaricacid.touhoulittlemaid.compat.wab.config;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabRemoteConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 126 个配置键的运行时取值口。业务代码只准从这里读，不准自己写死兜底值。
 *
 * <p>两条铁律：
 * <ul>
 *   <li>宿主把生效值放在 {@code fields} 帧的 {@code value} 里（保守覆盖已经在宿主侧做完）；
 *       模组这里只做读取与类型解析，不二次覆盖，避免两端各改一次产生漂移。</li>
 *   <li>断桥时 {@link WabRemoteConfig#present()} 为 false，一律回落到
 *       {@link WabConfigDefaults} 里的保守默认值——这正是「不连 WAB 时行为与原版一致」。</li>
 * </ul>
 */
public final class WabConfigRuntime {
    private WabConfigRuntime() {
    }

    /** 每次读取都取当前字段表的快照，避免设置页改完还拿着上一张表。 */
    private static Map<String, String> values() {
        if (!WabRemoteConfig.present()) return Map.of();
        Map<String, String> out = new HashMap<>();
        for (WabRemoteConfig.Field field : WabRemoteConfig.fields()) {
            out.put(field.key(), field.value());
        }
        return out;
    }

    public static boolean connected() {
        return WabRemoteConfig.present();
    }

    private static Object fallback(String key) {
        return WabConfigDefaults.of(key);
    }

    /** 开关。缺键 / 断桥 / 解析失败一律回落到目录里的默认值。 */
    public static boolean on(String key) {
        Object def = fallback(key);
        boolean defaultValue = def instanceof Boolean b ? b : false;
        String raw = values().get(key);
        if (raw == null || raw.isBlank()) return defaultValue;
        if ("true".equalsIgnoreCase(raw.trim())) return true;
        if ("false".equalsIgnoreCase(raw.trim())) return false;
        return defaultValue;
    }

    /** 数值（概率、间隔、次数、档位）。越界不夹取——夹取规则由 WabRules 显式处理，不在取值口偷偷改。 */
    public static double number(String key) {
        Object def = fallback(key);
        double defaultValue = 0;
        if (def instanceof Number n) defaultValue = n.doubleValue();
        String raw = values().get(key);
        if (raw == null || raw.isBlank()) return defaultValue;
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int integer(String key) {
        return (int) Math.rint(number(key));
    }

    /** 下拉项 / 文本。空串是合法值（例如口头禅留空、过滤表达式留空）。 */
    public static String text(String key) {
        Object def = fallback(key);
        String defaultValue = def instanceof String s ? s : "";
        String raw = values().get(key);
        return raw == null ? defaultValue : raw;
    }
}

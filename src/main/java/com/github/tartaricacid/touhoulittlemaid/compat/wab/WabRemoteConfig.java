package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * WAB 插件那一侧设置的只读副本。
 * 宿主把 plugin.json 声明的字段表（键名、标签、类型、当前值、说明、能不能改）原样描一份，
 * 随 welcome / settings 帧发下来；游戏里的设置页据此生成开关，改完再发回去。
 * 插件以后加一项、改个默认值，游戏里跟着长出来，不用再动模组。
 */
public final class WabRemoteConfig {
    /** 一行 = plugin.json 里的一个设置字段。type 沿用插件的写法：switch / number / text / select / secret。 */
    public record Field(String key, String label, String type, String value, String defaultValue,
                        String help, List<String> options, Map<String, String> optionLabels, boolean locked) {
        public boolean on() {
            return "true".equalsIgnoreCase(value);
        }

        public int number(int fallback) {
            try {
                return Integer.parseInt(value.trim());
            } catch (Exception e) {
                return fallback;
            }
        }

        /**
         * 选项的显示名：宿主描来的 option_labels 映射里有的用中文，没有的退回原键。
         * 落盘与判断永远用原键（interaction / galgame / main…），这里只管给人看。
         */
        public String labelOf(String option) {
            String name = optionLabels.get(option);
            return name == null || name.isBlank() ? option : name;
        }
    }

    private static volatile List<Field> fields = Collections.emptyList();
    private static volatile long updatedAt;

    private WabRemoteConfig() {
    }

    public static List<Field> fields() {
        return fields;
    }

    /** 收到过字段表才算「连着能改」；桥没连、或对面是不发这一帧的旧版 WAB，设置页只给一行说明。 */
    public static boolean present() {
        return !fields.isEmpty();
    }

    public static long updatedAt() {
        return updatedAt;
    }

    static void apply(JsonArray arr) {
        if (arr == null) return;
        List<Field> next = new ArrayList<>();
        for (JsonElement element : arr) {
            if (!element.isJsonObject()) continue;
            JsonObject row = element.getAsJsonObject();
            String key = WabJson.str(row, "key");
            if (key.isEmpty()) continue;
            List<String> options = new ArrayList<>();
            for (JsonElement option : WabJson.arr(row, "options")) {
                if (option.isJsonNull()) continue;
                options.add(option.getAsString());
            }
            Map<String, String> labels = new LinkedHashMap<>();
            if (row.get("option_labels") instanceof JsonObject ol) {
                for (Map.Entry<String, JsonElement> e : ol.entrySet()) {
                    if (e.getValue().isJsonPrimitive()) labels.put(e.getKey(), e.getValue().getAsString());
                }
            }
            next.add(new Field(key, WabJson.str(row, "label", key), WabJson.str(row, "type", "text"),
                    WabJson.str(row, "value"), WabJson.str(row, "default"), WabJson.str(row, "help"),
                    options, Map.copyOf(labels), WabJson.bool(row, "locked", false)));
        }
        fields = List.copyOf(next);
        updatedAt = System.currentTimeMillis();
        TouhouLittleMaid.LOGGER.info("[WAB] 收到插件字段表: {} 项", next.size());
    }

    /**
     * 桥断了就作废：留着上一次那张表，设置页会照常画出能点的开关，可点了什么也发不出去，
     * 玩家只会以为是自己手滑。清空之后那一栏退回「还没连上」的说明。
     */
    public static void clear() {
        fields = Collections.emptyList();
        updatedAt = 0;
    }

    /**
     * 把改动发给 WAB 落盘。这里必须绕回服务器线程：设置页跑在客户端，
     * 而桥的 socket 只在服务器那一侧，客户端直接写会撞上并发。
     * 每一步都写日志：以前静默 return，游戏里改了设置没生效，谁也说不清断在哪一环。
     */
    public static void save(String key, String value) {
        if (key == null || key.isEmpty()) return;
        String text = value == null ? "" : value;
        WabBridge bridge = WabBridge.get();
        if (bridge == null) {
            TouhouLittleMaid.LOGGER.warn("[WAB] 设置改动 {} 未发送：桥未就绪（存档没在跑）", key);
            return;
        }
        if (bridge.session() == null || bridge.session().isClosed()) {
            TouhouLittleMaid.LOGGER.warn("[WAB] 设置改动 {} 未发送：桥未连接", key);
            return;
        }
        TouhouLittleMaid.LOGGER.info("[WAB] 设置改动发送: {}={}", key, text);
        updateLocalValue(key, text);
        bridge.server().execute(() -> bridge.sendConfigSet(key, text));
    }

    /** 乐观更新本地字段表：界面立刻显示刚改的值；宿主的权威回推随后会覆盖它。 */
    private static void updateLocalValue(String key, String value) {
        List<Field> current = fields;
        if (current.isEmpty()) return;
        List<Field> next = new ArrayList<>();
        boolean hit = false;
        for (Field f : current) {
            if (f.key().equals(key)) {
                hit = true;
                next.add(new Field(f.key(), f.label(), f.type(), value, f.defaultValue(),
                        f.help(), f.options(), f.optionLabels(), f.locked()));
            } else {
                next.add(f);
            }
        }
        if (hit) fields = List.copyOf(next);
    }
}

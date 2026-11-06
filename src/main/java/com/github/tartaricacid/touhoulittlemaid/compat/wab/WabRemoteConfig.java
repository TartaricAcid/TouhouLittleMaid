package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WAB 插件那一侧设置的只读副本。
 * 宿主把 plugin.json 声明的字段表（键名、标签、类型、当前值、说明、能不能改）原样描一份，
 * 随 welcome / settings 帧发下来；游戏里的设置页据此生成开关，改完再发回去。
 * 插件以后加一项、改个默认值，游戏里跟着长出来，不用再动模组。
 */
public final class WabRemoteConfig {
    /** 一行 = plugin.json 里的一个设置字段。type 沿用插件的写法：switch / number / text / select / secret。 */
    public record Field(String key, String label, String type, String value, String defaultValue,
                        String help, List<String> options, boolean locked) {
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
            next.add(new Field(key, WabJson.str(row, "label", key), WabJson.str(row, "type", "text"),
                    WabJson.str(row, "value"), WabJson.str(row, "default"), WabJson.str(row, "help"),
                    options, WabJson.bool(row, "locked", false)));
        }
        fields = List.copyOf(next);
        updatedAt = System.currentTimeMillis();
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
     */
    public static void save(String key, String value) {
        WabBridge bridge = WabBridge.get();
        if (bridge == null || key == null || key.isEmpty()) return;
        String text = value == null ? "" : value;
        bridge.server().execute(() -> bridge.sendConfigSet(key, text));
    }
}

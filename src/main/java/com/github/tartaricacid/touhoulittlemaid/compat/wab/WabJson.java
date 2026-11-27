package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * 协议字段可能来自不同版本的 WAB，类型不符时一律退回默认值，避免一帧坏数据打断整个会话。
 */
public final class WabJson {
    private WabJson() {
    }

    public static JsonObject obj(JsonObject src, String key) {
        if (src == null) {
            return new JsonObject();
        }
        JsonElement element = src.get(key);
        return element != null && element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
    }

    public static JsonArray arr(JsonObject src, String key) {
        if (src == null) {
            return new JsonArray();
        }
        JsonElement element = src.get(key);
        return element != null && element.isJsonArray() ? element.getAsJsonArray() : new JsonArray();
    }

    public static String str(JsonObject src, String key) {
        return str(src, key, "");
    }

    public static String str(JsonObject src, String key, String fallback) {
        if (src == null) {
            return fallback;
        }
        JsonElement element = src.get(key);
        if (element == null || element.isJsonNull()) {
            return fallback;
        }
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return primitive.getAsString();
            }
            if (primitive.isBoolean()) {
                return primitive.getAsBoolean() ? "true" : "false";
            }
            return primitive.getAsString();
        }
        return fallback;
    }

    public static int integer(JsonObject src, String key, int fallback) {
        double value = number(src, key, fallback);
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return fallback;
        }
        return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, value));
    }

    public static double number(JsonObject src, String key, double fallback) {
        if (src == null) {
            return fallback;
        }
        JsonElement element = src.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            return fallback;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        try {
            return primitive.isNumber() ? primitive.getAsDouble() : Double.parseDouble(primitive.getAsString());
        } catch (Exception e) {
            return fallback;
        }
    }

    public static boolean bool(JsonObject src, String key, boolean fallback) {
        if (src == null) {
            return fallback;
        }
        JsonElement element = src.get(key);
        if (element == null || !element.isJsonPrimitive()) {
            return fallback;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isBoolean()) {
            return primitive.getAsBoolean();
        }
        if (primitive.isNumber()) {
            return primitive.getAsInt() != 0;
        }
        String text = primitive.getAsString().trim().toLowerCase();
        if ("true".equals(text) || "1".equals(text) || "on".equals(text) || "yes".equals(text)) {
            return true;
        }
        if ("false".equals(text) || "0".equals(text) || "off".equals(text) || "no".equals(text)) {
            return false;
        }
        return fallback;
    }

    public static String round(double value, int digits) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "0";
        }
        return String.format(java.util.Locale.ROOT, "%." + digits + "f", value);
    }
}

package com.github.tartaricacid.touhoulittlemaid.util.http;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class UrlTool {
    /**
     * 将键值对拼接成合法的 URL 查询字符串
     *
     * @param params 键值对集合
     * @return 拼接好的 URL 查询字符串
     */
    public static String buildQueryString(String baseUrl, @Nullable Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        StringBuilder query = new StringBuilder();
        query.append(baseUrl);
        if (!baseUrl.contains("?")) {
            query.append("?");
        } else if (!baseUrl.endsWith("&") && !baseUrl.endsWith("?")) {
            query.append("&");
        }
        String paramsText = Joiner.on("&").withKeyValueSeparator("=")
                .join(params.entrySet().stream().collect(escape()));
        query.append(paramsText);
        return query.toString();
    }

    public static String buildQueryString(String baseUrl, Consumer<LinkedHashMap<String, String>> consumer) {
        return buildQueryString(baseUrl, Util.make(Maps.newLinkedHashMap(), consumer));
    }

    @NotNull
    private static Collector<Map.Entry<String, String>, ?, Map<String, String>> escape() {
        Escaper escaper = UrlEscapers.urlFormParameterEscaper();
        return Collectors.toMap(e -> escaper.escape(e.getKey()),
                e -> escaper.escape(e.getValue()));
    }
}

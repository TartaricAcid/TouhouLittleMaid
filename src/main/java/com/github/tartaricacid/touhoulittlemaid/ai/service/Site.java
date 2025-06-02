package com.github.tartaricacid.touhoulittlemaid.ai.service;

import com.google.common.annotations.Beta;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public interface Site {
    /**
     * 与序列化反序列化相关的字段
     */
    String ID = "id";
    String API_TYPE = "api_type";
    String ENABLED = "enabled";
    String ICON = "icon";
    String URL = "url";
    String SECRET_KEY = "secret_key";
    String HEADERS = "headers";
    String MODELS = "models";

    /**
     * 用于控制 JSON 序列化的字段顺序
     */
    ToIntFunction<String> FIXED_ORDER_FIELDS = Util.make(new Object2IntOpenHashMap<>(), map -> {
        map.put(ID, 0);
        map.put(API_TYPE, 1);
        map.put(ENABLED, 2);
        map.put(ICON, 3);
        map.put(URL, 4);
        map.put(SECRET_KEY, 5);
        map.put(HEADERS, 6);
        map.defaultReturnValue(100);
        map.put(MODELS, Integer.MAX_VALUE);
    });

    /**
     * 用于控制 JSON 序列化的字段顺序
     */
    Comparator<String> KEY_COMPARATOR = Comparator.comparingInt(FIXED_ORDER_FIELDS).thenComparing(Function.identity());

    /**
     * 该站点的 ID，唯一标识一个站点
     * 该 ID 不建议包含空格或者其他非英文字符
     */
    String id();

    /**
     * 该站点是否启用
     * 启用后才会在选择界面显示
     */
    boolean enabled();

    /**
     * 该站点的图标
     * 用于游戏内配置站点功能的显示
     */
    ResourceLocation icon();

    /**
     * 该站点的 URL
     * 用于请求数据
     */
    String url();

    /**
     * HTTP 头部信息，特殊头部信息需要在这里添加
     */
    Map<String, String> headers();

    /**
     * 服务类型，指的是 LLM STT 还是 TTS
     */
    ServiceType getServiceType();

    /**
     * API 类型，指的是该站点的 API 类型
     * 不同 API 类型的站点拥有不同的解析和通信方式
     */
    String getApiType();

    /**
     * 该站点的客户端
     * 用于请求数据
     */
    Client client();

    /**
     * 该站点的序列化器，用于读取和写入 JSON 配置数据或者网络通信
     */
    default SerializableSite<? extends Site> serializer() {
        return SerializerRegister.getSerializer(getServiceType(), getApiType());
    }

    /**
     * 站点名称，不一定会被使用
     */
    @Beta
    default Component getName() {
        return Component.translatable("ai.touhou_little_maid.chat.site.%s.name".formatted(id()));
    }

    /**
     * 站点描述，不一定会被使用
     */
    @Beta
    default Component getDesc() {
        return Component.translatable("ai.touhou_little_maid.chat.site.%s.desc".formatted(id()));
    }
}

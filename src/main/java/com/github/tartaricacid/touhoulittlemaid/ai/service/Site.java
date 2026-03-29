package com.github.tartaricacid.touhoulittlemaid.ai.service;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
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
    String SECRET_ID = "secret_id";
    String SECRET_KEY = "secret_key";
    String HEADERS = "headers";
    String MODELS = "models";
    String HAS_THINKING_FIELD = "has_thinking_field";
    // 仅用于 MiniMax TTS，表示语音合成的模型，和 MODELS 里列出的模型不同，MODELS 里列出的是 voice_id，而 siteModel 是一个独立的参数
    String SITE_MODEL = "site_model";
    // 腾讯云 asr 语言类型
    String ENG_SER_VICE_TYPE = "eng_ser_vice_type";
    // 腾讯云 asr 热词
    String HOT_WORD = "hot_word";

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
     * 设置站点是否启用
     */
    void setEnabled(boolean enabled);

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
     * 站点名称语言文件 key
     */
    default String getNameKey() {
        return "ai.touhou_little_maid.chat.site.%s.name".formatted(id());
    }
}

package com.github.tartaricacid.touhoulittlemaid.ai.service;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public interface Site {
    String ID = "id";
    String API_TYPE = "api_type";
    String ENABLED = "enabled";
    String ICON = "icon";
    String URL = "url";
    String SECRET_KEY = "secret_key";
    String HEADERS = "headers";
    String MODELS = "models";
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
    Comparator<String> KEY_COMPARATOR = Comparator.comparingInt(FIXED_ORDER_FIELDS).thenComparing(Function.identity());

    String id();

    boolean enabled();

    ResourceLocation icon();

    String url();

    Map<String, String> headers();

    ServiceType getServiceType();

    String getApiType();

    Client client();

    default SerializableSite<? extends Site> serializer() {
        return SerializerRegister.getSerializer(getServiceType(), getApiType());
    }

    default Component getName() {
        return Component.translatable("ai.touhou_little_maid.site.%s.name".formatted(id()));
    }

    default Component getDesc() {
        return Component.translatable("ai.touhou_little_maid.site.%s.desc".formatted(id()));
    }
}

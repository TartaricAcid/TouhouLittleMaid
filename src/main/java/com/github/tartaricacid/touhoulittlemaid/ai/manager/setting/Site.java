package com.github.tartaricacid.touhoulittlemaid.ai.manager.setting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Site {
    private static final String CHAT = "chat";
    private static final String TTS = "tts";
    private static final String TYPE = "type";
    private static final String API_TYPE = "api_type";
    private static final String URL = "url";
    private static final String API_KEY = "api_key";
    private static final String MODELS = "models";
    private static final String EXTRA_ARGS = "extra_args";
    private static final String EXTRA_HEADER = "extra_header";

    private String type;
    private String apiType;
    private String url;
    private String apiKey;
    private List<String> models;
    private Map<String, Object> extraArgs;
    private Map<String, String> extraHeader;

    public Site(String type, String apiType, String url, String apiKey, List<String> models, Map<String, Object> extraArgs, Map<String, String> extraHeader) {
        this.type = type;
        this.apiType = apiType;
        this.url = url;
        this.apiKey = apiKey;
        this.models = models;
        this.extraArgs = extraArgs;
        this.extraHeader = extraHeader;
    }

    @SuppressWarnings("unchecked")
    public Site(LinkedHashMap<String, Object> map) {
        this.type = Objects.requireNonNullElse((String) map.get(TYPE), StringUtils.EMPTY);
        this.apiType = Objects.requireNonNullElse((String) map.get(API_TYPE), StringUtils.EMPTY);
        this.url = Objects.requireNonNullElse((String) map.get(URL), StringUtils.EMPTY);
        this.apiKey = Objects.requireNonNullElse((String) map.get(API_KEY), StringUtils.EMPTY);
        this.models = Objects.requireNonNullElse((List<String>) map.get(MODELS), Lists.newArrayList());
        this.extraArgs = Objects.requireNonNullElse((Map<String, Object>) map.get(EXTRA_ARGS), Maps.newHashMap());
        this.extraHeader = Objects.requireNonNullElse((Map<String, String>) map.get(EXTRA_HEADER), Maps.newHashMap());
    }

    public String getType() {
        return type;
    }

    public String getApiType() {
        return apiType;
    }

    public String getUrl() {
        return url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<String> getModels() {
        return models;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    public boolean isChat() {
        return CHAT.equals(type);
    }

    public boolean isTts() {
        return TTS.equals(type);
    }

    public Map<String, Object> getExtraArgs() {
        return extraArgs;
    }

    public Map<String, String> getExtraHeader() {
        return extraHeader;
    }
}

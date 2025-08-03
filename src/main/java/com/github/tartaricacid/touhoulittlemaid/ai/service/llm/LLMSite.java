package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ConfigProxySelector;
import com.github.tartaricacid.touhoulittlemaid.ai.service.SerializerRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Site;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.io.Reader;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;

public interface LLMSite extends Site {
    HttpClient LLM_HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .proxy(new ConfigProxySelector(AIConfig.LLM_PROXY_ADDRESS))
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    static Map<String, LLMSite> readSites(Path file) {
        Map<String, LLMSite> output = Maps.newHashMap();
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject root = GsonHelper.parse(reader);
            for (String id : root.keySet()) {
                JsonElement value = root.get(id);
                if (!(value instanceof JsonObject jsonObject)) {
                    continue;
                }
                String apiType = GsonHelper.getAsString(jsonObject, API_TYPE);
                var serializer = SerializerRegister.getLLMSerializer(apiType);
                if (serializer == null) {
                    TouhouLittleMaid.LOGGER.error("Unknown LLM site type: {}", apiType);
                    continue;
                }
                serializer.codec().decode(JsonOps.INSTANCE, value)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .ifPresent(site -> output.put(id, site.getFirst()));
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to read sites", e);
        }
        return output;
    }

    static void writeSites(Path file, Map<String, LLMSite> sites) {
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(file))) {
            JsonObject root = new JsonObject();
            for (String id : sites.keySet()) {
                LLMSite site = sites.get(id);
                var serializer = SerializerRegister.getLLMSerializer(site.getApiType());
                JsonElement json = serializer.codec()
                        .encodeStart(JsonOps.INSTANCE, site)
                        .resultOrPartial(TouhouLittleMaid.LOGGER::error)
                        .orElseThrow();
                json.getAsJsonObject().addProperty(API_TYPE, site.getApiType());
                root.add(id, json);
            }
            writer.setSerializeNulls(false);
            writer.setIndent("  ");
            GsonHelper.writeValue(writer, root, KEY_COMPARATOR);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to save sites", e);
        }
    }

    @Override
    LLMClient client();

    @Override
    default ServiceType getServiceType() {
        return ServiceType.LLM;
    }
}

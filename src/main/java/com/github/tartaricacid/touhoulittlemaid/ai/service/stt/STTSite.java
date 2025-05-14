package com.github.tartaricacid.touhoulittlemaid.ai.service.stt;

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

public interface STTSite extends Site {
    HttpClient STT_HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .proxy(new ConfigProxySelector(AIConfig.STT_PROXY_ADDRESS))
            .build();

    static Map<String, STTSite> readSites(Path file) {
        Map<String, STTSite> output = Maps.newHashMap();
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject root = GsonHelper.parse(reader);
            for (String id : root.keySet()) {
                JsonElement value = root.get(id);
                if (!(value instanceof JsonObject jsonObject)) {
                    continue;
                }
                String apiType = GsonHelper.getAsString(jsonObject, API_TYPE);
                var serializer = SerializerRegister.getSTTSerializer(apiType);
                if (serializer == null) {
                    TouhouLittleMaid.LOGGER.error("Unknown STT site type: {}", apiType);
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

    static void writeSites(Path file, Map<String, STTSite> sites) {
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(file))) {
            JsonObject root = new JsonObject();
            for (String id : sites.keySet()) {
                STTSite site = sites.get(id);
                var serializer = SerializerRegister.getSTTSerializer(site.getApiType());
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
    STTClient client();

    @Override
    default ServiceType getServiceType() {
        return ServiceType.STT;
    }
}

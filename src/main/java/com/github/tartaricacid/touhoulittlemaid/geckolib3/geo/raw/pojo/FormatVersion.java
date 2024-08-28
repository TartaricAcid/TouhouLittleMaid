package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import java.io.IOException;
import java.lang.reflect.Type;

@JsonAdapter(FormatVersion.Serializer.class)
public enum FormatVersion {
    /**
     * 旧版本基岩版模型，仅限 1.10.0
     */
    LEGACY("[1.10.0]"),
    /**
     * 新版本基岩版模型，往后的 1.14.0，1.16.0 1.21.0 通通用此版本读取
     */
    NEW("[1.12.0,)");

    private final VersionRange versionRange;

    FormatVersion(String version) {
        this.versionRange = createFromVersionSpec(version);
    }

    public static FormatVersion forValue(String value) throws IOException {
        DefaultArtifactVersion inputVersion = new DefaultArtifactVersion(value);
        if (NEW.versionRange.containsVersion(inputVersion)) {
            return NEW;
        }
        return LEGACY;
    }

    public String toValue() {
        return switch (this) {
            case LEGACY -> "1.10.0";
            case NEW -> "1.12.0";
        };
    }

    private static VersionRange createFromVersionSpec(final String spec) {
        try {
            return VersionRange.createFromVersionSpec(spec);
        } catch (InvalidVersionSpecificationException e) {
            TouhouLittleMaid.LOGGER.fatal("Failed to parse version spec {}", spec, e);
            throw new RuntimeException("Failed to parse spec", e);
        }
    }

    protected static class Serializer implements JsonSerializer<FormatVersion>, JsonDeserializer<FormatVersion> {
        @Override
        public FormatVersion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return FormatVersion.forValue(json.getAsString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public JsonElement serialize(FormatVersion src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toValue());
        }
    }
}

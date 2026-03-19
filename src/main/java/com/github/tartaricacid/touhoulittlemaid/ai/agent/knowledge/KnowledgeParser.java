package com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.Nullable;
import java.io.Reader;
import java.util.Locale;
import java.util.regex.Pattern;

public final class KnowledgeParser {
    private static final Yaml YAML = new Yaml(new Constructor(KnowledgeDefinition.class, new LoaderOptions()));
    private static final Pattern LOCALE_PATTERN = Pattern.compile("^[a-z0-9]+_[a-z0-9]+$");

    private KnowledgeParser() {
    }

    @Nullable
    public static KnowledgeDefinition parse(Reader reader, String sourceName) {
        KnowledgeDefinition definition;
        try {
            definition = YAML.load(reader);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.warn("Failed to parse knowledge yml from {}", sourceName, e);
            return null;
        }
        if (definition == null) {
            TouhouLittleMaid.LOGGER.warn("Skip invalid knowledge yml {} because root is empty", sourceName);
            return null;
        }

        if (StringUtils.isBlank(definition.getId()) || StringUtils.isBlank(definition.getDesc())) {
            TouhouLittleMaid.LOGGER.warn("Skip invalid knowledge yml {} because id or desc is missing or blank", sourceName);
            return null;
        }

        if (StringUtils.isBlank(definition.getBody())) {
            TouhouLittleMaid.LOGGER.warn("Skip invalid knowledge yml {} because body is missing or blank", sourceName);
            return null;
        }

        if (!validateLocalized(definition, sourceName)) {
            return null;
        }

        return definition;
    }

    private static boolean validateLocalized(KnowledgeDefinition definition, String sourceName) {
        if (definition.getLocalized() == null) {
            return true;
        }
        for (var entry : definition.getLocalized().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String id = definition.getId();

            if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                TouhouLittleMaid.LOGGER.warn("Skip invalid knowledge yml {} because localized entry for knowledge {} is malformed", sourceName, id);
                return false;
            }
            String normalizedKey = key.toLowerCase(Locale.ROOT).replace('-', '_');
            if (!LOCALE_PATTERN.matcher(normalizedKey).matches()) {
                TouhouLittleMaid.LOGGER.warn("Skip invalid knowledge yml {} because localized key {} for knowledge {} is invalid", sourceName, key, id);
                return false;
            }
        }
        return true;
    }
}

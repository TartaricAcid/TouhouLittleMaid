package com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class KnowledgeRegister {
    private static final int MAX_DEPTH = 3;
    private static final Path KNOWLEDGE_DIR = FMLPaths.CONFIGDIR.get()
            .resolve(TouhouLittleMaid.MOD_ID)
            .resolve("skill")
            .resolve("knowledge");

    private static Map<String, KnowledgeDefinition> CONFIG_KNOWLEDGE = Maps.newLinkedHashMap();
    private static Map<String, KnowledgeDefinition> DATAPACK_KNOWLEDGE = Maps.newLinkedHashMap();
    private static Map<String, KnowledgeDefinition> ALL_KNOWLEDGE = Maps.newLinkedHashMap();

    private KnowledgeRegister() {
    }

    public static void init() {
        createKnowledgeDir();
        reloadConfigKnowledge();
        reloadMergedKnowledge();
    }

    public static void reloadConfigKnowledge() {
        createKnowledgeDir();

        Map<String, KnowledgeDefinition> loaded = Maps.newLinkedHashMap();
        try (var stream = Files.walk(KNOWLEDGE_DIR, MAX_DEPTH)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".yml"))
                    .sorted()
                    .forEach(path -> loadKnowledgeFile(path, loaded));
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.warn("Failed to scan config knowledge directory {}", KNOWLEDGE_DIR, e);
        }

        CONFIG_KNOWLEDGE = ImmutableMap.copyOf(loaded);

        reloadMergedKnowledge();
    }

    public static void reloadDatapackKnowledge(Map<String, KnowledgeDefinition> datapackKnowledge) {
        DATAPACK_KNOWLEDGE = ImmutableMap.copyOf(datapackKnowledge);
        reloadMergedKnowledge();
    }

    public static Map<String, KnowledgeDefinition> getAllKnowledge() {
        return ALL_KNOWLEDGE;
    }

    @Nullable
    public static KnowledgeDefinition getKnowledge(String knowledgeId) {
        return ALL_KNOWLEDGE.get(knowledgeId);
    }

    private static void reloadMergedKnowledge() {
        Map<String, KnowledgeDefinition> merged = Maps.newLinkedHashMap();

        CONFIG_KNOWLEDGE.values().forEach(knowledge -> {
            KnowledgeDefinition previous = merged.put(knowledge.getId(), knowledge);
            if (previous != null) {
                TouhouLittleMaid.LOGGER.warn(
                        "Knowledge id {} is duplicated, later config entry overrides previous one",
                        knowledge.getId()
                );
            }
        });

        DATAPACK_KNOWLEDGE.values().forEach(knowledge -> {
            KnowledgeDefinition previous = merged.put(knowledge.getId(), knowledge);
            if (previous != null) {
                TouhouLittleMaid.LOGGER.warn(
                        "Knowledge id {} is duplicated, later datapack entry overrides previous one",
                        knowledge.getId()
                );
            }
        });

        ALL_KNOWLEDGE = ImmutableMap.copyOf(merged);
    }

    private static void loadKnowledgeFile(Path path, Map<String, KnowledgeDefinition> target) {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            KnowledgeDefinition definition = KnowledgeParser.parse(reader, path.toString());
            if (definition == null) {
                TouhouLittleMaid.LOGGER.warn("Knowledge file {} is empty or invalid, skipped", path);
                return;
            }
            KnowledgeDefinition previous = target.put(definition.getId(), definition);
            if (previous != null) {
                TouhouLittleMaid.LOGGER.warn(
                        "Knowledge id {} is duplicated inside config knowledge loading, later file {} overrides previous one",
                        definition.getId(), path
                );
            }
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.warn("Failed to read knowledge yml from {}", path, e);
        }
    }

    private static void createKnowledgeDir() {
        try {
            Files.createDirectories(KNOWLEDGE_DIR);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to create knowledge directory {}", KNOWLEDGE_DIR, e);
        }
    }
}

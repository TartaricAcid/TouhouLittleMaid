package com.github.tartaricacid.touhoulittlemaid.datapack.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeDefinition;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeParser;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge.KnowledgeRegister;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class KnowledgeDataReloadListener implements ResourceManagerReloadListener {
    private static final String KNOWLEDGE_PATH = "skill/knowledge";

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Map<String, KnowledgeDefinition> loaded = Maps.newLinkedHashMap();

        resourceManager.listResources(KNOWLEDGE_PATH, loc -> {
            // 必须是 touhou_little_maid/skill/knowledge 下的文件
            if (loc.getNamespace().equals(TouhouLittleMaid.MOD_ID)) {
                return loc.getPath().endsWith(".yml");
            }
            return false;
        }).forEach((location, resource) -> loadKnowledge(location, resource, loaded));

        KnowledgeRegister.reloadDatapackKnowledge(loaded);
    }

    private static void loadKnowledge(ResourceLocation location, Resource resource, Map<String, KnowledgeDefinition> target) {
        try (var stream = resource.open(); var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            KnowledgeDefinition definition = KnowledgeParser.parse(reader, location.toString());
            if (definition == null) {
                TouhouLittleMaid.LOGGER.warn("Knowledge file {} is empty or invalid, skipped", location.toString());
                return;
            }
            KnowledgeDefinition previous = target.put(definition.getId(), definition);
            if (previous != null) {
                TouhouLittleMaid.LOGGER.warn("Knowledge id {} is duplicated inside datapack loading, later resource {} overrides previous one", definition.getId(), location);
            }
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.warn("Failed to load knowledge datapack resource {}", location, e);
        }
    }
}

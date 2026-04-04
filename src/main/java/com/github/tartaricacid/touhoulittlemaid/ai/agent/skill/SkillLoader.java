package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.xml.XmlEscapers;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SkillLoader {
    private static final Path SKILLS_DIR = FMLPaths.CONFIGDIR.get()
            .resolve(TouhouLittleMaid.MOD_ID)
            .resolve("skills");


    private static final int MAX_DEPTH = 3;
    private static final String SKILL_FILE_NAME = "skill.md";
    private static final String REFERENCES = "references";

    /**
     * 来自配置文件目录下的 skill
     */
    private static Map<String, SkillInstance> CONFIG_SKILLS = Maps.newLinkedHashMap();
    private static Map<String, SkillInstance> DATA_PACK_SKILLS = Maps.newLinkedHashMap();

    public static void init() {
        createSkillsFolder();
        reloadFromConfig();
    }

    private static void reloadFromConfig() {
        Map<String, SkillInstance> loaded = Maps.newLinkedHashMap();
        try (var stream = Files.walk(SKILLS_DIR, MAX_DEPTH)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equalsIgnoreCase(SKILL_FILE_NAME))
                    .forEach(path -> loadSkillFromConfig(path, loaded));
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.warn("Failed to scan config skills directory {}", SKILLS_DIR, e);
        }

        CONFIG_SKILLS = ImmutableMap.copyOf(loaded);
    }

    public static void loadSkillFromDatapack(Map<String, SkillInstance> skills) {
        DATA_PACK_SKILLS = ImmutableMap.copyOf(skills);
    }

    private static void loadSkillFromConfig(Path path, Map<String, SkillInstance> loaded) {
        try {
            SkillInstance skill = parse(path);
            if (skill != null) {
                loaded.put(skill.name(), skill);
                TouhouLittleMaid.LOGGER.info("Loaded skill {} from file {}", skill.name(), path);
            }
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to load skill from file {}", path, e);
        }
    }

    /**
     * 读取特定目录下的 skill
     *
     * @param skillFilePath SKILL.md 文件的路径
     * @return 读取后的内容
     */
    private static SkillInstance parse(Path skillFilePath) {
        try (InputStream stream = Files.newInputStream(skillFilePath)) {
            Pair<SkillBean, String> result = SkillParser.parse(stream, skillFilePath.toString());
            if (result == null) {
                return null;
            }

            SkillBean header = result.getLeft();
            String body = result.getRight();
            Map<String, String> references = Maps.newLinkedHashMap();

            // 检查 references 目录是否存在，以及其下有无其他文件
            Path referencesPath = skillFilePath.getParent().resolve(REFERENCES);
            if (!Files.isDirectory(referencesPath)) {
                return new SkillInstance(
                        header.getName().trim(),
                        header.getDescription().trim(),
                        header.getMetadata(),
                        body, Map.of()
                );
            }

            // 读取 referencesPath 下所有的参考文件
            try (var walk = Files.walk(referencesPath, 1)) {
                walk.filter(Files::isRegularFile).forEach(refPath -> {
                    try {
                        String content = Files.readString(refPath);
                        references.put(refPath.getFileName().toString(), content);
                    } catch (IOException e) {
                        TouhouLittleMaid.LOGGER.warn("Failed to read reference file {} for skill {}, skipping this reference. Error: {}",
                                refPath, header.getName(), e.getMessage());
                    }
                });
            } catch (IOException e) {
                TouhouLittleMaid.LOGGER.warn("Failed to scan references directory {} for skill {}, skipping all references. Error: {}",
                        referencesPath, header.getName(), e.getMessage());
            }

            return new SkillInstance(
                    header.getName().trim(),
                    header.getDescription().trim(),
                    header.getMetadata(),
                    body, ImmutableMap.copyOf(references)
            );
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.error("Failed to read skill file {}", skillFilePath, e);
            return null;
        }
    }

    public static SkillInstance getSkill(String name) {
        if (DATA_PACK_SKILLS.containsKey(name)) {
            return DATA_PACK_SKILLS.get(name);
        }
        return CONFIG_SKILLS.get(name);
    }

    public static boolean isEmpty() {
        return CONFIG_SKILLS.isEmpty() && DATA_PACK_SKILLS.isEmpty();
    }

    public static Map<String, SkillInstance> getAllSkills() {
        Map<String, SkillInstance> all = Maps.newLinkedHashMap();
        all.putAll(DATA_PACK_SKILLS);
        all.putAll(CONFIG_SKILLS);
        return all;
    }

    /**
     * 学习 OpenCode，将 Skills 转换为无换行、无缩进的单行 XML 字符串（专为 LLM Prompt 优化）
     */
    public static String getSkillSummary() {
        // 边界处理
        if (isEmpty()) {
            return "No skills are currently available.";
        }

        // 数据合并，config 优先于 data pack
        Map<String, SkillInstance> skills = getAllSkills();

        StringBuilder sb = new StringBuilder("<available_skills>");
        for (var entry : skills.entrySet()) {
            // 使用 Guava 进行安全的 XML 转义
            String safeName = XmlEscapers.xmlContentEscaper().escape(entry.getKey());
            String safeDesc = XmlEscapers.xmlContentEscaper().escape(entry.getValue().description());
            // 紧凑拼接，不包含任何多余的空格或换行
            sb.append("<skill>");
            sb.append("<name>").append(safeName).append("</name>");
            sb.append("<description>").append(safeDesc).append("</description>");
            sb.append("</skill>");
        }
        sb.append("</available_skills>");
        return sb.toString();
    }

    private static void createSkillsFolder() {
        try {
            if (Files.isDirectory(SKILLS_DIR)) {
                return;
            }
            Files.createDirectories(SKILLS_DIR);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to create skills directory {}", SKILLS_DIR, e);
        }
    }
}

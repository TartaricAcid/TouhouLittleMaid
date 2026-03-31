package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SkillParser {
    private static final Yaml YAML = new Yaml(new Constructor(SkillBean.class, new LoaderOptions()));
    private static final String YAML_FRONT_MATTER = "---";

    /**
     * 读取主文件，也就是 SKILL.md
     *
     * @param stream SKILL.md 文件的输入流
     * @return 读取后的内容，包含 yaml 头部和字符串本体
     */
    @Nullable
    public static Pair<SkillBean, String> parse(InputStream stream, String fileName) {
        try {
            List<String> lines = IOUtils.readLines(stream, StandardCharsets.UTF_8);
            StringBuilder yamlContent = new StringBuilder();
            boolean isInsideYaml = false;
            int bodyStartIndex = 0;

            for (String line : lines) {
                bodyStartIndex++;

                // 读取两个 --- 包裹起来的内容
                if (YAML_FRONT_MATTER.equals(line.trim())) {
                    if (!isInsideYaml) {
                        isInsideYaml = true;
                        continue;
                    } else {
                        break;
                    }
                }

                // 将 YAML 块内的内容拼起来
                if (isInsideYaml) {
                    yamlContent.append(line).append("\n");
                }
            }

            String headerText = yamlContent.toString();

            // 如果啥也没有读取到
            if (headerText.isEmpty()) {
                return null;
            }

            // 如果没有本体
            if (bodyStartIndex >= lines.size()) {
                return null;
            }

            SkillBean header;
            String body = String.join("\n", lines.subList(bodyStartIndex, lines.size())).trim();

            // 解析头部
            try {
                header = YAML.load(headerText);
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.warn("Failed to parse skill YAML front matter in file {}, skipping this skill. Error: {}", fileName, e.getMessage());
                return null;
            }

            // 字段校验
            if (header == null) {
                TouhouLittleMaid.LOGGER.warn("Skip invalid skill yml {} because it cannot be parsed", fileName);
                return null;
            }

            if (StringUtils.isBlank(header.getName()) || StringUtils.isBlank(header.getDescription())) {
                TouhouLittleMaid.LOGGER.warn("Skip invalid skill yml {} because name or description is missing or blank", fileName);
                return null;
            }

            if (StringUtils.isBlank(body)) {
                TouhouLittleMaid.LOGGER.warn("Skip invalid skill yml {} because body is missing or blank", fileName);
                return null;
            }

            return Pair.of(header, body);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.warn("Failed to read skill file {}, skipping this skill. Error: {}", fileName, e.getMessage());
            return null;
        }
    }
}

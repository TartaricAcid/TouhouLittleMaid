package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 此类使用 JavaBean 风格的 getter / setter，主要是为了让 SnakeYAML
 * 可以直接按字段名进行反序列化，而不必手工从 Map 中逐层取值。
 */
public class SkillBean {
    private String name;
    private String description;
    private @Nullable Map<String, String> metadata;

    public SkillBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(@Nullable Map<String, String> metadata) {
        this.metadata = metadata;
    }
}

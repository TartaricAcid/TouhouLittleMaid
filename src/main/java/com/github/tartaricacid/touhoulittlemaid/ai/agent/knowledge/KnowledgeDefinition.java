package com.github.tartaricacid.touhoulittlemaid.ai.agent.knowledge;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

/**
 * 知识库定义对象，对应单个 knowledge yml 文件。
 * <p>
 * 此类使用 JavaBean 风格的 getter / setter，主要是为了让 SnakeYAML
 * 可以直接按字段名进行反序列化，而不必手工从 Map 中逐层取值。
 */
@SuppressWarnings("all")
public class KnowledgeDefinition {
    /**
     * 知识库唯一标识，对应 tool 参数中的 knowledge_id。
     */
    private String id = "";

    /**
     * 知识库的简短描述，主要用于给 LLM 做路由提示。
     */
    private String desc = "";

    /**
     * 默认正文，必填字段。
     * <p>
     * 当 localized 中不存在当前 chatLanguage 对应内容时，会回退到此正文。
     */
    private String body = "";

    /**
     * 可选的本地化正文映射，key 使用 Minecraft 语言代码格式，例如 zh_cn、ja_jp。
     */
    @Nullable
    private Map<String, String> localized;

    public KnowledgeDefinition() {
    }

    public KnowledgeDefinition(String id, String desc, String body, @Nullable Map<String, String> localized) {
        this.id = id;
        this.desc = desc;
        this.body = body;
        this.localized = localized;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Nullable
    public Map<String, String> getLocalized() {
        return localized;
    }

    public void setLocalized(@Nullable Map<String, String> localized) {
        this.localized = localized;
    }

    public String getResolvedBody(String chatLanguage) {
        if (localized == null || localized.isEmpty()) {
            return body;
        }
        String normalized = StringUtils.defaultIfBlank(chatLanguage, "en_us")
                .toLowerCase(Locale.ROOT)
                .replace('-', '_');
        return localized.getOrDefault(normalized, body);
    }
}

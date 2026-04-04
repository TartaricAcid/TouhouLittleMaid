package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.context.GameContextRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 在用户每次主动对话前，在开头插入一小段上下文
 */
public class UserPromptContexts {
    public static final String CONTEXT_START = "<context>";
    public static final String CONTEXT_END = "</context>";
    public static final Pattern CONTEXT_REG = Pattern.compile(CONTEXT_START + "[\\s\\S]*?" + CONTEXT_END);

    public static String removeContext(String message) {
        if (message.startsWith(CONTEXT_START) && message.contains(CONTEXT_END)) {
            return CONTEXT_REG.matcher(message).replaceFirst("").trim();
        }
        return message;
    }

    public static String addContext(EntityMaid maid, String userMsg) {
        StringBuilder builder = new StringBuilder();

        builder.append(CONTEXT_START);
        appendContext(builder, maid);
        builder.append(CONTEXT_END);

        builder.append("\n");
        builder.append(userMsg);

        return builder.toString();
    }

    private static void appendContext(StringBuilder builder, EntityMaid maid) {
        GameContextRegister.allPromptCategories().forEach(category -> {
            String id = category.id();
            List<String> context = GameContextRegister.getContext(id, maid);
            if (context != null && !context.isEmpty()) {
                builder.append(String.join(", ", context)).append("\n");
            }
        });
    }
}

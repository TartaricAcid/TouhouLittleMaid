package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.grounded;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.PapiReplacer;
import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.papi.StringConstant;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.util.Map;

public final class GroundedAnswerPrompts {
    private static final String GROUNDED_INSTRUCTIONS = """
            ## Grounded Answer Instructions
            Answer the user's question using only the provided grounded knowledge.
            The grounded knowledge was already assembled before this pass, including any localized skill reference selected for the current chat language.
            If the knowledge is insufficient, say so honestly instead of inventing details.
            Keep the answer concise and directly useful.
            If nearby conversation context is included, first resolve any references (like "it", "that", "this") into a clear standalone question, then answer it.
            """;

    private GroundedAnswerPrompts() {
    }

    public static String systemPrompt(EntityMaid maid, String chatLanguage) {
        Map<String, String> valueMap = Util.make(Maps.newHashMap(), map -> {
            map.put("owner_name", PapiReplacer.getOwnerName(maid));
            map.put("chat_language", PapiReplacer.getChatLanguage(chatLanguage));
            map.put("tts_language", PapiReplacer.getTtsLanguage(maid));
        });

        StringBuilder builder = new StringBuilder();
        builder.append(GROUNDED_INSTRUCTIONS).append('\n');
        builder.append(new StrSubstitutor(valueMap).replace(StringConstant.GROUNDED_ANSWER_BASE)).append('\n');
        if (chatLanguage.equals(maid.getAiChatManager().getTTSLanguage())) {
            builder.append(new StrSubstitutor(valueMap).replace(StringConstant.OUTPUT_FORMAT_REQUIREMENTS_SAME_LANGUAGES));
        } else {
            builder.append(new StrSubstitutor(valueMap).replace(StringConstant.OUTPUT_FORMAT_REQUIREMENTS_DIFFERENT_LANGUAGES));
        }
        return builder.toString();
    }

    public static String buildUserPrompt(String question, String knowledgeText) {
        return """
                ## Prepared Knowledge Package
                %s
                
                ## User Question Or Normalization Context
                %s
                """.formatted(knowledgeText, question);
    }
}

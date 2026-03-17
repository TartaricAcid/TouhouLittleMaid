package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;

public interface ITool<T> {
    String id();

    String summary(EntityMaid maid);

    Parameter parameters(ObjectParameter root, EntityMaid maid);

    Codec<T> codec();

    ToolResponse onCall(T result, EntityMaid maid);

    default boolean trigger(EntityMaid maid, ChatCompletion chatCompletion) {
        return true;
    }
}

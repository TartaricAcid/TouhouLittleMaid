package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill;

import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.List;

public interface ISkill {
    String id();

    String summary(EntityMaid maid);

    String body(EntityMaid maid);

    List<String> tools(EntityMaid maid);

    default boolean trigger(EntityMaid maid) {
        return true;
    }
}

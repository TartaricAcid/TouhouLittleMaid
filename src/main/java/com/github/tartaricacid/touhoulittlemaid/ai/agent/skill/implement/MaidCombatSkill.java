package com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.skill.ISkill;
import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement.SetMaidAttackTargetTool;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

import java.util.Collections;
import java.util.List;

public class MaidCombatSkill implements ISkill {
    public static final String ID = "maid_combat";

    private final List<String> tools;

    public MaidCombatSkill() {
        this.tools = Collections.singletonList(SetMaidAttackTargetTool.TOOL_ID);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return "Use to designate a specific entity as the maid's attack target";
    }

    @Override
    public String body(EntityMaid maid) {
        return """
                ## Maid Combat Control (maid_combat)
                - Use when the player asks the maid to attack, target, or focus on a specific entity.
                - You should have already completed these steps BEFORE loading this skill:
                  1. Switched to an attack task via maid_work (if not already in one).
                  2. Queried nearby_entities via maid_context to obtain the target's numeric entity id.
                - Now call the set_maid_attack_target tool with the entity id you found.
                - If the target is rejected, explain the reason to the player and suggest alternatives.
                """;
    }

    @Override
    public List<String> tools(EntityMaid maid) {
        return tools;
    }

    @Override
    public boolean trigger(EntityMaid maid) {
        return maid.getTask() instanceof IAttackTask;
    }
}

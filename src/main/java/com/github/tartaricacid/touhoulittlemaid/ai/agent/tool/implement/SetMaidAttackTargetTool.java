package com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.implement;

import com.github.tartaricacid.touhoulittlemaid.ai.agent.tool.ITool;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.IntegerParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class SetMaidAttackTargetTool implements ITool<SetMaidAttackTargetTool.Result> {
    public static final String TOOL_ID = "set_maid_attack_target";

    private static final String TOOL_DESC = "Set a specific entity as the maid's attack target by entity id";
    private static final String ENTITY_ID_PARAM = "entity_id";
    private static final String ENTITY_ID_DESC = "The numeric entity id of the target.";

    private static final Codec<Result> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf(ENTITY_ID_PARAM).forGetter(Result::entityId))
                    .apply(instance, Result::new));

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public String summary(EntityMaid maid) {
        return TOOL_DESC;
    }

    @Override
    public Parameter parameters(ObjectParameter root, EntityMaid maid) {
        IntegerParameter entityId = IntegerParameter.create();
        entityId.setDescription(ENTITY_ID_DESC);
        root.addProperties(ENTITY_ID_PARAM, entityId);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return CODEC;
    }

    @Override
    public ToolResponse onCall(Result result, EntityMaid maid) {
        // 检查女仆是否处于攻击模式
        if (!(maid.getTask() instanceof IAttackTask attackTask)) {
            return new ToolResponse("The maid is not in an attack task. Switch to an attack task first before setting a target.");
        }

        // 通过数字 ID 查找实体
        Entity entity = maid.level.getEntity(result.entityId);
        if (!(entity instanceof LivingEntity target) || !target.isAlive()) {
            return new ToolResponse("No living entity found with id %d in the maid's surroundings.".formatted(result.entityId));
        }

        // 获取实体名称用于返回消息
        String targetName = target.getName().getString();

        // 先设置攻击目标，女仆攻击目标的前提的是 lastHurtByMob 是这个实体
        LivingEntity tmp = maid.getLastHurtByMob();
        maid.setLastHurtByMob(target);

        // 如果此时不能攻击，清除目标
        if (!attackTask.canAttack(maid, target)) {
            // 恢复原来的 lastHurtByMob，避免对后续行为造成影响
            maid.setLastHurtByMob(tmp);
            return new ToolResponse("Cannot attack %s — it is excluded by the maid's attack rules.".formatted(targetName));
        } else {
            maid.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
        }

        return new ToolResponse("Attack target success set to %s".formatted(targetName));
    }

    public record Result(int entityId) {
    }
}

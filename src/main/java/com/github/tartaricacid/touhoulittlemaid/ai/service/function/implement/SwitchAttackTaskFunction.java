package com.github.tartaricacid.touhoulittlemaid.ai.service.function.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.compat.tacz.TacCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import java.util.Optional;

public class SwitchAttackTaskFunction implements IFunctionCall<SwitchAttackTaskFunction.Result> {
    private static final String FUNCTION_ID = "switch_maid_attack_task";
    private static final String FUNCTION_DESC = """
            Use this function to change the maid's current combat or behavior task.
            Especially when the user asks to alter her fighting style, prepare for a battle, or stand down""";
    private static final String TASK_ID_PARAMETER_ID = "task_id";
    private static final String TASK_ID_PARAMETER_DESC = """
            task_id (string, required): The specific ID of the task you want the maid to switch to. Available tasks include:
            idle (Stands idle), attack (Melee combat), ranged_attack (Bow combat)
            crossbow_attack (Crossbow combat), danmaku_attack (Danmaku combat), trident_attack (Trident combat)""";
    private static final String GUN_TASK_ID_PARAMETER_DESC = "gun_attack (Gun combat)";
    private static final String SUCCESS = "Successfully switched to %s task";
    private static final String MISSING = "Successfully switched to %s task, but the corresponding weapon is missing";
    private static final String FAIL = "Switch failed and there is no task named %s";
    private static final String NO_CHANGE = "You're currently in %s task and don't need to switch";

    @Override
    public String getId() {
        return FUNCTION_ID;
    }

    @Override
    public String getDescription(EntityMaid maid) {
        return FUNCTION_DESC;
    }

    @Override
    public Parameter addParameters(ObjectParameter root, EntityMaid maid) {
        Parameter taskId = StringParameter.create()
                .setDescription(TASK_ID_PARAMETER_DESC)
                .addEnumValues("idle", "attack", "ranged_attack",
                        "crossbow_attack", "danmaku_attack", "trident_attack");
        // 兼容 TACZ
        if (TacCompat.isInstalled()) {
            taskId.setDescription(TASK_ID_PARAMETER_DESC + "\n" + GUN_TASK_ID_PARAMETER_DESC);
            taskId.addEnumValues("gun_attack");
        }
        root.addProperties(TASK_ID_PARAMETER_ID, taskId);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance ->
                instance.group(Codec.STRING.fieldOf(TASK_ID_PARAMETER_ID)
                                .forGetter(Result::id))
                        .apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        String id = result.id;
        ResourceLocation taskId = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, id);
        Optional<IMaidTask> optional = TaskManager.findTask(taskId);
        if (optional.isEmpty()) {
            return new ToolResponse(FAIL.formatted(id));
        }

        // 空闲模式额外判断
        IMaidTask task = optional.get();
        RangedWrapper backpack = maid.getAvailableBackpackInv();
        if (task == TaskManager.getIdleTask()) {
            putItemBack(maid, backpack);
            maid.setTask(task);
            return new ToolResponse(SUCCESS.formatted(id));
        }

        // 攻击模式判断
        if (!(task instanceof IAttackTask attackTask)) {
            return new ToolResponse(FAIL.formatted(id));
        }
        // 如果不需要拿武器并切换模式，那么不用执行后面的逻辑
        IMaidTask currentTask = maid.getTask();
        if (attackTask == currentTask && attackTask.isWeapon(maid, maid.getMainHandItem())) {
            return new ToolResponse(NO_CHANGE.formatted(id));
        }

        maid.setTask(task);

        // 将武器取到主手上
        int slot = ItemsUtil.findStackSlot(backpack, item -> attackTask.isWeapon(maid, item));
        if (slot >= 0) {
            int count = backpack.getStackInSlot(slot).getCount();
            ItemStack output = backpack.extractItem(slot, count, false);
            if (!maid.getMainHandItem().isEmpty()) {
                ItemStack mainhand = maid.getMainHandItem();
                backpack.setStackInSlot(slot, mainhand);
            }
            maid.setItemInHand(InteractionHand.MAIN_HAND, output);
            return new ToolResponse(SUCCESS.formatted(id));
        }
        return new ToolResponse(MISSING.formatted(id));
    }

    private void putItemBack(EntityMaid maid, RangedWrapper backpack) {
        if (maid.getMainHandItem().isEmpty()) {
            return;
        }
        ItemStack mainHandItem = maid.getMainHandItem();
        for (int i = 0; i < backpack.getSlots(); i++) {
            ItemStack stackInSlot = backpack.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                backpack.setStackInSlot(i, mainHandItem);
                maid.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                return;
            }
        }
    }

    public record Result(String id) {
    }
}

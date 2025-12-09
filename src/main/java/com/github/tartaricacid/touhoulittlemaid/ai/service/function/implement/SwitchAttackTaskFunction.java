package com.github.tartaricacid.touhoulittlemaid.ai.service.function.implement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskExtinguishing;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskFishing;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskHoney;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskShears;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import java.util.Map;
import java.util.Optional;

public class SwitchAttackTaskFunction implements IFunctionCall<SwitchAttackTaskFunction.Result> {
    private static final String FUNCTION_ID = "switch_maid_attack_task";
    private static final String FUNCTION_DESC = """
            Use this function to switch the maid to any available task, including non-combat tasks such as fishing, farming, torch placing, etc.
            This is the unified entry for all task changes, including combat tasks (attack, ranged_attack, crossbow_attack, danmaku_attack, trident_attack).""";
    private static final String TASK_ID_PARAMETER_ID = "task_id";
    private static final String TASK_ID_PARAMETER_DESC = """
            task_id (string, required): The specific ID of the task you want the maid to switch to.
            Examples include: idle, fishing, farm, torch, milk, shears, honey, extinguishing, feed, feed_animal,
            grass, snow, melon, cocoa, board_games, and combat tasks (attack, ranged_attack, crossbow_attack,
            danmaku_attack, trident_attack).""";
    private static final String SUCCESS = "Successfully switched to %s task";
    private static final String FAIL = "Switch failed and there is no task named %s";
    private static final String NO_CHANGE = "You're currently in %s task and don't need to switch";
    private static final String MISSING_WEAPON = "Successfully switched to %s task, but the corresponding weapon is missing";
    private static final String MISSING_ROD = "Successfully switched to %s task, but the corresponding rod is missing";
    private static final String MISSING_SHEARS = "Successfully switched to %s task, but the corresponding shears is missing";
    private static final String MISSING_SHEARS_AND_BOTTLE = "Successfully switched to %s task, but both shears and bottles are missing";
    private static final String MISSING_EXTINGUISHER = "Successfully switched to %s task, but the extinguisher is missing";

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
        StringParameter taskId = StringParameter.create();
        // 将所有可用任务加入枚举值（使用路径名，如 fishing、farm、torch 等）
        for (Map.Entry<ResourceLocation, IMaidTask> entry : TaskManager.getTaskMap().entrySet()) {
            taskId.addEnumValues(entry.getKey().getPath());
        }
        taskId.setDescription(TASK_ID_PARAMETER_DESC);
        root.addProperties(TASK_ID_PARAMETER_ID, taskId);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf(TASK_ID_PARAMETER_ID).forGetter(Result::id)
        ).apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        String id = result.id;
        ResourceLocation taskId = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, id);
        Optional<IMaidTask> optional = TaskManager.findTask(taskId);
        if (optional.isEmpty()) {
            return new ToolResponse(FAIL.formatted(id));
        }

        IMaidTask task = optional.get();
        IMaidTask currentTask = maid.getTask();
        RangedWrapper backpack = maid.getAvailableBackpackInv();

        // 空闲模式：收起主手物品
        if (task == TaskManager.getIdleTask()) {
            putItemBack(maid, backpack);
            maid.setTask(task);
            return new ToolResponse(SUCCESS.formatted(id));
        }

        // 战斗类任务：尝试拿到合适武器
        if (task instanceof IAttackTask attackTask) {
            if (attackTask == currentTask && attackTask.isWeapon(maid, maid.getMainHandItem())) {
                return new ToolResponse(NO_CHANGE.formatted(id));
            }
            maid.setTask(task);
            if (tryEquipFromBackpack(maid, backpack, item -> attackTask.isWeapon(maid, item))) {
                return new ToolResponse(SUCCESS.formatted(id));
            }
            return new ToolResponse(MISSING_WEAPON.formatted(id));
        }

        // 钓鱼任务：尝试拿出钓鱼竿
        if (task instanceof TaskFishing) {
            maid.setTask(task);
            if (tryEquipFromBackpack(maid, backpack, item -> item.canPerformAction(ItemAbilities.FISHING_ROD_CAST))) {
                return new ToolResponse(SUCCESS.formatted(id));
            }
            return new ToolResponse(MISSING_ROD.formatted(id));
        }

        // 剪羊毛：尝试把剪刀放到主手
        if (task instanceof TaskShears) {
            maid.setTask(task);
            if (tryEquipFromBackpack(maid, backpack, item -> item.canPerformAction(ItemAbilities.SHEARS_HARVEST))) {
                return new ToolResponse(SUCCESS.formatted(id));
            }
            return new ToolResponse(MISSING_SHEARS.formatted(id));
        }

        // 蜂巢采集：优先尝试把剪刀放到主手；没有剪刀则检查是否有玻璃瓶
        if (task instanceof TaskHoney) {
            maid.setTask(task);
            // 先尝试剪刀（采蜜脾）
            if (tryEquipFromBackpack(maid, backpack, item -> item.canPerformAction(ItemAbilities.SHEARS_HARVEST))) {
                return new ToolResponse(SUCCESS.formatted(id));
            }
            // 无剪刀则检查是否有玻璃瓶（采蜂蜜瓶无需主手）
            CombinedInvWrapper available = maid.getAvailableInv(false);
            if (ItemsUtil.isStackIn(available, stack -> stack.is(Items.GLASS_BOTTLE))) {
                return new ToolResponse(SUCCESS.formatted(id) + " (no shears, will use bottles)");
            }
            return new ToolResponse(MISSING_SHEARS_AND_BOTTLE.formatted(id));
        }

        // 灭火：尝试把灭火器放到主手
        if (task instanceof TaskExtinguishing) {
            maid.setTask(task);
            if (tryEquipFromBackpack(maid, backpack, item -> item.getItem() == InitItems.EXTINGUISHER.get())) {
                return new ToolResponse(SUCCESS.formatted(id));
            }
            return new ToolResponse(MISSING_EXTINGUISHER.formatted(id));
        }

        // 其他生活类任务：仅切换，不处理物品（各任务会自检）
        maid.setTask(task);
        return new ToolResponse(SUCCESS.formatted(id));
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

    private boolean tryEquipFromBackpack(EntityMaid maid, RangedWrapper backpack, java.util.function.Predicate<ItemStack> predicate) {
        if (predicate.test(maid.getMainHandItem())) {
            return true;
        }
        int slot = ItemsUtil.findStackSlot(backpack, predicate::test);
        if (slot >= 0) {
            int count = backpack.getStackInSlot(slot).getCount();
            ItemStack output = backpack.extractItem(slot, count, false);
            if (!maid.getMainHandItem().isEmpty()) {
                ItemStack mainhand = maid.getMainHandItem();
                backpack.setStackInSlot(slot, mainhand);
            }
            maid.setItemInHand(InteractionHand.MAIN_HAND, output);
            return true;
        }
        return false;
    }

    public record Result(String id) {
    }
}

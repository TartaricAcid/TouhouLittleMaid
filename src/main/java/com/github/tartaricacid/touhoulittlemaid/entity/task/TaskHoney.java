package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCollectHoneyTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.ItemAbilities;
import com.github.tartaricacid.touhoulittlemaid.api.task.FunctionCallSwitchResult;
import com.github.tartaricacid.touhoulittlemaid.util.TaskEquipUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class TaskHoney implements IMaidTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "honey");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.HONEY_BOTTLE.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_IDLE.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidCollectHoneyTask maidCollectHoneyTask = new MaidCollectHoneyTask(0.6f, 2);
        return Lists.newArrayList(Pair.of(5, maidCollectHoneyTask));
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_bottle", this::hasBottle), Pair.of("has_shears", this::hasShears));
    }

    private boolean hasBottle(EntityMaid maid) {
        return ItemsUtil.isStackIn(maid.getAvailableInv(false), stack -> stack.is(Items.GLASS_BOTTLE));
    }

    private boolean hasShears(EntityMaid maid) {
        return maid.getMainHandItem().canPerformAction(ItemAbilities.SHEARS_HARVEST);
    }

    @Override
    public FunctionCallSwitchResult onFunctionCallSwitch(EntityMaid maid) {
        // 优先将剪刀放入主手
        if (hasShears(maid)) {
            return FunctionCallSwitchResult.NO_CHANGE;
        }
        if (TaskEquipUtil.tryEquipFromBackpack(maid, item -> item.canPerformAction(ItemAbilities.SHEARS_HARVEST))) {
            return FunctionCallSwitchResult.OK;
        }
        // 若无剪刀，但有玻璃瓶则允许仅用瓶子进行部分功能（部分成功）
        if (hasBottle(maid)) {
            return FunctionCallSwitchResult.PARTIAL_OK;
        }
        return FunctionCallSwitchResult.MISSING_REQUIRED_ITEM;
    }
}

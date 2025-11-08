package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class BaseTaskJS implements IMaidTask {
    private final Builder builder;

    public BaseTaskJS(Builder builder) {
        this.builder = builder;
    }

    @Override
    public ResourceLocation getUid() {
        return this.builder.id;
    }

    @Override
    public ItemStack getIcon() {
        return this.builder.icon;
    }

    @Override
    @Nullable
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return this.builder.sound;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList();
        for (var pair : this.builder.brains) {
            tasks.add(Pair.of(pair.getFirst(), pair.getSecond().apply(this, maid)));
        }
        return tasks;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList();
        for (var pair : this.builder.rideBrains) {
            tasks.add(Pair.of(pair.getFirst(), pair.getSecond().apply(this, maid)));
        }
        return tasks;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        if (this.builder.enable == null) {
            return IMaidTask.super.isEnable(maid);
        }
        return this.builder.enable.test(maid);
    }

    @Override
    public boolean isHidden(EntityMaid maid) {
        if (this.builder.isHidden == null) {
            return IMaidTask.super.isHidden(maid);
        }
        return this.builder.isHidden.test(maid);
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        if (this.builder.enableLookAndRandomWalk == null) {
            return IMaidTask.super.enableLookAndRandomWalk(maid);
        }
        return this.builder.enableLookAndRandomWalk.test(maid);
    }

    @Override
    public boolean enablePanic(EntityMaid maid) {
        if (this.builder.enablePanic == null) {
            return IMaidTask.super.enablePanic(maid);
        }
        return this.builder.enablePanic.test(maid);
    }

    @Override
    public boolean enableEating(EntityMaid maid) {
        if (this.builder.enableEating == null) {
            return IMaidTask.super.enableEating(maid);
        }
        return this.builder.enableEating.test(maid);
    }

    @Override
    public boolean workPointTask(EntityMaid maid) {
        if (this.builder.workPointTask == null) {
            return IMaidTask.super.workPointTask(maid);
        }
        return this.builder.workPointTask.test(maid);
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return this.builder.enableConditionDesc;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return this.builder.conditionDesc;
    }

    @Override
    public float searchRadius(EntityMaid maid) {
        if (this.builder.searchRadius < 0) {
            return IMaidTask.super.searchRadius(maid);
        }
        return this.builder.searchRadius;
    }

    public static class Builder extends TaskBuilder<Builder, BaseTaskJS> {
        private @Nullable Predicate<EntityMaid> enablePanic = null;
        private @Nullable Predicate<EntityMaid> workPointTask = null;
        private float searchRadius = -1;

        public Builder(ResourceLocation id, ItemStack icon) {
            super(id, icon);
        }

        @Info("""
                Sets the condition to enable panic behavior for the maid. Default is true. <br>
                设置女仆是否启用惊慌行为的条件。默认为 true。
                """
        )
        public Builder enablePanic(Predicate<EntityMaid> enablePanic) {
            this.enablePanic = enablePanic;
            return this;
        }

        @Info("""
                Sets the condition to enable work point task for the maid. Default is false. <br>
                设置女仆是否启用工作点任务的条件。默认为 false。
                """
        )
        public Builder workPoint(Predicate<EntityMaid> workPointTask) {
            this.workPointTask = workPointTask;
            return this;
        }

        @Info("""
                Sets the search radius for the maid's task. Default is the maid's working range. <br>
                设置女仆任务的搜索半径。默认为女仆的工作范围。
                """
        )
        public Builder searchRadius(float searchRadius) {
            this.searchRadius = searchRadius;
            return this;
        }
    }
}


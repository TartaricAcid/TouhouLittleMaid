package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidWalkToLivingEntityTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WalkToLivingEntityTaskJS implements IMaidTask {
    private final WalkToLivingEntityTaskJS.Builder builder;

    public WalkToLivingEntityTaskJS(WalkToLivingEntityTaskJS.Builder builder) {
        this.builder = builder;
    }

    @Override
    public ResourceLocation getUid() {
        return builder.id;
    }

    @Override
    public ItemStack getIcon() {
        return builder.icon;
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid maid) {
        if (this.builder.sound == null) {
            return SoundUtil.environmentSound(maid, InitSounds.MAID_IDLE.get(), 0.5f);
        }
        return this.builder.sound;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidWalkToLivingEntityTask task = new MaidWalkToLivingEntityTask(0.6f, builder.closeEnoughDist,
                builder.startSearchPredicate, builder.entityPredicate, builder.arriveAction);
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(Pair.of(5, task));
        for (var pair : this.builder.brains) {
            tasks.add(Pair.of(pair.getFirst(), pair.getSecond().apply(this, maid)));
        }
        return tasks;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        if (this.builder.enable == null) {
            return true;
        }
        return this.builder.enable.test(maid);
    }

    @Override
    public boolean isHidden(EntityMaid maid) {
        if (this.builder.isHidden == null) {
            return false;
        }
        return this.builder.isHidden.test(maid);
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        if (this.builder.enableLookAndRandomWalk == null) {
            return true;
        }
        return this.builder.enableLookAndRandomWalk.test(maid);
    }

    @Override
    public boolean enableEating(EntityMaid maid) {
        if (this.builder.enableEating == null) {
            return true;
        }
        return this.builder.enableEating.test(maid);
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return this.builder.enableConditionDesc;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return this.builder.conditionDesc;
    }

    public static class Builder extends TaskBuilder<Builder, WalkToLivingEntityTaskJS> {
        private @Nullable Predicate<EntityMaid> startSearchPredicate = null;
        private @Nullable BiPredicate<EntityMaid, LivingEntity> entityPredicate = null;
        private @Nullable BiConsumer<EntityMaid, LivingEntity> arriveAction = null;
        private float closeEnoughDist = 2;

        public Builder(ResourceLocation id, ItemStack icon) {
            super(id, icon);
        }

        @Info("""
                Mandatory, this is the condition to check before starting the entity search. <br>
                Please only perform the search when necessary to reduce performance overhead. <br>
                必填项目，开始进行实体搜索之前的判断条件，请在必要时再进行搜索，减少性能消耗
                """)
        public WalkToLivingEntityTaskJS.Builder setStartSearchPredicate(Predicate<EntityMaid> predicate) {
            this.startSearchPredicate = predicate;
            return this;
        }

        @Info("""
                Mandatory, this is the condition to check whether the entity is suitable for walking to. <br>
                必填项目，搜索的实体是否是我们的目标实体
                """)
        public WalkToLivingEntityTaskJS.Builder setEntityPredicate(BiPredicate<EntityMaid, LivingEntity> predicate) {
            this.entityPredicate = predicate;
            return this;
        }

        @Info("""
                Lastly, this is the logic to execute when arriving near the entity. Mandatory, otherwise no logic will be executed. <br>
                最后到达实体附近需要执行的逻辑，必填内容，否则不执行任何逻辑
                """)
        public WalkToLivingEntityTaskJS.Builder setArriveAction(BiConsumer<EntityMaid, LivingEntity> action) {
            this.arriveAction = action;
            return this;
        }

        @Info("""
                Optional, default is 2. The logic will only be executed if the distance to the target entity is less than or equal to this number of blocks. <br>
                选填内容，默认为 2。当距离目标实体小于等于这里的格数时才会执行后续逻辑
                """)
        public WalkToLivingEntityTaskJS.Builder setCloseEnoughDist(float dist) {
            this.closeEnoughDist = dist;
            return this;
        }
    }
}

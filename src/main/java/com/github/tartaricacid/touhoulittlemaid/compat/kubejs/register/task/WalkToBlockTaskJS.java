package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.register.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidArriveAtBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WalkToBlockTaskJS implements IMaidTask {
    private final Builder builder;

    public WalkToBlockTaskJS(Builder builder) {
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
        MaidMoveToPredicateBlockTask moveToPredicateBlockTask = new MaidMoveToPredicateBlockTask(
                0.6f, builder.verticalSearchRange, builder.searchCondition, builder.blockPredicate);
        MaidArriveAtBlockTask arriveAtBlockTask = new MaidArriveAtBlockTask(builder.closeEnoughDist, builder.arriveAction);
        List<Pair<Integer, BehaviorControl<? super EntityMaid>>> tasks = Lists.newArrayList(
                Pair.of(5, moveToPredicateBlockTask),
                Pair.of(6, arriveAtBlockTask));
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

    public static class Builder extends TaskBuilder<Builder, WalkToBlockTaskJS> {
        private @Nullable Predicate<EntityMaid> searchCondition = null;
        private @Nullable BiPredicate<EntityMaid, BlockPos> blockPredicate = null;
        private @Nullable BiConsumer<EntityMaid, BlockPos> arriveAction;
        private double closeEnoughDist = 2;
        private int verticalSearchRange = 2;

        public Builder(ResourceLocation id, ItemStack icon) {
            super(id, icon);
        }

        @Info(value = """
                Must be set before searching for blocks. If not set, the search will not be performed.
                Please search only when necessary to reduce performance overhead. <br>
                必填项目，否则不进行搜索。开始进行搜索之前的判断条件，请在必要时在进行搜索，减少性能消耗
                """)
        public Builder setSearchCondition(Predicate<EntityMaid> condition) {
            this.searchCondition = condition;
            return this;
        }

        @Info(value = """
                Set the predicate for the block to be searched. If not set, the task will not search for blocks. <br>
                设置搜索的方块的判断条件，如果不设置，则不会进行方块搜索。
                """)
        public Builder setBlockPredicate(BiPredicate<EntityMaid, BlockPos> predicate) {
            this.blockPredicate = predicate;
            return this;
        }

        @Info(value = """
                Set the action to be performed when the maid arrives at the block. If not set, no action will be performed. <br>
                设置当女仆到达方块时执行的动作，如果不设置，则不会执行任何动作。
                """)
        public Builder setArriveAction(BiConsumer<EntityMaid, BlockPos> action) {
            this.arriveAction = action;
            return this;
        }

        @Info(value = """
                Set the distance at which the maid is considered close enough to the block. Default is 2 blocks. <br>
                设置女仆到达方块时的距离，低于该距离则认为到达。默认为 2 格。
                """)
        public Builder setCloseEnoughDist(double dist) {
            this.closeEnoughDist = dist;
            return this;
        }

        @Info(value = """
                Search range for the blocks. The search is limited to the maid's working area, so we can only customize the vertical height of the search. <br>
                But this value should not be too large, otherwise traversing blocks will cause serious performance overhead. <br>
                Default is 2, which means searching from -2 to 2 in the vertical direction. <br>
                搜索范围为女仆的工作范围，我们只能自定义搜索的垂直高度。 <br>
                此数值不宜过大，否则遍历方块会带来严重的性能消耗。默认为 2，也就是搜索 -2 ~ 2 的垂直范围。
                """)
        public Builder setVerticalSearchRange(int range) {
            this.verticalSearchRange = range;
            return this;
        }
    }
}

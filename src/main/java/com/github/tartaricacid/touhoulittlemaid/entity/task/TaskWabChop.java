package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabWorkJobs;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.List;

/**
 * WAB 工作模式：伐木。注册后右键女仆「工作模式」即可选中；
 * 具体参数在 WAB 设置面板（WabWorkScreen）里配。
 */
public class TaskWabChop implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "wab_chop");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.IRON_AXE.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return null;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return Lists.newArrayList(Pair.of(5, new WabGatherBehavior("chop")));
    }

    @Override
    public boolean enableLookAndRandomWalk(EntityMaid maid) {
        return false;
    }

    @Override
    public boolean enablePanic(EntityMaid maid) {
        return false;
    }

    @Override
    public String getMaidActionSummary() {
        return "Chop logs (WAB)";
    }

    /** 只负责把该女仆交给 WabWorkJobs；节流、起 Job、放方块都在 WabWorkJobs 内。 */
    public static class WabGatherBehavior extends Behavior<EntityMaid> {
        private final String action;

        public WabGatherBehavior(String action) {
            super(ImmutableMap.<MemoryModuleType<?>, MemoryStatus>of());
            this.action = action;
        }

        @Override
        protected void start(ServerLevel level, EntityMaid maid, long gameTime) {
            try {
                WabWorkJobs.ensureJob(maid, action);
            } catch (Exception ignored) {
                // 装饰性 AI 绝不影响女仆原有行为
            }
        }
    }
}

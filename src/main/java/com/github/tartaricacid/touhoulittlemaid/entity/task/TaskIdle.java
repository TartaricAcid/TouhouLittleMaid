package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidBegTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRunOne;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidSnowballTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidStartSnowballAttacking;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TaskIdle implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "idle");
    private static final float LOW_TEMPERATURE = 0.15F;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.FEATHER.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_IDLE.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToPlayer = Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToMaid = Pair.of(SetEntityLookTarget.create(EntityMaid.TYPE, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToWolf = Pair.of(SetEntityLookTarget.create(EntityType.WOLF, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToCat = Pair.of(SetEntityLookTarget.create(EntityType.CAT, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> lookToParrot = Pair.of(SetEntityLookTarget.create(EntityType.PARROT, 5), 1);
        Pair<BehaviorControl<? super EntityMaid>, Integer> noLook = Pair.of(new DoNothing(20, 40), 2);
        Pair<BehaviorControl<? super EntityMaid>, Integer> findSnowballTarget = Pair.of(new MaidStartSnowballAttacking<>(this::canSnowballFight, this::findFirstValidSnowballTarget), 2);
        MaidRunOne randomLookTask = new MaidRunOne(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, findSnowballTarget, noLook));
        MaidSnowballTargetTask snowballTargetTask = new MaidSnowballTargetTask(40);

        Pair<Integer, BehaviorControl<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        Pair<Integer, BehaviorControl<? super EntityMaid>> supplemented = Pair.of(6, randomLookTask);
        Pair<Integer, BehaviorControl<? super EntityMaid>> snowballFight = Pair.of(6, snowballTargetTask);
        return Lists.newArrayList(beg, supplemented, snowballFight);
    }

    private boolean canSnowballFight(EntityMaid maid) {
        Level world = maid.level();
        BlockPos pos = maid.blockPosition();
        return !maid.isBegging() && world.getBiome(pos).value().coldEnoughToSnow(pos) && world.getBlockState(pos).is(Blocks.SNOW);
    }

    private Optional<? extends LivingEntity> findFirstValidSnowballTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(
                list -> list.find(e -> isSnowballTarget(e, maid))
                        .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                        .findFirst());
    }

    private boolean isSnowballTarget(LivingEntity entity, EntityMaid maid) {
        if (maid.isOwnedBy(entity)) {
            return true;
        }
        if (entity instanceof EntityMaid && maid.getOwner() != null) {
            EntityMaid maidOther = (EntityMaid) entity;
            return maid.getOwner().equals(maidOther.getOwner());
        }
        return false;
    }
}

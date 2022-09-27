package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidBegTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidSnowballTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        Pair<Task<? super EntityMaid>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> noLook = Pair.of(new DummyTask(20, 40), 2);
        Pair<Task<? super EntityMaid>, Integer> findSnowballTarget = Pair.of(new ForgetAttackTargetTask<>(this::canSnowballFight, this::findFirstValidSnowballTarget), 2);
        FirstShuffledTask<EntityMaid> firstShuffledTask = new FirstShuffledTask<>(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, findSnowballTarget, noLook));
        SupplementedTask<EntityMaid> randomLookTask = new SupplementedTask<>(e -> !e.isBegging(), firstShuffledTask);

        MaidSnowballTargetTask snowballTargetTask = new MaidSnowballTargetTask(40);

        Pair<Integer, Task<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        Pair<Integer, Task<? super EntityMaid>> supplemented = Pair.of(6, randomLookTask);
        Pair<Integer, Task<? super EntityMaid>> snowballFight = Pair.of(6, snowballTargetTask);

        return Lists.newArrayList(beg, supplemented, snowballFight);
    }

    private boolean canSnowballFight(EntityMaid maid) {
        World world = maid.level;
        BlockPos pos = maid.blockPosition();
        return !maid.isBegging() && world.getBiome(pos).getTemperature(pos) < LOW_TEMPERATURE && world.getBlockState(pos).is(Blocks.SNOW);
    }

    private Optional<? extends LivingEntity> findFirstValidSnowballTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).flatMap(
                mobs -> mobs.stream().filter(e -> isSnowballTarget(e, maid))
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

package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidBegTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.List;

public class TaskIdle implements IMaidTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "idle");

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
    public List<Pair<Integer, Behavior<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        Pair<Behavior<? super EntityMaid>, Integer> lookToPlayer = Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 5), 1);
        Pair<Behavior<? super EntityMaid>, Integer> lookToMaid = Pair.of(new SetEntityLookTarget(EntityMaid.TYPE, 5), 1);
        Pair<Behavior<? super EntityMaid>, Integer> lookToWolf = Pair.of(new SetEntityLookTarget(EntityType.WOLF, 5), 1);
        Pair<Behavior<? super EntityMaid>, Integer> lookToCat = Pair.of(new SetEntityLookTarget(EntityType.CAT, 5), 1);
        Pair<Behavior<? super EntityMaid>, Integer> lookToParrot = Pair.of(new SetEntityLookTarget(EntityType.PARROT, 5), 1);
        Pair<Behavior<? super EntityMaid>, Integer> noLook = Pair.of(new DoNothing(20, 40), 2);
        RunOne<EntityMaid> firstShuffledTask = new RunOne<>(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, noLook));
        RunIf<EntityMaid> supplementedTask = new RunIf<>(e -> !e.isBegging(), firstShuffledTask);

        Pair<Integer, Behavior<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        Pair<Integer, Behavior<? super EntityMaid>> supplemented = Pair.of(6, supplementedTask);
        return Lists.newArrayList(beg, supplemented);
    }
}

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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

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
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        Pair<Task<? super EntityMaid>, Integer> lookToPlayer = Pair.of(new LookAtEntityTask(EntityType.PLAYER, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToMaid = Pair.of(new LookAtEntityTask(EntityMaid.TYPE, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToWolf = Pair.of(new LookAtEntityTask(EntityType.WOLF, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToCat = Pair.of(new LookAtEntityTask(EntityType.CAT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> lookToParrot = Pair.of(new LookAtEntityTask(EntityType.PARROT, 5), 1);
        Pair<Task<? super EntityMaid>, Integer> noLook = Pair.of(new DummyTask(20, 40), 2);
        FirstShuffledTask<EntityMaid> firstShuffledTask = new FirstShuffledTask<>(ImmutableList.of(lookToPlayer, lookToMaid, lookToWolf, lookToCat, lookToParrot, noLook));
        SupplementedTask<EntityMaid> supplementedTask = new SupplementedTask<>(e -> !e.isBegging(), firstShuffledTask);

        Pair<Integer, Task<? super EntityMaid>> beg = Pair.of(5, new MaidBegTask());
        Pair<Integer, Task<? super EntityMaid>> supplemented = Pair.of(6, supplementedTask);
        return Lists.newArrayList(beg, supplemented);
    }
}

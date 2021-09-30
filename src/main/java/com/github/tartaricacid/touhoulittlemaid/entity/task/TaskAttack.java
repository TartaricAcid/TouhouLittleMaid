package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class TaskAttack implements IAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "attack");
    private static final int MAX_STOP_ATTACK_DISTANCE = 8;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.DIAMOND_SWORD.getDefaultInstance();
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return InitSounds.MAID_ATTACK.get();
    }

    @Override
    public List<Pair<Integer, Task<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        SupplementedTask<EntityMaid> supplementedTask = new SupplementedTask<>(this::hasSword,
                new ForgetAttackTargetTask<>(IAttackTask::findFirstValidAttackTarget));
        FindNewAttackTargetTask<EntityMaid> findTargetTask = new FindNewAttackTargetTask<>(
                (target) -> !hasSword(maid) || farAway(target, maid));
        MoveToTargetTask moveToTargetTask = new MoveToTargetTask(0.6f);
        AttackTargetTask attackTargetTask = new AttackTargetTask(20);

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, attackTargetTask)
        );
    }

    @Override
    public List<ITextComponent> getDescription(EntityMaid maid) {
        return Collections.emptyList();
    }

    private boolean hasSword(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof SwordItem;
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }
}

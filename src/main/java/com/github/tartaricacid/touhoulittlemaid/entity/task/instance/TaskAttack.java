package com.github.tartaricacid.touhoulittlemaid.entity.task.instance;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskAttack implements IAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "attack");

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
                new ForgetAttackTargetTask<>(this::findRandomValidAttackTarget));
        FindNewAttackTargetTask<EntityMaid> findTargetTask = new FindNewAttackTargetTask<>((target) -> !hasSword(maid));
        MoveToTargetTask moveToTargetTask = new MoveToTargetTask(0.6f);
        AttackTargetTask attackTargetTask = new AttackTargetTask(20);

        return Lists.newArrayList(
                Pair.of(2, supplementedTask),
                Pair.of(2, findTargetTask),
                Pair.of(2, moveToTargetTask),
                Pair.of(2, attackTargetTask)
        );
    }

    @Override
    public List<ITextComponent> getDescription() {
        return Collections.emptyList();
    }

    private boolean hasSword(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof SwordItem;
    }

    private Optional<? extends LivingEntity> findRandomValidAttackTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).flatMap(
                mobs -> mobs.stream().filter(maid::canAttack)
                        .skip(maid.getRandom().nextInt(mobs.size()))
                        .findFirst());
    }
}

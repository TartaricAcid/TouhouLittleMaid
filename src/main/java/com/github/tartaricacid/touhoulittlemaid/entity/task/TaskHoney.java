package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCollectHoneyTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class TaskHoney implements IMaidTask {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "honey");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return Items.HONEY_BOTTLE.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_IDLE.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        MaidCollectHoneyTask maidCollectHoneyTask = new MaidCollectHoneyTask(0.6f, 2);
        return Lists.newArrayList(Pair.of(5, maidCollectHoneyTask));
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_bottle", this::hasBottle), Pair.of("has_shears", this::hasShears));
    }

    private boolean hasBottle(EntityMaid maid) {
        return ItemsUtil.isStackIn(maid.getAvailableInv(false), stack -> stack.is(Items.GLASS_BOTTLE));
    }

    private boolean hasShears(EntityMaid maid) {
        return maid.getMainHandItem().canPerformAction(ToolActions.SHEARS_HARVEST);
    }
}

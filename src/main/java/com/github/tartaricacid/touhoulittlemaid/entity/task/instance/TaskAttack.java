package com.github.tartaricacid.touhoulittlemaid.entity.task.instance;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public Task<EntityMaid> createTask(EntityMaid maid) {
        return null;
    }
}

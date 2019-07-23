package com.github.tartaricacid.touhoulittlemaid.internal.task;

import java.util.Random;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidAttackRanged;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskAttackRanged implements IMaidTask
{
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "ranged_attack");

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Items.BOW);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid, Random rand)
    {
        return MaidSoundEvent.MAID_RANGE_ATTACK;
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid)
    {
        return new EntityMaidAttackRanged((EntityMaid) maid, 0.6f, 2, 16);
    }

    @Override
    public boolean isAttack()
    {
        return true;
    }
}

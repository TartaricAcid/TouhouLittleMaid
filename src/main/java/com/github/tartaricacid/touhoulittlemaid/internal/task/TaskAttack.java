package com.github.tartaricacid.touhoulittlemaid.internal.task;

import java.util.Random;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskAttack implements IMaidTask
{
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "attack");

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Items.DIAMOND_SWORD);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid, Random rand)
    {
        if (maid.getAttackTarget() != null)
        {
            return MaidSoundEvent.MAID_FIND_TARGET;
        }
        else
        {
            return MaidSoundEvent.MAID_ATTACK;
        }
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid)
    {
        return new EntityMaidAttack(maid, 0.6f, false);
    }

    @Override
    public boolean isAttack()
    {
        return true;
    }
}

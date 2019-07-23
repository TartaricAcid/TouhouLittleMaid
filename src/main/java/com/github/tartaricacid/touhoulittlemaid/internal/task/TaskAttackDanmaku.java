package com.github.tartaricacid.touhoulittlemaid.internal.task;

import java.util.Random;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskAttackDanmaku extends TaskAttackRanged
{
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "danmaku_attack");

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Items.ENDER_PEARL);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid, Random rand)
    {
        return MaidSoundEvent.MAID_DANMAKU_ATTACK;
    }
}

package com.github.tartaricacid.touhoulittlemaid.internal.task;

import java.util.Random;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.util.Util;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskIdle implements IMaidTask
{
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "idle");

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Items.FEATHER);
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid)
    {
        return Util.environmentSound(maid, MaidSoundEvent.MAID_IDLE, 0.2f);
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid)
    {
        return null;
    }
}

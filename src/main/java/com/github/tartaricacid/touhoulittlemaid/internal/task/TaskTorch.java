package com.github.tartaricacid.touhoulittlemaid.internal.task;

import java.util.Random;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.util.Util;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityMaidPlaceTorch;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class TaskTorch implements IMaidTask
{
    public static final ResourceLocation UID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "torch");

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Item.getItemFromBlock(Blocks.TORCH));
    }

    @Override
    public SoundEvent getAmbientSound(AbstractEntityMaid maid, Random rand)
    {
        return Util.environmentSound(maid, MaidSoundEvent.MAID_TORCH, 0.2f, rand);
    }

    @Override
    public EntityAIBase createAI(AbstractEntityMaid maid)
    {
        return new EntityMaidPlaceTorch((EntityMaid) maid, 7, 2, 0.6f);
    }
}

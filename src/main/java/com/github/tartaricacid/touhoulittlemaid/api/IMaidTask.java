package com.github.tartaricacid.touhoulittlemaid.api;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public interface IMaidTask
{
    ResourceLocation getUid();

    ItemStack getIcon();

    @Nullable
    SoundEvent getAmbientSound(AbstractEntityMaid maid);

    @Nullable
    EntityAIBase createAI(AbstractEntityMaid maid);

    default boolean isAttack()
    {
        return false;
    }

    default void onRangedAttack(AbstractEntityMaid maid, EntityLivingBase target, float distanceFactor)
    {
    }
}

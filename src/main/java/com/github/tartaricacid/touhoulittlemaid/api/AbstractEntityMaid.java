package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;

public abstract class AbstractEntityMaid extends EntityTameable implements IRangedAttackMob
{

    public AbstractEntityMaid(World worldIn)
    {
        super(worldIn);
    }

    abstract public BaubleItemHandler getBaubleInventory();
}

package com.github.tartaricacid.touhoulittlemaid.api;

import javax.annotation.Nullable;

import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

public abstract class AbstractEntityMaid extends EntityTameable implements IRangedAttackMob
{

    public AbstractEntityMaid(World worldIn)
    {
        super(worldIn);
    }

    abstract public BaubleItemHandler getBaubleInv();

    abstract public IItemHandlerModifiable getAvailableInv();

    abstract public IItemHandlerModifiable getInv(MaidInventory type);

    abstract public boolean isFarmItemInInventory();
}

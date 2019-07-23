package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.passive.EntityTameable;
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

    /**
     * 检查女仆背包内是否有箭
     */
    abstract public boolean hasArrow();

    abstract public boolean isFarmItemInInventory();
}

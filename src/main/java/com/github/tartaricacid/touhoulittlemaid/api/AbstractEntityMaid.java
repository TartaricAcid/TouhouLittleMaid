package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;


/**
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public abstract class AbstractEntityMaid extends EntityTameable implements IRangedAttackMob {
    public AbstractEntityMaid(World worldIn) {
        super(worldIn);
    }

    /**
     * 获取女仆的饰品栏
     *
     * @return 饰品栏对象
     */
    abstract public BaubleItemHandler getBaubleInv();

    /**
     * 获取女仆可用的物品栏，可用指的是能够塞入普通物品的地方，一般指代女仆的主手副手和主物品栏
     *
     * @return IItemHandlerModifiable 对象
     */
    abstract public IItemHandlerModifiable getAvailableInv();

    /**
     * 获取女仆的物品栏
     *
     * @param type 有主物品栏、主手副手、护甲栏、饰品栏
     * @return IItemHandlerModifiable 对象
     */
    abstract public IItemHandlerModifiable getInv(MaidInventory type);

    abstract public boolean destroyBlock(BlockPos pos);

    abstract public boolean placeBlock(BlockPos pos, IBlockState state);

    abstract public boolean isHome();

    abstract public boolean isPickup();
}

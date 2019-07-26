package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


/**
 * 用于女仆在农场模式下的判定收割、种植行为的接口
 *
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public interface FarmHandler extends TaskHandler {
    /**
     * 传入的 ItemStack 对象是否可以作为种子
     *
     * @param stack ItemStack
     * @return boolean
     */
    boolean isSeed(ItemStack stack);

    /**
     * 当前位置方块是否可以收获
     *
     * @param maid  女仆对象
     * @param world 所处的世界
     * @param pos   方块坐标
     * @param state 方块的 IBlockState
     * @return 是否可以收获
     */
    boolean canHarvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state);

    /**
     * 对应的收获行为
     *
     * @param maid  女仆对象
     * @param world 所处的世界
     * @param pos   方块坐标
     * @param state 方块的 IBlockState
     */
    void harvest(AbstractEntityMaid maid, World world, BlockPos pos, IBlockState state);

    /**
     * 该位置是否可以种植作物
     *
     * @param maid  女仆对象
     * @param world 所处的世界
     * @param pos   方块坐标
     * @param seed  种子
     * @return 是否可以种植
     */
    boolean canPlant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed);

    /**
     * 对应的种植行为
     *
     * @param maid  女仆对象
     * @param world 所处的世界
     * @param pos   方块坐标
     * @param seed  种子
     * @return 种植后返回的物品
     */
    ItemStack plant(AbstractEntityMaid maid, World world, BlockPos pos, ItemStack seed);

    /**
     * 是否启用此 Handler
     *
     * @param maid 女仆对象
     * @return boolean
     */
    @Override
    default boolean canExecute(AbstractEntityMaid maid) {
        return true;
    }
}

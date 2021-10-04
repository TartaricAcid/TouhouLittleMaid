package com.github.tartaricacid.touhoulittlemaid.api.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

public interface IMultiBlock {
    /**
     * 触发的构建多方块结构时，点击的方块是否为核心方块
     *
     * @param blockState 点击的方块
     * @return 是否为核心方块
     */
    boolean isCoreBlock(BlockState blockState);

    /**
     * 触发的构建多方块结构时，朝向是否正确
     *
     * @param direction 朝向
     * @return 是否为合法的触发方向
     */
    boolean directionIsSuitable(Direction direction);

    /**
     * 获取多方块结构的中心点
     *
     * @param direction 朝向
     * @return 多方块结构的中心点
     */
    BlockPos getCenterPos(Direction direction);

    /**
     * 获取多方块结构模板
     *
     * @param world     世界
     * @param direction 朝向
     * @return 多方块结构模板
     */
    Template getTemplate(ServerWorld world, Direction direction);

    /**
     * 是否匹配该多方块结构
     *
     * @param world     世界实例
     * @param posStart  起始坐标
     * @param direction 判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template  结构模板
     * @return 是否匹配
     */
    boolean isMatch(World world, BlockPos posStart, Direction direction, Template template);

    /**
     * 修建多方块结构的逻辑
     *
     * @param worldIn   世界实例
     * @param posStart  起始坐标
     * @param direction 判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template  结构模板
     */
    void build(World worldIn, BlockPos posStart, Direction direction, Template template);
}

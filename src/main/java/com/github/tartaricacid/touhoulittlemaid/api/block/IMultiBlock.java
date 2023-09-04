package com.github.tartaricacid.touhoulittlemaid.api.block;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

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
    StructureTemplate getTemplate(ServerLevel world, Direction direction);

    /**
     * 是否匹配该多方块结构
     *
     * @param world     世界实例
     * @param posStart  起始坐标
     * @param direction 判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template  结构模板
     * @return 是否匹配
     */
    boolean isMatch(Level world, BlockPos posStart, Direction direction, StructureTemplate template);

    /**
     * 修建多方块结构的逻辑
     *
     * @param worldIn   世界实例
     * @param posStart  起始坐标
     * @param direction 判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template  结构模板
     */
    void build(Level worldIn, BlockPos posStart, Direction direction, StructureTemplate template);
}

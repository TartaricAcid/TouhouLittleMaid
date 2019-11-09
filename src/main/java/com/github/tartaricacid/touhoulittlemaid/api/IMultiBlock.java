package com.github.tartaricacid.touhoulittlemaid.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

/**
 * 多方块结构接口，临时的，未优化的接口
 *
 * @author TartaricAcid
 * @date 2019/11/9 12:30
 */
public interface IMultiBlock {
    /**
     * 触发的构建多方块结构时，点击的方块是否合适
     *
     * @param blockState 点击的方块
     * @return 是否为合法的触发方块
     */
    boolean blockIsSuitable(IBlockState blockState);

    /**
     * 触发的构建多方块结构时，朝向是否正确
     *
     * @param facing 朝向
     * @return 是否为合法的触发方向
     */
    boolean facingIsSuitable(EnumFacing facing);

    /**
     * 获取多方块结构的中心点
     *
     * @param facing 朝向
     * @return 多方块结构的中心点
     */
    BlockPos getCenterPos(EnumFacing facing);

    /**
     * 获取多方块结构模板
     *
     * @param worldIn 世界
     * @param facing  朝向
     * @return 多方块结构模板
     */
    Template getTemplate(World worldIn, EnumFacing facing);

    /**
     * 是否匹配该多方块结构
     *
     * @param worldIn  世界实例
     * @param posStart 起始坐标
     * @param facing   判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template 结构模板
     * @return 是否匹配
     */
    boolean isMatch(World worldIn, BlockPos posStart, EnumFacing facing, Template template);

    /**
     * 修建多方块结构的逻辑
     *
     * @param worldIn  世界实例
     * @param posStart 起始坐标
     * @param facing   判定匹配时的朝向，用来应用到一些具有方向的多方块结构
     * @param template 结构模板
     */
    void build(World worldIn, BlockPos posStart, EnumFacing facing, Template template);
}

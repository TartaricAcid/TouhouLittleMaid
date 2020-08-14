package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.api.util.BaubleItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public abstract class AbstractEntityMaid extends EntityTameable implements IRangedAttackMob {
    public AbstractEntityMaid(World worldIn) {
        super(worldIn);
    }

    /**
     * 获取女仆的当前AI的UID
     *
     * @return AI的UID
     */
    abstract public ResourceLocation getTaskUid();

    /**
     * 获取女仆的饰品栏
     *
     * @return 饰品栏对象
     */
    abstract public BaubleItemHandler getBaubleInv();

    /**
     * 获取女仆可用的物品栏，可用指的是能够塞入普通物品的地方，一般指代女仆的主手副手和主物品栏
     *
     * @param handsFirst 是否主手副手栏优先
     * @return IItemHandlerModifiable 对象
     */
    abstract public IItemHandlerModifiable getAvailableInv(boolean handsFirst);

    /**
     * 获取女仆的物品栏
     *
     * @param type 有主物品栏、主手副手、护甲栏、饰品栏
     * @return IItemHandlerModifiable 对象
     */
    abstract public IItemHandlerModifiable getInv(MaidInventory type);

    /**
     * 判断主手物品是否满足条件，否则寻找优先级最大的物品交换到主手
     *
     * @param itemRule        如果主手满足条件，则无需交换
     * @param priorityHandler 如果主手不满足条件，则交换。优先级大于0才是有效物品，寻找优先级最大的物品
     * @return 返回true主手物品已符合条件，反之false
     */
    abstract public boolean MoveItemsToMainhandForMaxPriority(Predicate<ItemStack> itemRule, Function<ItemStack, Integer> priorityHandler);

    /**
     * 女仆能否破坏方块
     *
     * @param pos 破坏的方块所处的坐标
     * @return boolean
     */
    abstract public boolean canDestroyBlock(BlockPos pos);

    /**
     * 女仆执行破坏方块时的行为
     *
     * @param pos 破坏的方块坐标
     * @return 是否成功破坏
     */
    @Deprecated
    abstract public boolean destroyBlock(BlockPos pos);

    /**
     * 女仆执行破坏方块时的行为
     *
     * @param pos       破坏的方块坐标
     * @param dropBlock 该破坏是否掉落方块
     * @return 是否成功破坏
     */
    abstract public boolean destroyBlock(BlockPos pos, boolean dropBlock);

    /**
     * 女仆能否放置方块
     *
     * @param pos   放置的坐标
     * @param state 放置的方块 IBlockState
     * @return boolean
     */
    abstract public boolean canPlaceBlock(BlockPos pos, IBlockState state);

    /**
     * 女仆执行放置方块的行为
     *
     * @param pos   放置方块的坐标
     * @param state 放置方块的 IBlockState
     * @return 是否成功放置
     */
    abstract public boolean placeBlock(BlockPos pos, IBlockState state);

    /**
     * 当前女仆是否处于 Home 模式
     *
     * @return boolean
     */
    abstract public boolean isHomeModeEnable();

    /**
     * 获取经验值
     *
     * @return 经验值
     */
    abstract public int getExp();

    /**
     * 设置经验值
     *
     * @param expIn 经验值
     */
    abstract public void setExp(int expIn);

    /**
     * 添加经验值
     *
     * @param expIn 经验值
     */
    abstract public void addExp(int expIn);

    /**
     * 当前女仆是否处于拾物模式
     *
     * @return boolean
     */
    abstract public boolean isPickup();

    /**
     * 当前女仆是否拥有旗指物
     *
     * @return boolean
     */
    abstract public boolean hasSasimono();

    /**
     * 女仆当前的模型 id
     *
     * @return String
     */
    abstract public String getModelId();

    /**
     * 设置女仆的模型 id
     *
     * @param modelId String
     */
    abstract public void setModelId(String modelId);

    /**
     * 获取女仆的冷却时间计数器
     *
     * @return 冷却时间计数器
     */
    abstract public CooldownTracker getCooldownTracker();

    /**
     * 能够持有拉杆箱
     */
    abstract public boolean isCanHoldTrolley();

    /**
     * 能够持有载具
     */
    abstract public boolean isCanHoldVehicle();

    /**
     * 能够骑乘扫帚
     */
    abstract public boolean isCanRidingBroom();
}

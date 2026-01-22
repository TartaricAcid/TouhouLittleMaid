package com.github.tartaricacid.touhoulittlemaid.api.block;

import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.ApiStatus;

/**
 * 方块食物接口，用于让女仆与方块类食物进行交互。
 * <p>
 * 女仆会在工作日程时寻找并食用周围实现了此接口的方块食物。
 * 此外，女仆还可以将背包中的食物物品放置为方块食物后再进行食用。
 * <p>
 * 实现此接口的方块需要处理以下两种场景：
 * <ul>
 *     <li>世界中已存在的方块食物的食用逻辑</li>
 *     <li>从物品堆放置为方块食物的逻辑（可选）</li>
 * </ul>
 *
 */
@ApiStatus.AvailableSince("1.4.7")
public interface IMaidEdibleBlock {
    /**
     * 工具方法，用于判断指定方块下方是否为零食架方块。
     */
    static boolean belowIsSnackStand(EntityMaid maid, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = maid.level.getBlockState(belowPos);
        return belowState.is(TagBlock.MAID_SNACK_STAND_BLOCK);
    }

    /**
     * 判断女仆是否应该移动到该方块食物处进行食用。
     * <p>
     * <b>性能提示：</b>此方法会被频繁调用，请确保实现的高效性。
     *
     * @param maid  女仆实体
     * @param pos   方块位置
     * @param state 方块状态
     * @return 若女仆应移动到该位置进行食用返回 {@code true}，否则返回 {@code false}
     */
    boolean shouldMoveTo(EntityMaid maid, BlockPos pos, BlockState state);

    /**
     * 获取女仆食用该方块食物时所获得的好感度点数。
     *
     * @param maid  女仆实体
     * @param pos   方块位置
     * @param state 方块状态
     * @return 好感度点数，该好感每游戏日会尝试执行 6 次，不建议设置过高，一般在 1-4 之间较为合适
     */
    int getFavorabilityPoints(EntityMaid maid, BlockPos pos, BlockState state);

    /**
     * 执行女仆食用方块食物的逻辑。
     * <p>
     * 实现此方法时，你需要自行处理方块状态的变更（如减少食物层数或移除方块）。
     *
     * @param maid  女仆实体
     * @param pos   方块位置
     * @param state 方块状态
     * @return 若食用成功返回 {@code true}，女仆将播放挥动手臂的动画，并增加对应好感度；
     * 否则返回 {@code false}
     */
    boolean consume(EntityMaid maid, BlockPos pos, BlockState state);

    /**
     * 判断该物品是否可以被女仆放置为方块食物。
     * <p>
     * 此方法用于过滤女仆背包中可放置的食物物品。
     *
     * @param maid      女仆实体
     * @param stack     待检测的物品堆
     * @param slotIndex 物品所在的背包槽位索引，方便做额外判断
     * @return 若该物品可被放置为方块食物返回 {@code true}，否则返回 {@code false}
     */
    default boolean canPlaceAsFood(EntityMaid maid, ItemStack stack, int slotIndex) {
        return false;
    }

    /**
     * 判断女仆是否应该将物品堆放置到指定位置作为方块食物。
     * <p>
     * <b>性能提示：</b>此方法会被频繁调用，请确保实现的高效性。
     *
     * @param maid  女仆实体
     * @param pos   待放置的位置
     * @param state 待放置位置的方块状态
     * @param stack 待放置的物品堆
     * @return 若女仆应将该物品放置到此位置返回 {@code true}，否则返回 {@code false}
     */
    default boolean shouldPlaceTo(EntityMaid maid, BlockPos pos, BlockState state, ItemStack stack) {
        // 不能放在脚下
        if (pos.equals(maid.blockPosition())) {
            return false;
        }
        // 目标位置能放东西
        if (!state.canBeReplaced()) {
            return false;
        }
        // 必须放在零食架上
        return belowIsSnackStand(maid, pos);
    }

    /**
     * 执行女仆将物品放置为方块食物的逻辑。
     * <p>
     * 实现此方法时，你需要自行处理物品数量的减少以及方块的放置逻辑。
     *
     * @param maid      女仆实体
     * @param pos       待放置的位置
     * @param stack     待放置的物品堆
     * @param slotIndex 物品所在的背包槽位索引，方便扣除物品
     * @return 若放置成功返回 {@code true}，女仆将播放挥动手臂的动画；否则返回 {@code false}
     */
    default boolean placeAsFood(EntityMaid maid, BlockPos pos, ItemStack stack, int slotIndex) {
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        ItemStack stackExtra = availableInv.extractItem(slotIndex, 1, false);
        if (stackExtra.isEmpty()) {
            return false;
        }
        return maid.placeItemBlock(pos, stackExtra);
    }
}

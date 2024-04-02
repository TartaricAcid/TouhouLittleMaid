package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.List;

public class MaidHomeMealTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 50;
    private @Nullable TileEntityPicnicMat tmpPicnicMat = null;

    public MaidHomeMealTask() {
        super(ImmutableMap.of());
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        if (!super.checkExtraStartConditions(serverLevel, maid)) {
            return false;
        }
        if (!maid.getFavorabilityManager().canAdd(Type.HOME_MEAL.getTypeName())) {
            return false;
        }
        if (maid.getVehicle() instanceof EntitySit sit && sit.getJoyType().equals(Type.ON_HOME_MEAL.getTypeName())) {
            BlockPos blockPos = sit.getAssociatedBlockPos();
            if (serverLevel.getBlockEntity(blockPos) instanceof TileEntityPicnicMat picnicMat) {
                this.tmpPicnicMat = picnicMat;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        if (tmpPicnicMat == null) {
            return;
        }
        int handConditionCount = 0;
        List<IMaidMeal> maidMeals = MaidMealManager.getMaidMeals(MaidMealType.HOME_MEAL);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = maid.getItemInHand(hand);
            if (!itemInHand.isEmpty()) {
                boolean switchItem = false;
                if (hand == InteractionHand.OFF_HAND) {
                    // 尝试把副手物品放背包
                    RangedWrapper backpackInv = maid.getAvailableBackpackInv();
                    for (int i = 0; i < backpackInv.getSlots(); i++) {
                        ItemStack stack = backpackInv.getStackInSlot(i);
                        if (stack.isEmpty()) {
                            backpackInv.setStackInSlot(i, itemInHand.copy());
                            maid.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                            switchItem = true;
                            break;
                        }
                    }
                }
                if (!switchItem) {
                    handConditionCount = handConditionCount + 1;
                    continue;
                }
            }
            // 先搜索所有的格子，检查一下能否吃，并记录 slot
            ItemStackHandler handler = this.tmpPicnicMat.getHandler();
            IntList candidateFood = new IntArrayList();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.isEmpty()) {
                    continue;
                }
                for (IMaidMeal maidMeal : maidMeals) {
                    if (maidMeal.canMaidEat(maid, stack, hand)) {
                        candidateFood.add(i);
                    }
                }
            }
            // 如果没搜索到，不执行后续吃的逻辑
            int size = candidateFood.size();
            if (size == 0) {
                ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.inner.home_meal.meal_is_empty");
                continue;
            }
            // 随机选择格子
            int skipCount = maid.getRandom().nextInt(size);
            candidateFood.intStream().skip(skipCount).findFirst().ifPresent(slotIndex -> {
                ItemStack outputStack = handler.extractItem(slotIndex, 1, false);
                this.tmpPicnicMat.refresh();
                maid.setItemInHand(hand, outputStack);
                ItemStack refreshItemInHand = maid.getItemInHand(hand);
                for (IMaidMeal maidMeal : maidMeals) {
                    if (maidMeal.canMaidEat(maid, refreshItemInHand, hand)) {
                        maidMeal.onMaidEat(maid, refreshItemInHand, hand);
                        return;
                    }
                }
            });
            return;
        }

        // 双手都是满的，聊天气泡提示
        if (handConditionCount >= 2) {
            ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.inner.home_meal.two_hand_is_full");
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.tmpPicnicMat = null;
    }
}

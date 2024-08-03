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
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

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

        List<IMaidMeal> maidMeals = MaidMealManager.getMaidMeals(MaidMealType.HOME_MEAL);

        // 对手部进行处理：如果没有空的手部，那就取副手
        InteractionHand eanHand = InteractionHand.OFF_HAND;
        for (InteractionHand hand : InteractionHand.values()) {
            if (maid.getItemInHand(hand).isEmpty()) {
                eanHand = hand;
                break;
            }
        }

        // 先对把手上的物品放入背包做预处理：如果放入背包后，手上还有剩余，那就不执行后续吃的逻辑并添加气泡提示
        ItemStack itemInHand = maid.getItemInHand(eanHand);
        IItemHandlerModifiable availableInv = maid.getAvailableBackpackInv();
        ItemStack leftoverStack = ItemHandlerHelper.insertItemStacked(availableInv, itemInHand.copy(), true);
        if (!leftoverStack.isEmpty()) {
            ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.inner.home_meal.two_hand_is_full");
            return;
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
                if (maidMeal.canMaidEat(maid, stack, eanHand)) {
                    candidateFood.add(i);
                }
            }
        }

        // 如果没搜索到，不执行后续吃的逻辑
        int size = candidateFood.size();
        if (size == 0) {
            ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.inner.home_meal.meal_is_empty");
            return;
        }

        // 随机选择格子
        int skipCount = maid.getRandom().nextInt(size);
        InteractionHand hand = eanHand;
        candidateFood.intStream().skip(skipCount).findFirst().ifPresent(slotIndex -> {
            ItemStack outputStack = handler.extractItem(slotIndex, 1, false);
            this.tmpPicnicMat.refresh();
            ItemHandlerHelper.insertItemStacked(availableInv, itemInHand.copy(), false);
            maid.setItemInHand(hand, outputStack);
            ItemStack refreshItemInHand = maid.getItemInHand(hand);
            for (IMaidMeal maidMeal : maidMeals) {
                if (maidMeal.canMaidEat(maid, refreshItemInHand, hand)) {
                    maidMeal.onMaidEat(maid, refreshItemInHand, hand);
                    return;
                }
            }
        });
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.tmpPicnicMat = null;
    }
}

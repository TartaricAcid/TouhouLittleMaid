package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.meal.IMaidMeal;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.meal.MaidMealManager;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class MaidHomeMealTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 50;
    private @Nullable TileEntityPicnicMat tmpPicnicMat = null;
    private int slotIndex = -1;

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
            if (!(serverLevel.getBlockEntity(blockPos) instanceof TileEntityPicnicMat picnicMat)) {
                return false;
            }
            ItemStackHandler handler = picnicMat.getHandler();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    this.tmpPicnicMat = picnicMat;
                    this.slotIndex = i;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        if (tmpPicnicMat == null || this.slotIndex < 0) {
            return;
        }
        List<IMaidMeal> maidMeals = MaidMealManager.getMaidMeals(MaidMealType.HOME_MEAL);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemInHand = maid.getItemInHand(hand);
            if (!itemInHand.isEmpty()) {
                // TODO 打印提示
                continue;
            }
            ItemStackHandler handler = this.tmpPicnicMat.getHandler();
            // TODO 随机取食物吃
            ItemStack outputStack = handler.extractItem(this.slotIndex, 1, false);
            this.tmpPicnicMat.refresh();
            maid.setItemInHand(hand, outputStack);
            itemInHand = maid.getItemInHand(hand);
            for (IMaidMeal maidMeal : maidMeals) {
                if (maidMeal.canMaidEat(maid, itemInHand, hand)) {
                    maidMeal.onMaidEat(maid, itemInHand, hand);
                    return;
                }
            }
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.tmpPicnicMat = null;
        this.slotIndex = -1;
    }
}

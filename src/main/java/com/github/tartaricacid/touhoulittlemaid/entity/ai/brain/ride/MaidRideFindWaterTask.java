package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ride;

import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.FishingTypeManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MaidRideFindWaterTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 100;

    private final int verticalSearchRange;
    private final int searchRange;

    protected int verticalSearchStart;
    private IFishingType fishingType = null;
    private BlockPos waterPos = null;

    public MaidRideFindWaterTask(int searchRange, int verticalSearchRange) {
        super(ImmutableMap.of());
        this.searchRange = searchRange;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner) && owner.fishing == null;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        ItemStack mainHandItem = maid.getMainHandItem();
        this.fishingType = FishingTypeManager.getFishingType(mainHandItem);

        if (this.fishingType.isFishingRod(mainHandItem)) {
            if (waterPos == null) {
                this.searchForDestination(worldIn, maid, mainHandItem);
                return;
            }
            if (!posCheck(maid)) {
                waterPos = null;
                return;
            }
            if (this.fishingType.suitableFishingHook(maid, worldIn, mainHandItem, waterPos)) {
                Vec3 centerPos = Vec3.atCenterOf(this.waterPos);

                MaidFishingHook fishingHook = this.fishingType.getFishingHook(maid, worldIn, mainHandItem, centerPos);
                worldIn.addFreshEntity(fishingHook);

                worldIn.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
                maid.swing(InteractionHand.MAIN_HAND);
                maid.getLookControl().setLookAt(centerPos);
            } else {
                waterPos = null;
            }
        }
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid, ItemStack rod) {
        BlockPos centrePos = maid.getBrainSearchPos();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            // FIXME: 应该从一片水域的正中央开始
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && this.fishingType.suitableFishingHook(maid, worldIn, rod, mutableBlockPos)) {
                            maid.getLookControl().setLookAt(mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ());
                            this.waterPos = mutableBlockPos;
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean posCheck(EntityMaid maid) {
        int distanceSqr = this.searchRange * this.searchRange;
        Vec3 waterVec = new Vec3(this.waterPos.getX(), this.waterPos.getY(), this.waterPos.getZ());
        return maid.distanceToSqr(waterVec) < distanceSqr && maid.isWithinRestriction(this.waterPos);
    }
}
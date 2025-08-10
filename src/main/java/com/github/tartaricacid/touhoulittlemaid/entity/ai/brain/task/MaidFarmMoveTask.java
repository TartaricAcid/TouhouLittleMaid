package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.IItemHandler;

import static com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask.VERTICAL_SEARCH_RANGE;

public class MaidFarmMoveTask extends MaidMoveToBlockTask {
    private static final int MAX_ITEMS = 8;
    private final NonNullList<ItemStack> seeds = NonNullList.create();
    private final IFarmTask task;
    private long chatBubbleKey = -1;

    public MaidFarmMoveTask(IFarmTask task, float movementSpeed) {
        super(movementSpeed, 2);
        this.task = task;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        // 如果周围掉落物过多，女仆将不执行收菜任务
        // 这是因为很多玩家在挂机时，女仆会无休止的收菜，导致掉落物过多
        // 而女仆又会疯狂检索周围掉落物，加剧了服务器的卡顿
        if (super.checkExtraStartConditions(worldIn, owner)) {
            // 如果掉落物超过 MAX_ITEMS 个，女仆将不执行收菜任务
            if (this.getItemEntityCount(worldIn, owner) < MAX_ITEMS) {
                return true;
            } else {
                this.chatBubbleKey = owner.getChatBubbleManager()
                        .addTextChatBubbleIfTimeout("chat_bubble.touhou_little_maid.inner.farm.too_many_item_entities", chatBubbleKey);
                return false;
            }
        }
        return false;
    }

    private int getItemEntityCount(ServerLevel worldIn, EntityMaid maid) {
        float radius = maid.getRestrictRadius();
        AABB aabb;
        if (maid.hasRestriction()) {
            aabb = new AABB(maid.getRestrictCenter()).inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        } else {
            aabb = maid.getBoundingBox().inflate(radius, VERTICAL_SEARCH_RANGE, radius);
        }
        return worldIn.getEntitiesOfClass(Entity.class, aabb, Entity::isAlive).size();
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        seeds.clear();
        IItemHandler inv = entityIn.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (task.isSeed(stack)) {
                seeds.add(stack);
            }
        }
        this.searchForDestination(worldIn, entityIn);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos basePos) {
        if (task.checkCropPosAbove()) {
            BlockPos above2Pos = basePos.above(2);
            BlockState stateUp2 = worldIn.getBlockState(above2Pos);
            if (!stateUp2.getCollisionShape(worldIn, above2Pos).isEmpty()) {
                return false;
            }
        }

        BlockPos cropPos = basePos.above();
        BlockState cropState = worldIn.getBlockState(cropPos);
        if (task.canHarvest(maid, cropPos, cropState)) {
            return true;
        }

        BlockState baseState = worldIn.getBlockState(basePos);
        return seeds.stream().anyMatch(seed -> task.canPlant(maid, basePos, baseState, seed));
    }
}

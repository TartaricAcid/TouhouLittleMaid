package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class MaidFindSitTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final float speedModifier;
    private Entity sitEntity = null;
    private long chatBubbleKey = -1;

    public MaidFindSitTask(float speedModifier) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = speedModifier;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner) && owner.getVehicle() == null;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        this.sitEntity = null;
        Entity sitTarget = maid.getBrain().getMemory(InitEntities.NEAREST_SIT_TARGET.get()).orElse(null);

        if (sitTarget != null) {
            // 找到坐具后清除旧的 no_sit 气泡，防止残留
            clearChatBubble(maid);
            this.sitEntity = sitTarget;
            BehaviorUtils.setWalkAndLookTargetMemories(maid, this.sitEntity, this.speedModifier, 0);
        } else {
            String langKey = "chat_bubble.touhou_little_maid.inner.fishing.no_sit";
            this.chatBubbleKey = maid.getChatBubbleManager().addTextChatBubbleIfTimeout(langKey, this.chatBubbleKey);
        }

        if (sitEntity != null && sitEntity.isAlive() && sitEntity.closerThan(maid, 2)) {
            if (sitEntity.getPassengers().isEmpty()) {
                maid.startRiding(this.sitEntity, true);
            }
            this.sitEntity = null;
        }
    }

    private void clearChatBubble(EntityMaid maid) {
        if (this.chatBubbleKey >= 0) {
            maid.getChatBubbleManager().removeChatBubble(this.chatBubbleKey);
            maid.getChatBubbleManager().forceUpdateChatBubble();
            this.chatBubbleKey = -1;
        }
    }
}

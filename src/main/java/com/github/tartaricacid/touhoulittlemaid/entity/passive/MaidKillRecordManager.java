package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Slime;

import javax.annotation.Nullable;

public final class MaidKillRecordManager {
    private static final String KILL_RECORD = "KillRecord";
    private static final String TOTAL_COUNT = "TotalCount";
    private static final String SLIME_COUNT = "Slime";
    private static final String WITHER_COUNT = "Wither";
    private static final String ENDER_DRAGON_COUNT = "EnderDragon";

    private int totalCount;
    private int slimeCount;
    private int witherCount;
    private int enderDragonCount;

    void addAdditionalSaveData(CompoundTag compound) {
        CompoundTag killRecord = new CompoundTag();
        killRecord.putInt(KILL_RECORD, totalCount);
        killRecord.putInt(SLIME_COUNT, slimeCount);
        killRecord.putInt(WITHER_COUNT, witherCount);
        killRecord.putInt(ENDER_DRAGON_COUNT, enderDragonCount);
        compound.put(KILL_RECORD, killRecord);
    }

    void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains(KILL_RECORD)) {
            CompoundTag killRecord = compound.getCompound(KILL_RECORD);
            totalCount = killRecord.getInt(TOTAL_COUNT);
            slimeCount = killRecord.getInt(SLIME_COUNT);
            witherCount = killRecord.getInt(WITHER_COUNT);
            enderDragonCount = killRecord.getInt(ENDER_DRAGON_COUNT);
        }
    }

    public void onTargetDeath(EntityMaid maid, LivingEntity target) {
        LivingEntity owner = maid.getOwner();
        this.totalCount++;
        triggerKill(owner, TriggerType.MAID_KILL_MOB);
        if (this.totalCount >= 100) {
            triggerKill(owner, TriggerType.KILL_100);
        }
        if (target instanceof Slime) {
            this.slimeCount++;
            if (slimeCount >= 300) {
                triggerKill(owner, TriggerType.KILL_SLIME_300);
            }
        }
        if (target instanceof WitherBoss) {
            this.witherCount++;
            triggerKill(owner, TriggerType.KILL_WITHER);
        }
        if (target instanceof EnderDragon) {
            this.enderDragonCount++;
            triggerKill(owner, TriggerType.KILL_DRAGON);
        }
    }

    private void triggerKill(@Nullable LivingEntity owner, String eventName) {
        if (owner instanceof ServerPlayer serverPlayer) {
            InitTrigger.MAID_EVENT.get().trigger(serverPlayer, eventName);
        }
    }
}

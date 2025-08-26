package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment.MAID_NUM;
import static com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment.POWER_NUM;

@EventBusSubscriber
public class EntityDeathEvent {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (source == null) {
            return;
        }
        Entity causingEntity = source.getEntity();
        if (causingEntity instanceof EntityMaid maid) {
            maid.getKillRecordManager().onTargetDeath(maid, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        Player newEntity = event.getEntity();
        Player oldEntity = event.getOriginal();
        boolean wasDeath = event.isWasDeath();
        boolean isKeep = newEntity.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);

        PowerAttachment power = oldEntity.getData(POWER_NUM);
        MaidNumAttachment maidNum = oldEntity.getData(MAID_NUM);
        if (wasDeath && !isKeep) {
            power.min(MiscConfig.PLAYER_DEATH_LOSS_POWER_POINT.get().floatValue());
        }
        newEntity.setData(POWER_NUM, power);
        newEntity.setData(MAID_NUM, maidNum);
    }
}

package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.goal.MaidTemptGoal;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncDataPackage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment.POWER_NUM;

@EventBusSubscriber
public class EntityJoinWorldEvent {
    @SubscribeEvent
    public static void onCreeperJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, EntityMaid.class, 6, 1, 1.2));
        }
    }

    @SubscribeEvent
    public static void onAnimalJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Animal animal) {
            animal.goalSelector.getAvailableGoals().stream().filter(goal -> goal.getGoal() instanceof TemptGoal).findFirst().ifPresent(g -> {
                if (g.getGoal() instanceof TemptGoal temptGoal) {
                    animal.goalSelector.addGoal(g.getPriority(), new MaidTemptGoal(temptGoal.mob, temptGoal.speedModifier, temptGoal.items, temptGoal.canScare));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PowerAttachment power = player.getData(POWER_NUM);
            MaidNumAttachment maidNum = player.getData(InitDataAttachment.MAID_NUM);
            PacketDistributor.sendToPlayer(player, new SyncDataPackage(power.get(), maidNum.get()));
        }
    }
}
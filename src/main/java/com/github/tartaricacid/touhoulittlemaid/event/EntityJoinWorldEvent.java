package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.goal.MaidTemptGoal;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
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
            // 先复制一遍进行遍历，避免出现 ConcurrentModificationException
            var goals = List.copyOf(animal.goalSelector.getAvailableGoals());
            goals.stream().filter(goal -> goal.getGoal() instanceof TemptGoal).findFirst().ifPresent(g -> {
                if (g.getGoal() instanceof TemptGoal temptGoal) {
                    MaidTemptGoal maidTemptGoal = new MaidTemptGoal(temptGoal.mob, temptGoal.speedModifier, temptGoal.items, temptGoal.canScare);
                    animal.goalSelector.addGoal(g.getPriority(), maidTemptGoal);
                }
            });
        }
    }
}
package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.goal.MaidTemptGoal;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityJoinWorldEvent {
    @SubscribeEvent
    public static void onCreeperJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, EntityMaid.class, 6, 1, 1.2));
        }
    }

    @SubscribeEvent
    public static void onAnimalJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Animal animal) {
            animal.goalSelector.getAvailableGoals().stream().filter(goal -> goal.getGoal() instanceof TemptGoal).findFirst().ifPresent(g -> {
                if (g.getGoal() instanceof TemptGoal temptGoal) {
                    animal.goalSelector.addGoal(g.getPriority(), new MaidTemptGoal(temptGoal.mob, temptGoal.speedModifier, temptGoal.items, temptGoal.canScare));
                }
            });
        }
    }
}
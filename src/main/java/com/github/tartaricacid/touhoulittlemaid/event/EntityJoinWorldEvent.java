package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.goal.MaidTemptGoal;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EntityJoinWorldEvent {
    @SubscribeEvent
    public static void onCreeperJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, EntityMaid.class, 6, 1, 1.2));
        }
    }

    @SubscribeEvent
    public static void onAnimalJoinWorld(net.minecraftforge.event.entity.EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AnimalEntity) {
            AnimalEntity animal = (AnimalEntity) event.getEntity();
            animal.goalSelector.availableGoals.stream().filter(goal -> goal.getGoal() instanceof TemptGoal).findFirst().ifPresent(g -> {
                if (g.getGoal() instanceof TemptGoal && MaidTemptGoal.checkNavigation(((TemptGoal) g.getGoal()).mob)) {
                    TemptGoal temptGoal = (TemptGoal) g.getGoal();
                    animal.goalSelector.addGoal(g.getPriority(), new MaidTemptGoal(temptGoal.mob, temptGoal.speedModifier, temptGoal.items, temptGoal.canScare));
                }
            });
        }
    }
}
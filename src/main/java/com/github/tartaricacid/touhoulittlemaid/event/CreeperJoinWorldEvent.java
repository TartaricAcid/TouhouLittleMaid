package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityScarecrow;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedHashSet;

/**
 * @author TartaricAcid
 */
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class CreeperJoinWorldEvent {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public static void onCreeperJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.getEntity();
            EntityAIAvoidEntity aiAvoidEntity = new EntityAIAvoidEntity(creeper, EntityScarecrow.class, 10.0F, 1.0D, 1.2D);
            // 把规避稻草人放在 AI 的第一个执行
            LinkedHashSet<EntityAITasks.EntityAITaskEntry> task = (LinkedHashSet) creeper.tasks.taskEntries;
            LinkedHashSet<EntityAITasks.EntityAITaskEntry> taskTmp = Sets.newLinkedHashSet(task);
            task.clear();
            task.add(creeper.tasks.new EntityAITaskEntry(1, aiAvoidEntity));
            task.addAll(taskTmp);
        }
    }
}

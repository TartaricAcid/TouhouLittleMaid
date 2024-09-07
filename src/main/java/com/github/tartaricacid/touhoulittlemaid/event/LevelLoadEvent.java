package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.DefaultMonsterType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class LevelLoadEvent {
    @SubscribeEvent
    public static void onLevelLoadEvent(LevelEvent.Load event) {
        LevelAccessor levelAccessor = event.getLevel();
        if (levelAccessor instanceof Level level) {
            DefaultMonsterType.initDefault(level);
        }
    }
}
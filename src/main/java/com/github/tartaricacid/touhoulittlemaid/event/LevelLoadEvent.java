package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.DefaultMonsterType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class LevelLoadEvent {
    @SubscribeEvent
    public static void onLevelLoadEvent(WorldEvent.Load event) {
        LevelAccessor levelAccessor = event.getWorld();
        if (levelAccessor instanceof Level level) {
            DefaultMonsterType.initDefault(level);
        }
    }
}
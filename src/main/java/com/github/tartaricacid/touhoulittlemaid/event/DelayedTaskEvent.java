package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.util.DelayedTask;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.function.BooleanSupplier;

/**
 * @author TartaricAcid
 * @date 2019/9/8 1:11
 **/

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class DelayedTaskEvent {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            DelayedTask.SUPPLIERS.removeIf(BooleanSupplier::getAsBoolean);
        }
    }
}


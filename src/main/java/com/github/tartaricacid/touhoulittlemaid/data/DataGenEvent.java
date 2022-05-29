package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvent {
    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(new MaidBlockStateProvider(event.getGenerator(), TouhouLittleMaid.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new AltarRecipeProvider(event.getGenerator()));
        event.getGenerator().addProvider(new MaidRecipeProvider(event.getGenerator()));
        event.getGenerator().addProvider(new LootModifierProvider(event.getGenerator()));
    }
}

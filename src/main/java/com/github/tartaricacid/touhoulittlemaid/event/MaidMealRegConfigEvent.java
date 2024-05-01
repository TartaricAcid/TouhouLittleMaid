package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.google.common.collect.Lists;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidMealRegConfigEvent {
    private static final String CONFIG_NAME = TouhouLittleMaid.MOD_ID + "-common.toml";

    public static List<String> HEAL_MEAL_REGEX = Lists.newArrayList();
    public static List<String> HOME_MEAL_REGEX = Lists.newArrayList();
    public static List<String> WORK_MEAL_REGEX = Lists.newArrayList();

    @SubscribeEvent
    public static void onEvent(ModConfigEvent.Loading event) {
        String fileName = event.getConfig().getFileName();
        if (CONFIG_NAME.equals(fileName)) {
            handleConfig(MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST_REGEX.get(), HEAL_MEAL_REGEX);
            handleConfig(MaidConfig.MAID_HOME_MEALS_BLOCK_LIST_REGEX.get(), HOME_MEAL_REGEX);
            handleConfig(MaidConfig.MAID_WORK_MEALS_BLOCK_LIST_REGEX.get(), WORK_MEAL_REGEX);
        }
    }

    public static void handleConfig(List<String> config, List<String> output) {
        output.clear();
        output.addAll(getMatchedList(config));
    }

    private static @NotNull List<String> getMatchedList(List<String> matchList) {
        List<String> list = Lists.newArrayList();
        for (String match : matchList) {
            Pattern pattern = Pattern.compile(match);
            ForgeRegistries.ITEMS.getKeys().stream().filter(id -> pattern.matcher(id.toString()).matches()).forEach(e -> list.add(e.toString()));
        }
        return list;
    }
}

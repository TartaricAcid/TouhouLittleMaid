package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidEatenRetConEvent {
    private static final String CONFIG_NAME = TouhouLittleMaid.MOD_ID + "-common.toml";

    public static Map<Class<?>, ItemStack> EATEN_RETURN_MAP = new LinkedHashMap<>();

    @SubscribeEvent
    public static void onEvent(ModConfigEvent.Loading event) {
        String fileName = event.getConfig().getFileName();
        if (CONFIG_NAME.equals(fileName)) {
            handleConfig(MaidConfig.MAID_EATEN_CLASS_LIST.get(), MaidConfig.MAID_EATEN_RETURN_CONTAINER_LIST.get(), EATEN_RETURN_MAP);
        }
    }

    public static void handleConfig(List<String> classList, List<String> itemList, Map<Class<?>, ItemStack> classItemStackHashMap) {
        classItemStackHashMap.clear();
        if (classList.size() != itemList.size()) {
            classItemStackHashMap.putAll(MaidConfig.getDefaultRetCon());

            MaidConfig.getCompatRetConMap().forEach((className, itemId) -> {
                try {
                    classItemStackHashMap.put(Class.forName(className), getItemStack(itemId));
                } catch (ClassNotFoundException e) {
                    TouhouLittleMaid.LOGGER.error(e);
                }
            });

            TouhouLittleMaid.LOGGER.error("ClassList and ItemList size is not same, so set default RetCon");
            TouhouLittleMaid.LOGGER.error("classList.size: {} {}", classList.size(), classList);
            TouhouLittleMaid.LOGGER.error("itemList.size: {} {}", itemList.size(), itemList);
            return;
        }

        for (int i = 0; i < classList.size(); i++) {
            try {
                Class<?> clazz = Class.forName(classList.get(i));
                String itemId = itemList.get(i);
                ItemStack itemStack = getItemStack(itemId);
                Preconditions.checkNotNull(itemStack);
                classItemStackHashMap.put(clazz, itemStack);
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error(e);
            }
        }
    }


    @Nullable
    private static ItemStack getItemStack(String itemId) {
        ResourceLocation location;
        if (itemId.contains(":")) {
            location = new ResourceLocation(itemId.split(":")[0], itemId.split(":")[1]);
        } else {
            location = new ResourceLocation(itemId);
        }
        Item value = ForgeRegistries.ITEMS.getValue(location);
        if (value != null) {
            return new ItemStack(value);
        }
        return null;
    }
}
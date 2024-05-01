package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
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
            classItemStackHashMap.putAll(getDefaultRetCon());

            getCompatRetConMap().forEach((className, itemId) -> {
                try {
                    Class<?> aClass = Class.forName(className);
                    ItemStack itemStack = getItemStack(itemId);
                    classItemStackHashMap.put(aClass, itemStack);
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
                classItemStackHashMap.put(clazz, itemStack);
            } catch (Exception e) {
                TouhouLittleMaid.LOGGER.error(e);
            }
        }
    }

    public static Map<Class<?>, ItemStack> getDefaultRetCon() {
        Map<Class<?>, ItemStack> classItemStackLinkedHashMap = new LinkedHashMap<>();
        classItemStackLinkedHashMap.put(BowlFoodItem.class, Items.BOWL.getDefaultInstance());
        classItemStackLinkedHashMap.put(HoneyBottleItem.class, Items.GLASS_BOTTLE.getDefaultInstance());
        return classItemStackLinkedHashMap;
    }

    public static Map<String, String> getCompatRetConMap() {
        Map<String, String> stringStringLinkedHashMap = new LinkedHashMap<>();
        // 注意：子类一定要在前面
        // 农夫乐事系列
        if (ModList.get().isLoaded("miners_delight")) {
            stringStringLinkedHashMap.put("com.sammy.minersdelight.content.item.CopperCupFoodItem", "miners_delight:copper_cup");
        }
        if (ModList.get().isLoaded("farmersdelight")) {
            stringStringLinkedHashMap.put("vectorwing.farmersdelight.common.item.DrinkableItem", getItemId(Items.GLASS_BOTTLE));
            stringStringLinkedHashMap.put("vectorwing.farmersdelight.common.item.ConsumableItem", getItemId(Items.BOWL));
        }

        // 其他

        return stringStringLinkedHashMap;
    }

    public static Pair<List<String>, List<String>> getEatenReturnInfo() {
        List<String> classList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        getDefaultRetCon().forEach((class1, itemStack) -> {
            classList.add(class1.getName());
            stringList.add(getItemId(itemStack.getItem()));
        });

        getCompatRetConMap().forEach((className, itemId) -> {
            classList.add(className);
            stringList.add(itemId);
        });

        return new Pair<>(classList, stringList);
    }

    public static String getItemId(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        Preconditions.checkNotNull(key);
        return key.toString();
    }


    public static ItemStack getItemStack(String itemId) {
        ResourceLocation resourceLocation = new ResourceLocation(itemId);
        Item value = ForgeRegistries.ITEMS.getValue(resourceLocation);
        Preconditions.checkNotNull(value);
        return new ItemStack(value);
    }
}
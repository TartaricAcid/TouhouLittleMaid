package com.github.tartaricacid.touhoulittlemaid.api.task.meal;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public interface IMaidMeal {
    ArrayList<ResourceLocation> HEAL_MEAL_MATCH = getMatchedList(MaidConfig.MAID_HEAL_MEALS_BLOCK_LIST_MATCH.get());
    ArrayList<ResourceLocation> HOME_MEAL_MATCH = getMatchedList(MaidConfig.MAID_HOME_MEALS_BLOCK_LIST_MATCH.get());
    ArrayList<ResourceLocation> WORK_MEAL_MATCH = getMatchedList(MaidConfig.MAID_WORK_MEALS_BLOCK_LIST_MATCH.get());

    static boolean isBlockList(ItemStack food, List<String> blockList) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(food.getItem());
        return key == null || blockList.contains(key.toString());
    }

    static boolean isBlockListMatch(ItemStack food,ArrayList<ResourceLocation> itemList) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(food.getItem());
        return itemList.contains(key);
    }

    /**
     * 女仆能否吃下这个物品
     */
    boolean canMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand);

    /**
     * 女仆吃物品时的行为
     */
    void onMaidEat(EntityMaid maid, ItemStack stack, InteractionHand hand);

    private static @NotNull ArrayList<ResourceLocation> getMatchedList(List<String> matchList) {
        ArrayList<ResourceLocation> list = new ArrayList<>();
        for(String match : matchList) {
        Pattern pattern = Pattern.compile(match);
        HolderLookup.RegistryLookup<Item>  items = BuiltInRegistries.ITEM.asLookup();
        items.listElementIds()
                .filter(itemResourceKey -> {
                    var key = itemResourceKey.toString();
                    return pattern.matcher(key).matches();
                })
                .map(ResourceKey::location)
                .forEach(list::add);
        }
        return list;
    }
}

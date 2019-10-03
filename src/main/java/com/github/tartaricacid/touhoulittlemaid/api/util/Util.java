package com.github.tartaricacid.touhoulittlemaid.api.util;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.stream.Collectors;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public final class Util {
    private Util() {
    }

    /**
     * 用来播放基于环境的音效，比如气温，天气，时间
     *
     * @param fallback    这些都没有播放情况下的默认音效
     * @param probability 播放环境音效的概率
     * @return 应当触发的音效
     */
    public static SoundEvent environmentSound(AbstractEntityMaid maid, SoundEvent fallback, float probability) {
        // FIXME: 2019/7/26 这里调用了非 api 包的方法，看起来需要剔除或者转移位置
        World world = maid.world;
        Random rand = maid.getRNG();
        // 差不多早上 6:30 - 7:30
        if (500 < world.getWorldTime() && world.getWorldTime() < 1500 && rand.nextFloat() < probability) {
            return MaidSoundEvent.MAID_MORNING;
        }
        // 差不多下午 6:30 - 7:30
        if (12500 < world.getWorldTime() && world.getWorldTime() < 13500 && rand.nextFloat() < probability) {
            return MaidSoundEvent.MAID_NIGHT;
        }
        Biome biome = world.getBiome(maid.getPosition());
        if (world.isRaining() && biome.canRain() && rand.nextFloat() < probability) {
            return MaidSoundEvent.MAID_RAIN;
        }
        if (world.isRaining() && biome.isSnowyBiome() && rand.nextFloat() < probability) {
            return MaidSoundEvent.MAID_SNOW;
        }
        if (biome.getTempCategory() == Biome.TempCategory.COLD && rand.nextFloat() < probability) {
            return MaidSoundEvent.MAID_COLD;
        }
        if (biome.getTempCategory() == Biome.TempCategory.WARM && rand.nextFloat() < probability) {
            return MaidSoundEvent.MAID_HOT;
        }
        return fallback;
    }
    
    public static boolean doesItemHaveOreName(ItemStack stack, String ore)
    {
        if (stack.isEmpty() || !OreDictionary.doesOreNameExist(ore))
        {
            return false;
        }
        int oreid = OreDictionary.getOreID(ore);
        for (int id : OreDictionary.getOreIDs(stack))
        {
            if (oreid == id)
            {
                return true;
            }
        }
        return false;
    }
    
    public static NonNullList<ItemDefinition> getItemsFromOre(String ore)
    {
        LinkedHashSet<ItemDefinition> set = new LinkedHashSet<>();
        if (!ore.isEmpty())
        {
            for (ItemStack item : OreDictionary.getOres(ore, false))
            {
                if (item.getMetadata() == OreDictionary.WILDCARD_VALUE)
                {
                    NonNullList<ItemStack> subItems = NonNullList.create();
                    item.getItem().getSubItems(item.getItem().getCreativeTab(), subItems);
                    set.addAll(subItems.stream().map(ItemDefinition::of).collect(Collectors.toList()));
                }
                else
                {
                    set.add(ItemDefinition.of(item));
                }
            }
        }
        return NonNullList.from(ItemDefinition.EMPTY, set.toArray(new ItemDefinition[0]));
    }
    
    public static NonNullList<ItemStack> getItemsFromOre(String ore, int count)
    {
        return NonNullList.from(ItemStack.EMPTY, getItemsFromOre(ore).stream().map(ItemDefinition::getItemStack).peek(i -> i.setCount(count)).toArray(ItemStack[]::new));
    }
}

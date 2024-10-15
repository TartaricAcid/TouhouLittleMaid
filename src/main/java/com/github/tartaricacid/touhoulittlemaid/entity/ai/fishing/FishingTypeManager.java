package com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing;

import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.AquacultureCompat;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class FishingTypeManager {
    private static final List<IFishingType> FISHING_TYPES = Lists.newArrayList();
    private static final DefaultFishingType DEFAULT_FISHING_TYPE = new DefaultFishingType();

    public void addFishingType(IFishingType fishingType) {
        FISHING_TYPES.add(fishingType);
    }

    public static void init() {
        FishingTypeManager manager = new FishingTypeManager();
        AquacultureCompat.registerFishingType(manager);
    }

    public static IFishingType getFishingType(ItemStack itemStack) {
        for (IFishingType fishingType : FISHING_TYPES) {
            if (fishingType.isFishingRod(itemStack)) {
                return fishingType;
            }
        }
        return DEFAULT_FISHING_TYPE;
    }
}
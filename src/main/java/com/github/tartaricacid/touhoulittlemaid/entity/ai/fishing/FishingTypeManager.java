package com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing;

import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.AquacultureCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    public static MaidFishingHook getFishingTypes(EntityMaid maid, Level level, ItemStack itemStack, Vec3 pos) {
        for (IFishingType fishingType : FISHING_TYPES) {
            if (fishingType.isFishingRod(itemStack)) {
                return fishingType.getFishingHook(maid, level, itemStack, pos);
            }
        }
        return DEFAULT_FISHING_TYPE.getFishingHook(maid, level, itemStack, pos);
    }
}
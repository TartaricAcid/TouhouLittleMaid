package com.github.tartaricacid.touhoulittlemaid.compat.extracontainer;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios.ExtraContainerEquipHandler;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios.ExtraContainerPickupHandler;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.curios.ExtraContainerRequestHandler;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ExtraContainerManager {
    private static final List<BackpackProvider> PROVIDERS = Lists.newArrayList();
    private static boolean CURIOS_HANDLERS_REGISTERED = false;

    private static final int DEFAULT_PRIORITY = 100;
    private static final Map<String, Integer> SLOT_PRIORITY = Map.of("back", 0, "trinkets", 1);

    public static void register(BackpackProvider provider) {
        PROVIDERS.add(provider);
        if (!CURIOS_HANDLERS_REGISTERED && CuriosCompat.isLoadedOrEnable()) {
            MinecraftForge.EVENT_BUS.register(new ExtraContainerEquipHandler());
            MinecraftForge.EVENT_BUS.register(new ExtraContainerPickupHandler());
            MinecraftForge.EVENT_BUS.register(new ExtraContainerRequestHandler());
            CURIOS_HANDLERS_REGISTERED = true;
        }
    }

    public static boolean isAnyBackpack(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (BackpackProvider provider : PROVIDERS) {
            if (provider.isBackpack(stack)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static ContainerRef tryCreateSlotRef(ItemStack stack, String slotType, int slotIndex) {
        if (stack.isEmpty()) {
            return null;
        }
        for (BackpackProvider provider : PROVIDERS) {
            if (provider.isBackpack(stack)) {
                return provider.createSlotRef(slotType, slotIndex);
            }
        }
        return null;
    }

    public static int getSlotPriority(String slotType) {
        return SLOT_PRIORITY.getOrDefault(slotType, DEFAULT_PRIORITY);
    }
}

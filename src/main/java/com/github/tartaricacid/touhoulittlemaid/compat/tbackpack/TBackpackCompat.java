package com.github.tartaricacid.touhoulittlemaid.compat.tbackpack;

import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.BackpackProvider;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.ContainerRef;
import com.github.tartaricacid.touhoulittlemaid.compat.extracontainer.ExtraContainerManager;
import com.github.tartaricacid.touhoulittlemaid.compat.tbackpack.curios.TBackpackSlotRef;
import net.minecraft.world.item.ItemStack;

public class TBackpackCompat {
    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = true;
        ExtraContainerManager.register(new TBackpackProvider());
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static boolean isBackpack(ItemStack stack) {
        if (isLoaded()) {
            return TBackpackCompatInner.isBackpack(stack);
        }
        return false;
    }

    private static class TBackpackProvider implements BackpackProvider {
        @Override
        public boolean isBackpack(ItemStack stack) {
            return TBackpackCompatInner.isBackpack(stack);
        }

        @Override
        public ContainerRef createSlotRef(String slotType, int slotIndex) {
            return new TBackpackSlotRef(slotType, slotIndex);
        }
    }
}

package com.github.tartaricacid.touhoulittlemaid.compat.slashblade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class SlashBladeCompat {
    private static final String SLASH_BLADE = "slashblade";
    private static final ResourceLocation SLASH_BLADE_ID = new ResourceLocation(SLASH_BLADE, "slashblade");

    public static boolean isSlashBladeLoaded() {
        return ModList.get().isLoaded(SLASH_BLADE);
    }

    public static boolean isSlashBladeItem(ItemStack stack) {
        return isSlashBladeLoaded() && SLASH_BLADE_ID.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()));
    }
}

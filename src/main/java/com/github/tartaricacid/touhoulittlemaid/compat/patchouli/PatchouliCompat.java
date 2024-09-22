package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PatchouliCompat {
    public static void init() {
        MultiblockRegistry.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new OpenDefaultBook());
        }
    }
}
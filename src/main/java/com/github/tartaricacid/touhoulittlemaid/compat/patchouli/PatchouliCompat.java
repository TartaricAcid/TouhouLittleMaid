package com.github.tartaricacid.touhoulittlemaid.compat.patchouli;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

public class PatchouliCompat {
    public static void init() {
        MultiblockRegistry.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(new OpenDefaultBook());
        }
    }
}
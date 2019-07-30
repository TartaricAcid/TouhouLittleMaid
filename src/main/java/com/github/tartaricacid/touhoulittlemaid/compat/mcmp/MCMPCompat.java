package com.github.tartaricacid.touhoulittlemaid.compat.mcmp;

import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;

import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.addon.MCMPAddon;
import mcmultipart.api.multipart.IMultipartRegistry;

@MCMPAddon
public class MCMPCompat implements IMCMPAddon {
    @Override
    public void registerParts(IMultipartRegistry registry) {
        registry.registerPartWrapper(MaidBlocks.GRID, new PartGrid());
        registry.registerStackWrapper(MaidBlocks.GRID);
    }
}

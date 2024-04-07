package com.github.tartaricacid.touhoulittlemaid.compat.jade;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.compat.jade.provider.MaidProvider;
import com.github.tartaricacid.touhoulittlemaid.compat.jade.provider.ShrineLampProvider;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcp.mobius.waila.api.IWailaClientRegistration;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    @SuppressWarnings("all")
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerComponentProvider(ShrineLampProvider.INSTANCE, TooltipPosition.TAIL, BlockMaidBeacon.class);
        registration.registerComponentProvider(MaidProvider.INSTANCE, TooltipPosition.TAIL, EntityMaid.class);
    }
}

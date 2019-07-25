package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

/**
 * HWYLA 模组的兼容部分
 *
 * @author TartaricAcid
 * @date 2019/7/25 18:16
 **/
@WailaPlugin(TouhouLittleMaid.MOD_ID)
public class WailaInfo implements IWailaPlugin {
    public WailaInfo() {
    }

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new EntityMaidProvider(), EntityMaid.class);
    }
}

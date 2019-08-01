package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

/**
 * @author TartaricAcid
 * @date 2019/7/25 18:16
 **/
@WailaPlugin(TouhouLittleMaid.MOD_ID)
public class WailaInfo implements IWailaPlugin {
    /**
     * 按照接口说明，必须存在一个默认的构造器
     */
    public WailaInfo() {
    }

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new EntityMaidProvider(), EntityMaid.class);
        registrar.registerBodyProvider(new TileEntityGridProvider(), BlockGrid.class);
    }
}

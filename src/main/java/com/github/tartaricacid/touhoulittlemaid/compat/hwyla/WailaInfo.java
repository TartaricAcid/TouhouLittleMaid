package com.github.tartaricacid.touhoulittlemaid.compat.hwyla;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockAltar;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
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
        registrar.registerBodyProvider(new MaidProvider(), EntityMaid.class);
        registrar.registerBodyProvider(TrolleyAudioProvider.INSTANCE, EntityTrolleyAudio.class);
        registrar.registerNBTProvider(TrolleyAudioProvider.INSTANCE, EntityTrolleyAudio.class);
        registrar.registerBodyProvider(new GridProvider(), BlockGrid.class);
        registrar.registerStackProvider(new AltarProvider(), BlockAltar.class);
        // FIXME: 2020/2/3 还是存在无法注册信息的问题
        registrar.registerBodyProvider(TombstoneProvider.INSTANCE, BlockTombstone.class);
        registrar.registerNBTProvider(TombstoneProvider.INSTANCE, BlockTombstone.class);
    }
}

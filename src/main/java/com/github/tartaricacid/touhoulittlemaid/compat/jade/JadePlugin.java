package com.github.tartaricacid.touhoulittlemaid.compat.jade;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.compat.jade.provider.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityPicnicMat;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(ShrineLampProvider.INSTANCE, BlockMaidBeacon.class);

        registration.registerEntityComponent(MaidProvider.INSTANCE, EntityMaid.class);

        registration.registerItemStorageClient(TombstoneProvider.INSTANCE);
        registration.registerItemStorageClient(AltarProvider.INSTANCE);
        registration.registerItemStorageClient(PicnicMatProvider.INSTANCE);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerItemStorage(TombstoneProvider.INSTANCE, EntityTombstone.class);
        registration.registerItemStorage(AltarProvider.INSTANCE, TileEntityAltar.class);
        registration.registerItemStorage(PicnicMatProvider.INSTANCE, TileEntityPicnicMat.class);
    }
}

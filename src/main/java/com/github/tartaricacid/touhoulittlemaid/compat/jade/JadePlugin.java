package com.github.tartaricacid.touhoulittlemaid.compat.jade;

import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.compat.jade.provider.*;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTombstone;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
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

        registration.registerItemStorageClient(BroomProvider.INSTANCE);
        registration.registerItemStorageClient(TombstoneProvider.INSTANCE);
        registration.registerItemStorageClient(AltarProvider.INSTANCE);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerItemStorage(BroomProvider.INSTANCE, EntityBroom.class);
        registration.registerItemStorage(TombstoneProvider.INSTANCE, EntityTombstone.class);
        registration.registerItemStorage(AltarProvider.INSTANCE, TileEntityAltar.class);
    }
}

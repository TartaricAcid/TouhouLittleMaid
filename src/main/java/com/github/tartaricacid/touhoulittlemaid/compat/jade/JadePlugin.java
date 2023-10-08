package com.github.tartaricacid.touhoulittlemaid.compat.jade;

import com.github.tartaricacid.touhoulittlemaid.compat.jade.provider.MaidProvider;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(new MaidProvider(), EntityMaid.class);
    }
}

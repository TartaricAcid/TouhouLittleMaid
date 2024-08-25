package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

@OnlyIn(Dist.CLIENT)
public class RenderMaidEvent extends Event implements ICancellableEvent {
    private final IMaid maid;
    private final MaidModels.ModelData modelData;

    public RenderMaidEvent(IMaid maid, MaidModels.ModelData modelData) {
        this.maid = maid;
        this.modelData = modelData;
    }

    public IMaid getMaid() {
        return maid;
    }

    public MaidModels.ModelData getModelData() {
        return modelData;
    }
}

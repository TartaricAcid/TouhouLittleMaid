package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
@OnlyIn(Dist.CLIENT)
public class RenderMaidEvent extends Event {
    private final EntityMaidRenderable maid;
    private final MaidModels.ModelData modelData;

    public RenderMaidEvent(EntityMaidRenderable maid, MaidModels.ModelData modelData) {
        this.maid = maid;
        this.modelData = modelData;
    }

    public EntityMaidRenderable getMaid() {
        return maid;
    }

    public MaidModels.ModelData getModelData() {
        return modelData;
    }
}

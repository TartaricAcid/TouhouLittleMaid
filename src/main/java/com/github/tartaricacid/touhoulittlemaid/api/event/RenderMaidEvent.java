package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.client.resources.ModelData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Cancelable
@SideOnly(Side.CLIENT)
public class RenderMaidEvent extends Event {
    private final EntityMaid maid;
    private ModelData modelData;

    public RenderMaidEvent(EntityMaid maid, ModelData modelData) {
        this.maid = maid;
        this.modelData = modelData;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public ModelData getModelData() {
        return modelData;
    }
}

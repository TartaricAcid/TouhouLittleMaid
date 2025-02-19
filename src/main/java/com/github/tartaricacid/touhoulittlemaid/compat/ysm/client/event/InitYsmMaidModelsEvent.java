package com.github.tartaricacid.touhoulittlemaid.compat.ysm.client.event;

import com.github.tartaricacid.touhoulittlemaid.compat.ysm.data.YsmMaidBaseInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class InitYsmMaidModelsEvent extends Event {
    private List<YsmMaidBaseInfo> ysmModels = Collections.emptyList();

    public List<YsmMaidBaseInfo> getYsmModels() {
        return ysmModels;
    }

    public void setYsmModels(List<YsmMaidBaseInfo> ysmModels) {
        this.ysmModels = ysmModels;
    }
}

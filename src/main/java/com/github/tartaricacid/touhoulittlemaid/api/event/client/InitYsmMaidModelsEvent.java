package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.MaidModelGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class InitYsmMaidModelsEvent extends Event {
    private List<MaidModelGui.YsmMaidBaseInfo> ysmModels = Collections.emptyList();

    public List<MaidModelGui.YsmMaidBaseInfo> getYsmModels() {
        return ysmModels;
    }

    public void setYsmModels(List<MaidModelGui.YsmMaidBaseInfo> ysmModels) {
        this.ysmModels = ysmModels;
    }
}

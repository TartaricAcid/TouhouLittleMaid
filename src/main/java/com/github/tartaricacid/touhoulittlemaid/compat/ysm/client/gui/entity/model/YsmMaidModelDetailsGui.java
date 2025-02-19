package com.github.tartaricacid.touhoulittlemaid.compat.ysm.client.gui.entity.model;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.detail.MaidModelDetailsGui;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public class YsmMaidModelDetailsGui extends MaidModelDetailsGui {
    public YsmMaidModelDetailsGui(EntityMaid sourceEntity, MaidModelInfo modelInfo, String modeId, String texture) {
        super(sourceEntity, modelInfo);
        this.guiEntity.setIsYsmModel(true);
        this.guiEntity.setYsmModel(modeId, texture);
    }
}

package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;

import java.io.InputStream;

public class BroomModel extends SimpleBedrockModel<EntityBroom> {
    private final BedrockPart all;

    public BroomModel(InputStream stream) {
        super(stream);
        this.all = this.getPart("all");
    }

    @Override
    public void setupAnim(EntityBroom broom, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        all.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (broom.isVehicle()) {
            all.xRot = headPitch * ((float) Math.PI / 180F) / 10;
        }
    }
}

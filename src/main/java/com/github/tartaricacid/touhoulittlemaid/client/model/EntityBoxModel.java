package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBox;
import net.minecraft.util.Mth;

import java.io.InputStream;

public class EntityBoxModel extends SimpleBedrockModel<EntityBox> {
    private final BedrockPart x1;
    private final BedrockPart x2;
    private final BedrockPart z1;
    private final BedrockPart z2;
    private final BedrockPart top;

    public EntityBoxModel(InputStream stream) {
        super(stream);
        this.x1 = this.getPart("x1");
        this.x2 = this.getPart("x2");
        this.z1 = this.getPart("z1");
        this.z2 = this.getPart("z2");
        this.top = this.getPart("top");
    }

    @Override
    public void setupAnim(EntityBox entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        int stage = entityIn.getOpenStage();
        if (stage == EntityBox.FIRST_STAGE) {
            top.visible = true;
            x1.xRot = 0;
            x2.xRot = 0;
            z1.zRot = 0;
            z2.zRot = 0;
        } else if (stage == EntityBox.SECOND_STAGE) {
            top.visible = false;
            x1.xRot = 0;
            x2.xRot = 0;
            z1.zRot = 0;
            z2.zRot = 0;
        } else {
            long timeStamp = System.currentTimeMillis() - entityIn.thirdStageTimeStamp;
            float progress = Mth.clamp((float) timeStamp / 50f, 0.0f, 60f);
            top.visible = false;
            x1.xRot = 0.023998277f * progress;
            x2.xRot = -0.023998277f * progress;
            z1.zRot = 0.023998277f * progress;
            z2.zRot = -0.023998277f * progress;
        }
    }
}
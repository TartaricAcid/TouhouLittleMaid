package com.github.tartaricacid.touhoulittlemaid.client.model;


import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.SimpleBedrockModel;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.util.Mth;

import java.io.InputStream;

public class NewEntityFairyModel extends SimpleBedrockModel<EntityFairy> {
    private final BedrockPart head;
    private final BedrockPart blink;
    private final BedrockPart armLeft;
    private final BedrockPart legLeft;
    private final BedrockPart legRight;
    private final BedrockPart wingLeft;
    private final BedrockPart wingRight;
    private final BedrockPart armRight;

    public NewEntityFairyModel(InputStream stream) {
        super(stream);
        this.head = this.getPart("head");
        this.blink = this.getPart("blink");
        this.armLeft = this.getPart("armLeft");
        this.legLeft = this.getPart("legLeft");
        this.legRight = this.getPart("legRight");
        this.wingLeft = this.getPart("wingLeft");
        this.wingRight = this.getPart("wingRight");
        this.armRight = this.getPart("armRight");
    }

    @Override
    public void setupAnim(EntityFairy entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.xRot = headPitch * 0.017453292F;
        head.yRot = netHeadYaw * 0.017453292F;
        armLeft.zRot = Mth.cos(ageInTicks * 0.05f) * 0.05f - 0.4f;
        armRight.zRot = -Mth.cos(ageInTicks * 0.05f) * 0.05f + 0.4f;
        if (entityIn.onGround()) {
            legLeft.xRot = Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            legRight.xRot = -Mth.cos(limbSwing * 0.67f) * 0.3f * limbSwingAmount;
            armLeft.xRot = -Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            armRight.xRot = Mth.cos(limbSwing * 0.67f) * 0.7F * limbSwingAmount;
            wingLeft.yRot = -Mth.cos(ageInTicks * 0.3f) * 0.2f + 1.0f;
            wingRight.yRot = Mth.cos(ageInTicks * 0.3f) * 0.2f - 1.0f;
        } else {
            legLeft.xRot = 0f;
            legRight.xRot = 0f;
            armLeft.xRot = -0.17453292F;
            armRight.xRot = -0.17453292F;
            head.xRot = head.xRot - 8 * 0.017453292F;
            wingLeft.yRot = -Mth.cos(ageInTicks * 0.5f) * 0.4f + 1.2f;
            wingRight.yRot = Mth.cos(ageInTicks * 0.5f) * 0.4f - 1.2f;
        }
        float remainder = ageInTicks % 60;
        // 0-10 显示眨眼贴图
        blink.visible = (55 < remainder && remainder < 60);
    }
}
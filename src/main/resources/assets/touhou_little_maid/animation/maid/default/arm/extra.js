Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        armLeftExtraA = modelMap.get("armLeftExtraA");
        armRightExtraA = modelMap.get("armRightExtraA");

        f1 = 1.0 - Math.pow(1.0 - maid.getSwingProgress(), 4);
        f2 = Math.sin(f1 * Math.PI);
        f3 = Math.sin(maid.getSwingProgress() * Math.PI) * -0.7 * 0.75;

        if (armLeftExtraA != undefined) {
            if (maid.isHoldTrolley()) {
                armLeftExtraA.setRotateAngleX(0.5);
                armLeftExtraA.setRotateAngleY(0);
                armLeftExtraA.setRotateAngleZ(-0.395);
            } else if (maid.isHoldVehicle()) {
                rotation = maid.getLeftHandRotation();
                armLeftExtraA.setRotateAngleX(rotation[0]);
                armLeftExtraA.setRotateAngleY(rotation[1]);
                armLeftExtraA.setRotateAngleZ(rotation[2]);
            } else {
                armLeftExtraA.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount);
                armLeftExtraA.setRotateAngleY(0);
                armLeftExtraA.setRotateAngleZ(Math.cos(ageInTicks * 0.05) * 0.05 - 0.4);
                // 手部使用动画
                if (maid.getSwingProgress() > 0.0 && maid.isSwingLeftHand()) {
                    armLeftExtraA.setRotateAngleX(armLeftExtraA.getRotateAngleX() - (f2 * 1.2 + f3));
                    armLeftExtraA.setRotateAngleZ(armLeftExtraA.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
                }
            }
        }

        if (armRightExtraA != undefined) {
            if (maid.isHoldVehicle()) {
                rotation = maid.getRightHandRotation();
                armRightExtraA.setRotateAngleX(rotation[0]);
                armRightExtraA.setRotateAngleY(rotation[1]);
                armRightExtraA.setRotateAngleZ(rotation[2]);
            } else {
                armRightExtraA.setRotateAngleX(Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount);
                armRightExtraA.setRotateAngleY(0);
                armRightExtraA.setRotateAngleZ(-Math.cos(ageInTicks * 0.05) * 0.05 + 0.4);
                // 手部使用动画
                if (maid.getSwingProgress() > 0.0 && !maid.isSwingLeftHand()) {
                    armRightExtraA.setRotateAngleX(armRightExtraA.getRotateAngleX() - (f2 * 1.2 + f3));
                    armRightExtraA.setRotateAngleZ(armRightExtraA.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
                }
            }
        }
    }
})
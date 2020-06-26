Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        armLeft = modelMap.get("armLeft");
        armRight = modelMap.get("armRight");

        f1 = 1.0 - Math.pow(1.0 - maid.getSwingProgress(), 4);
        f2 = Math.sin(f1 * Math.PI);
        f3 = Math.sin(maid.getSwingProgress() * Math.PI) * -0.7 * 0.75;

        if (armLeft != undefined) {
            if (maid.isHoldTrolley()) {
                armLeft.setRotateAngleX(0.5);
                armLeft.setRotateAngleY(0);
                armLeft.setRotateAngleZ(-0.395);
            } else if (maid.isHoldVehicle()) {
                rotation = maid.getLeftHandRotation();
                armLeft.setRotateAngleX(rotation[0]);
                armLeft.setRotateAngleY(rotation[1]);
                armLeft.setRotateAngleZ(rotation[2]);
            } else {
                armLeft.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount);
                armLeft.setRotateAngleY(0);
                armLeft.setRotateAngleZ(Math.cos(ageInTicks * 0.05) * 0.05 - 0.4);
                // 手部使用动画
                if (maid.getSwingProgress() > 0.0 && maid.isSwingLeftHand()) {
                    armLeft.setRotateAngleX(armLeft.getRotateAngleX() - (f2 * 1.2 + f3));
                    armLeft.setRotateAngleZ(armLeft.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
                }
            }
        }

        if (armRight != undefined) {
            if (maid.isHoldVehicle()) {
                rotation = maid.getRightHandRotation();
                armRight.setRotateAngleX(rotation[0]);
                armRight.setRotateAngleY(rotation[1]);
                armRight.setRotateAngleZ(rotation[2]);
            } else {
                armRight.setRotateAngleX(Math.cos(limbSwing * 0.67) * 0.7 * limbSwingAmount);
                armRight.setRotateAngleY(0);
                armRight.setRotateAngleZ(-Math.cos(ageInTicks * 0.05) * 0.05 + 0.4);
                // 手部使用动画
                if (maid.getSwingProgress() > 0.0 && !maid.isSwingLeftHand()) {
                    armRight.setRotateAngleX(armRight.getRotateAngleX() - (f2 * 1.2 + f3));
                    armRight.setRotateAngleZ(armRight.getRotateAngleZ() + Math.sin(maid.getSwingProgress() * Math.PI) * -0.4);
                }
            }
        }
    }
})
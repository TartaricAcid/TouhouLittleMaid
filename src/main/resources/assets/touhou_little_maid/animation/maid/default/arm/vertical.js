Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        armLeftVertical = modelMap.get("armLeftVertical");
        armLeft = modelMap.get("armLeft");
        if (armLeftVertical != undefined) {
            if (armLeft != undefined) {
                armLeftVertical.setRotateAngleX(-armLeft.getRotateAngleX());
                armLeftVertical.setRotateAngleZ(-armLeft.getRotateAngleZ());
            }
        }

        armRightVertical = modelMap.get("armRightVertical");
        armRight = modelMap.get("armRight");
        if (armRightVertical != undefined) {
            if (armRight != undefined) {
                armRightVertical.setRotateAngleX(-armRight.getRotateAngleX());
                armRightVertical.setRotateAngleZ(-armRight.getRotateAngleZ());
            }
        }
    }
})
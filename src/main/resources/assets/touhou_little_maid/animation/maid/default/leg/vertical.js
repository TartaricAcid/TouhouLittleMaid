Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        legLeftVertical = modelMap.get("legLeftVertical");
        legLeft = modelMap.get("legLeft");
        if (legLeftVertical != undefined) {
            if (legLeft != undefined) {
                legLeftVertical.setRotateAngleX(-legLeft.getRotateAngleX());
                legLeftVertical.setRotateAngleZ(-legLeft.getRotateAngleZ());
            }
        }

        legRightVertical = modelMap.get("legRightVertical");
        legRight = modelMap.get("legRight");
        if (legRightVertical != undefined) {
            if (legRight != undefined) {
                legRightVertical.setRotateAngleX(-legRight.getRotateAngleX());
                legRightVertical.setRotateAngleZ(-legRight.getRotateAngleZ());
            }
        }
    }
})
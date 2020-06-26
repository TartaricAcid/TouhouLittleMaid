Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        legLeft = modelMap.get("legLeft");
        legRight = modelMap.get("legRight");

        if (legLeft != undefined) {
            legLeft.setRotateAngleX(Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
            legLeft.setRotateAngleY(0);
            legLeft.setRotateAngleZ(0);
        }
        if (legRight != undefined) {
            legRight.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
            legRight.setRotateAngleY(0);
            legRight.setRotateAngleZ(0);
        }
    }
})
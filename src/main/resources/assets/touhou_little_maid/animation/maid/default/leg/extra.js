Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        legLeftExtraA = modelMap.get("legLeftExtraA");
        legRightExtraA = modelMap.get("legRightExtraA");

        if (legLeftExtraA != undefined) {
            legLeftExtraA.setRotateAngleX(Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
            legLeftExtraA.setRotateAngleY(0);
            legLeftExtraA.setRotateAngleZ(0);
        }
        if (legRightExtraA != undefined) {
            legRightExtraA.setRotateAngleX(-Math.cos(limbSwing * 0.67) * 0.3 * limbSwingAmount);
            legRightExtraA.setRotateAngleY(0);
            legRightExtraA.setRotateAngleZ(0);
        }
    }
})
Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        xRotationNormalA = modelMap.get("xRotationNormalA");
        if (xRotationNormalA != undefined) {
            xRotationNormalA.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }

        xRotationNormalB = modelMap.get("xRotationNormalB");
        if (xRotationNormalB != undefined) {
            xRotationNormalB.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }

        xRotationNormalC = modelMap.get("xRotationNormalC");
        if (xRotationNormalC != undefined) {
            xRotationNormalC.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }

        xRotationNormalD = modelMap.get("xRotationNormalD");
        if (xRotationNormalD != undefined) {
            xRotationNormalD.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }

        xRotationNormalE = modelMap.get("xRotationNormalE");
        if (xRotationNormalE != undefined) {
            xRotationNormalE.setRotateAngleX(ageInTicks % 360 * 0.017453292);
        }
    }
})
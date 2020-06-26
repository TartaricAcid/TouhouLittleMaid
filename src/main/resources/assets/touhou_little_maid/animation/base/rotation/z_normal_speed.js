Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        zRotationNormalA = modelMap.get("zRotationNormalA");
        if (zRotationNormalA != undefined) {
            zRotationNormalA.setRotateAngleZ(ageInTicks % 360 * 0.017453292);
        }

        zRotationNormalB = modelMap.get("zRotationNormalB");
        if (zRotationNormalB != undefined) {
            zRotationNormalB.setRotateAngleZ(ageInTicks % 360 * 0.017453292);
        }

        zRotationNormalC = modelMap.get("zRotationNormalC");
        if (zRotationNormalC != undefined) {
            zRotationNormalC.setRotateAngleZ(ageInTicks % 360 * 0.017453292);
        }

        zRotationNormalD = modelMap.get("zRotationNormalD");
        if (zRotationNormalD != undefined) {
            zRotationNormalD.setRotateAngleZ(ageInTicks % 360 * 0.017453292);
        }

        zRotationNormalE = modelMap.get("zRotationNormalE");
        if (zRotationNormalE != undefined) {
            zRotationNormalE.setRotateAngleZ(ageInTicks % 360 * 0.017453292);
        }
    }
})
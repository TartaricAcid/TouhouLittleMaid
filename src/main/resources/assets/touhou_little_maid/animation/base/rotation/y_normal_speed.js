Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        yRotationNormalA = modelMap.get("yRotationNormalA");
        if (yRotationNormalA != undefined) {
            yRotationNormalA.setRotateAngleY(ageInTicks % 360 * 0.017453292);
        }

        yRotationNormalB = modelMap.get("yRotationNormalB");
        if (yRotationNormalB != undefined) {
            yRotationNormalB.setRotateAngleY(ageInTicks % 360 * 0.017453292);
        }

        yRotationNormalC = modelMap.get("yRotationNormalC");
        if (yRotationNormalC != undefined) {
            yRotationNormalC.setRotateAngleY(ageInTicks % 360 * 0.017453292);
        }

        yRotationNormalD = modelMap.get("yRotationNormalD");
        if (yRotationNormalD != undefined) {
            yRotationNormalD.setRotateAngleY(ageInTicks % 360 * 0.017453292);
        }

        yRotationNormalE = modelMap.get("yRotationNormalE");
        if (yRotationNormalE != undefined) {
            yRotationNormalE.setRotateAngleY(ageInTicks % 360 * 0.017453292);
        }
    }
})
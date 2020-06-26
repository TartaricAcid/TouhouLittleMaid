Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        xRotationLowA = modelMap.get("xRotationLowA");
        if (xRotationLowA != undefined) {
            xRotationLowA.setRotateAngleX((ageInTicks / 4) % 360 * 0.017453292);
        }

        xRotationLowB = modelMap.get("xRotationLowB");
        if (xRotationLowB != undefined) {
            xRotationLowB.setRotateAngleX((ageInTicks / 4) % 360 * 0.017453292);
        }

        xRotationLowC = modelMap.get("xRotationLowC");
        if (xRotationLowC != undefined) {
            xRotationLowC.setRotateAngleX((ageInTicks / 4) % 360 * 0.017453292);
        }

        xRotationLowD = modelMap.get("xRotationLowD");
        if (xRotationLowD != undefined) {
            xRotationLowD.setRotateAngleX((ageInTicks / 4) % 360 * 0.017453292);
        }

        xRotationLowE = modelMap.get("xRotationLowE");
        if (xRotationLowE != undefined) {
            xRotationLowE.setRotateAngleX((ageInTicks / 4) % 360 * 0.017453292);
        }
    }
})
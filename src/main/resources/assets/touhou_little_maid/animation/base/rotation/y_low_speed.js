Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        yRotationLowA = modelMap.get("yRotationLowA");
        if (yRotationLowA != undefined) {
            yRotationLowA.setRotateAngleY((ageInTicks / 4) % 360 * 0.017453292);
        }

        yRotationLowB = modelMap.get("yRotationLowB");
        if (yRotationLowB != undefined) {
            yRotationLowB.setRotateAngleY((ageInTicks / 4) % 360 * 0.017453292);
        }

        yRotationLowC = modelMap.get("yRotationLowC");
        if (yRotationLowC != undefined) {
            yRotationLowC.setRotateAngleY((ageInTicks / 4) % 360 * 0.017453292);
        }

        yRotationLowD = modelMap.get("yRotationLowD");
        if (yRotationLowD != undefined) {
            yRotationLowD.setRotateAngleY((ageInTicks / 4) % 360 * 0.017453292);
        }

        yRotationLowE = modelMap.get("yRotationLowE");
        if (yRotationLowE != undefined) {
            yRotationLowE.setRotateAngleY((ageInTicks / 4) % 360 * 0.017453292);
        }
    }
})
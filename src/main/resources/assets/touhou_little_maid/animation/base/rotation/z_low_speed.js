Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        zRotationLowA = modelMap.get("zRotationLowA");
        if (zRotationLowA != undefined) {
            zRotationLowA.setRotateAngleZ((ageInTicks / 4) % 360 * 0.017453292);
        }

        zRotationLowB = modelMap.get("zRotationLowB");
        if (zRotationLowB != undefined) {
            zRotationLowB.setRotateAngleZ((ageInTicks / 4) % 360 * 0.017453292);
        }

        zRotationLowC = modelMap.get("zRotationLowC");
        if (zRotationLowC != undefined) {
            zRotationLowC.setRotateAngleZ((ageInTicks / 4) % 360 * 0.017453292);
        }

        zRotationLowD = modelMap.get("zRotationLowD");
        if (zRotationLowD != undefined) {
            zRotationLowD.setRotateAngleZ((ageInTicks / 4) % 360 * 0.017453292);
        }

        zRotationLowE = modelMap.get("zRotationLowE");
        if (zRotationLowE != undefined) {
            zRotationLowE.setRotateAngleZ((ageInTicks / 4) % 360 * 0.017453292);
        }
    }
})
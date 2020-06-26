Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        yRotationHighA = modelMap.get("yRotationHighA");
        if (yRotationHighA != undefined) {
            yRotationHighA.setRotateAngleY((ageInTicks * 4) % 360 * 0.017453292);
        }

        yRotationHighB = modelMap.get("yRotationHighB");
        if (yRotationHighB != undefined) {
            yRotationHighB.setRotateAngleY((ageInTicks * 4) % 360 * 0.017453292);
        }

        yRotationHighC = modelMap.get("yRotationHighC");
        if (yRotationHighC != undefined) {
            yRotationHighC.setRotateAngleY((ageInTicks * 4) % 360 * 0.017453292);
        }

        yRotationHighD = modelMap.get("yRotationHighD");
        if (yRotationHighD != undefined) {
            yRotationHighD.setRotateAngleY((ageInTicks * 4) % 360 * 0.017453292);
        }

        yRotationHighE = modelMap.get("yRotationHighE");
        if (yRotationHighE != undefined) {
            yRotationHighE.setRotateAngleY((ageInTicks * 4) % 360 * 0.017453292);
        }
    }
})
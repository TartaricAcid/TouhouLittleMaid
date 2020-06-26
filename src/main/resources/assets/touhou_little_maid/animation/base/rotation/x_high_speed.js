Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        xRotationHighA = modelMap.get("xRotationHighA");
        if (xRotationHighA != undefined) {
            xRotationHighA.setRotateAngleX((ageInTicks * 4) % 360 * 0.017453292);
        }

        xRotationHighB = modelMap.get("xRotationHighB");
        if (xRotationHighB != undefined) {
            xRotationHighB.setRotateAngleX((ageInTicks * 4) % 360 * 0.017453292);
        }

        xRotationHighC = modelMap.get("xRotationHighC");
        if (xRotationHighC != undefined) {
            xRotationHighC.setRotateAngleX((ageInTicks * 4) % 360 * 0.017453292);
        }

        xRotationHighD = modelMap.get("xRotationHighD");
        if (xRotationHighD != undefined) {
            xRotationHighD.setRotateAngleX((ageInTicks * 4) % 360 * 0.017453292);
        }

        xRotationHighE = modelMap.get("xRotationHighE");
        if (xRotationHighE != undefined) {
            xRotationHighE.setRotateAngleX((ageInTicks * 4) % 360 * 0.017453292);
        }
    }
})
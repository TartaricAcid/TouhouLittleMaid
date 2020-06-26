Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        zRotationHighA = modelMap.get("zRotationHighA");
        if (zRotationHighA != undefined) {
            zRotationHighA.setRotateAngleZ((ageInTicks * 4) % 360 * 0.017453292);
        }

        zRotationHighB = modelMap.get("zRotationHighB");
        if (zRotationHighB != undefined) {
            zRotationHighB.setRotateAngleZ((ageInTicks * 4) % 360 * 0.017453292);
        }

        zRotationHighC = modelMap.get("zRotationHighC");
        if (zRotationHighC != undefined) {
            zRotationHighC.setRotateAngleZ((ageInTicks * 4) % 360 * 0.017453292);
        }

        zRotationHighD = modelMap.get("zRotationHighD");
        if (zRotationHighD != undefined) {
            zRotationHighD.setRotateAngleZ((ageInTicks * 4) % 360 * 0.017453292);
        }

        zRotationHighE = modelMap.get("zRotationHighE");
        if (zRotationHighE != undefined) {
            zRotationHighE.setRotateAngleZ((ageInTicks * 4) % 360 * 0.017453292);
        }
    }
})
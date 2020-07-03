Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetTempOcean = modelMap.get("helmetTempOcean");
        chestPlateTempOcean = modelMap.get("chestPlateTempOcean");
        chestPlateLeftTempOcean = modelMap.get("chestPlateLeftTempOcean");
        chestPlateMiddleTempOcean = modelMap.get("chestPlateMiddleTempOcean");
        chestPlateRightTempOcean = modelMap.get("chestPlateRightTempOcean");
        leggingsTempOcean = modelMap.get("leggingsTempOcean");
        leggingsLeftTempOcean = modelMap.get("leggingsLeftTempOcean");
        leggingsMiddleTempOcean = modelMap.get("leggingsMiddleTempOcean");
        leggingsRightTempOcean = modelMap.get("leggingsRightTempOcean");
        bootsLeftTempOcean = modelMap.get("bootsLeftTempOcean");
        bootsRightTempOcean = modelMap.get("bootsRightTempOcean");

        tempIsOcean = maid.getAtBiome() === "OCEAN";

        if (helmetTempOcean != undefined) {
            helmetTempOcean.setHidden(!tempIsOcean);
        }
        if (chestPlateTempOcean != undefined) {
            chestPlateTempOcean.setHidden(!tempIsOcean);
        }
        if (chestPlateLeftTempOcean != undefined) {
            chestPlateLeftTempOcean.setHidden(!tempIsOcean);
        }
        if (chestPlateMiddleTempOcean != undefined) {
            chestPlateMiddleTempOcean.setHidden(!tempIsOcean);
        }
        if (chestPlateRightTempOcean != undefined) {
            chestPlateRightTempOcean.setHidden(!tempIsOcean);
        }
        if (leggingsTempOcean != undefined) {
            leggingsTempOcean.setHidden(!tempIsOcean);
        }
        if (leggingsLeftTempOcean != undefined) {
            leggingsLeftTempOcean.setHidden(!tempIsOcean);
        }
        if (leggingsMiddleTempOcean != undefined) {
            leggingsMiddleTempOcean.setHidden(!tempIsOcean);
        }
        if (leggingsRightTempOcean != undefined) {
            leggingsRightTempOcean.setHidden(!tempIsOcean);
        }
        if (bootsLeftTempOcean != undefined) {
            bootsLeftTempOcean.setHidden(!tempIsOcean);
        }
        if (bootsRightTempOcean != undefined) {
            bootsRightTempOcean.setHidden(!tempIsOcean);
        }
    }
})
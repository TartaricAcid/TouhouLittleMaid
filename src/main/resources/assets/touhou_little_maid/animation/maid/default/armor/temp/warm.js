Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetTempWarm = modelMap.get("helmetTempWarm");
        chestPlateTempWarm = modelMap.get("chestPlateTempWarm");
        chestPlateLeftTempWarm = modelMap.get("chestPlateLeftTempWarm");
        chestPlateMiddleTempWarm = modelMap.get("chestPlateMiddleTempWarm");
        chestPlateRightTempWarm = modelMap.get("chestPlateRightTempWarm");
        leggingsTempWarm = modelMap.get("leggingsTempWarm");
        leggingsLeftTempWarm = modelMap.get("leggingsLeftTempWarm");
        leggingsMiddleTempWarm = modelMap.get("leggingsMiddleTempWarm");
        leggingsRightTempWarm = modelMap.get("leggingsRightTempWarm");
        bootsLeftTempWarm = modelMap.get("bootsLeftTempWarm");
        bootsRightTempWarm = modelMap.get("bootsRightTempWarm");

        tempIsWarm = maid.getAtBiomeTemp() === "WARM";

        if (helmetTempWarm != undefined) {
            helmetTempWarm.setHidden(!tempIsWarm);
        }
        if (chestPlateTempWarm != undefined) {
            chestPlateTempWarm.setHidden(!tempIsWarm);
        }
        if (chestPlateLeftTempWarm != undefined) {
            chestPlateLeftTempWarm.setHidden(!tempIsWarm);
        }
        if (chestPlateMiddleTempWarm != undefined) {
            chestPlateMiddleTempWarm.setHidden(!tempIsWarm);
        }
        if (chestPlateRightTempWarm != undefined) {
            chestPlateRightTempWarm.setHidden(!tempIsWarm);
        }
        if (leggingsTempWarm != undefined) {
            leggingsTempWarm.setHidden(!tempIsWarm);
        }
        if (leggingsLeftTempWarm != undefined) {
            leggingsLeftTempWarm.setHidden(!tempIsWarm);
        }
        if (leggingsMiddleTempWarm != undefined) {
            leggingsMiddleTempWarm.setHidden(!tempIsWarm);
        }
        if (leggingsRightTempWarm != undefined) {
            leggingsRightTempWarm.setHidden(!tempIsWarm);
        }
        if (bootsLeftTempWarm != undefined) {
            bootsLeftTempWarm.setHidden(!tempIsWarm);
        }
        if (bootsRightTempWarm != undefined) {
            bootsRightTempWarm.setHidden(!tempIsWarm);
        }
    }
})
Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetTempCold = modelMap.get("helmetTempCold");
        chestPlateTempCold = modelMap.get("chestPlateTempCold");
        chestPlateLeftTempCold = modelMap.get("chestPlateLeftTempCold");
        chestPlateMiddleTempCold = modelMap.get("chestPlateMiddleTempCold");
        chestPlateRightTempCold = modelMap.get("chestPlateRightTempCold");
        leggingsTempCold = modelMap.get("leggingsTempCold");
        leggingsLeftTempCold = modelMap.get("leggingsLeftTempCold");
        leggingsMiddleTempCold = modelMap.get("leggingsMiddleTempCold");
        leggingsRightTempCold = modelMap.get("leggingsRightTempCold");
        bootsLeftTempCold = modelMap.get("bootsLeftTempCold");
        bootsRightTempCold = modelMap.get("bootsRightTempCold");

        tempIsCold = maid.getAtBiomeTemp() === "COLD";

        if (helmetTempCold != undefined) {
            helmetTempCold.setHidden(!tempIsCold);
        }
        if (chestPlateTempCold != undefined) {
            chestPlateTempCold.setHidden(!tempIsCold);
        }
        if (chestPlateLeftTempCold != undefined) {
            chestPlateLeftTempCold.setHidden(!tempIsCold);
        }
        if (chestPlateMiddleTempCold != undefined) {
            chestPlateMiddleTempCold.setHidden(!tempIsCold);
        }
        if (chestPlateRightTempCold != undefined) {
            chestPlateRightTempCold.setHidden(!tempIsCold);
        }
        if (leggingsTempCold != undefined) {
            leggingsTempCold.setHidden(!tempIsCold);
        }
        if (leggingsLeftTempCold != undefined) {
            leggingsLeftTempCold.setHidden(!tempIsCold);
        }
        if (leggingsMiddleTempCold != undefined) {
            leggingsMiddleTempCold.setHidden(!tempIsCold);
        }
        if (leggingsRightTempCold != undefined) {
            leggingsRightTempCold.setHidden(!tempIsCold);
        }
        if (bootsLeftTempCold != undefined) {
            bootsLeftTempCold.setHidden(!tempIsCold);
        }
        if (bootsRightTempCold != undefined) {
            bootsRightTempCold.setHidden(!tempIsCold);
        }
    }
})
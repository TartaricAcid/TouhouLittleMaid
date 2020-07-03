Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetTempMedium = modelMap.get("helmetTempMedium");
        chestPlateTempMedium = modelMap.get("chestPlateTempMedium");
        chestPlateLeftTempMedium = modelMap.get("chestPlateLeftTempMedium");
        chestPlateMiddleTempMedium = modelMap.get("chestPlateMiddleTempMedium");
        chestPlateRightTempMedium = modelMap.get("chestPlateRightTempMedium");
        leggingsTempMedium = modelMap.get("leggingsTempMedium");
        leggingsLeftTempMedium = modelMap.get("leggingsLeftTempMedium");
        leggingsMiddleTempMedium = modelMap.get("leggingsMiddleTempMedium");
        leggingsRightTempMedium = modelMap.get("leggingsRightTempMedium");
        bootsLeftTempMedium = modelMap.get("bootsLeftTempMedium");
        bootsRightTempMedium = modelMap.get("bootsRightTempMedium");

        tempIsMedium = maid.getAtBiomeTemp() === "MEDIUM";

        if (helmetTempMedium != undefined) {
            helmetTempMedium.setHidden(!tempIsMedium);
        }
        if (chestPlateTempMedium != undefined) {
            chestPlateTempMedium.setHidden(!tempIsMedium);
        }
        if (chestPlateLeftTempMedium != undefined) {
            chestPlateLeftTempMedium.setHidden(!tempIsMedium);
        }
        if (chestPlateMiddleTempMedium != undefined) {
            chestPlateMiddleTempMedium.setHidden(!tempIsMedium);
        }
        if (chestPlateRightTempMedium != undefined) {
            chestPlateRightTempMedium.setHidden(!tempIsMedium);
        }
        if (leggingsTempMedium != undefined) {
            leggingsTempMedium.setHidden(!tempIsMedium);
        }
        if (leggingsLeftTempMedium != undefined) {
            leggingsLeftTempMedium.setHidden(!tempIsMedium);
        }
        if (leggingsMiddleTempMedium != undefined) {
            leggingsMiddleTempMedium.setHidden(!tempIsMedium);
        }
        if (leggingsRightTempMedium != undefined) {
            leggingsRightTempMedium.setHidden(!tempIsMedium);
        }
        if (bootsLeftTempMedium != undefined) {
            bootsLeftTempMedium.setHidden(!tempIsMedium);
        }
        if (bootsRightTempMedium != undefined) {
            bootsRightTempMedium.setHidden(!tempIsMedium);
        }
    }
})
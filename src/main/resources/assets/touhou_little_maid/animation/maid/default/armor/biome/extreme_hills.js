Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeExtremeHills = modelMap.get("helmetBiomeExtremeHills");
        chestPlateBiomeExtremeHills = modelMap.get("chestPlateBiomeExtremeHills");
        chestPlateLeftBiomeExtremeHills = modelMap.get("chestPlateLeftBiomeExtremeHills");
        chestPlateMiddleBiomeExtremeHills = modelMap.get("chestPlateMiddleBiomeExtremeHills");
        chestPlateRightBiomeExtremeHills = modelMap.get("chestPlateRightBiomeExtremeHills");
        leggingsBiomeExtremeHills = modelMap.get("leggingsBiomeExtremeHills");
        leggingsLeftBiomeExtremeHills = modelMap.get("leggingsLeftBiomeExtremeHills");
        leggingsMiddleBiomeExtremeHills = modelMap.get("leggingsMiddleBiomeExtremeHills");
        leggingsRightBiomeExtremeHills = modelMap.get("leggingsRightBiomeExtremeHills");
        bootsLeftBiomeExtremeHills = modelMap.get("bootsLeftBiomeExtremeHills");
        bootsRightBiomeExtremeHills = modelMap.get("bootsRightBiomeExtremeHills");

        biomeIsExtremeHills = maid.getAtBiomeBiome() === "extreme_hills";

        if (helmetBiomeExtremeHills != undefined) {
            helmetBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (chestPlateBiomeExtremeHills != undefined) {
            chestPlateBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (chestPlateLeftBiomeExtremeHills != undefined) {
            chestPlateLeftBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (chestPlateMiddleBiomeExtremeHills != undefined) {
            chestPlateMiddleBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (chestPlateRightBiomeExtremeHills != undefined) {
            chestPlateRightBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (leggingsBiomeExtremeHills != undefined) {
            leggingsBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (leggingsLeftBiomeExtremeHills != undefined) {
            leggingsLeftBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (leggingsMiddleBiomeExtremeHills != undefined) {
            leggingsMiddleBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (leggingsRightBiomeExtremeHills != undefined) {
            leggingsRightBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (bootsLeftBiomeExtremeHills != undefined) {
            bootsLeftBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
        if (bootsRightBiomeExtremeHills != undefined) {
            bootsRightBiomeExtremeHills.setHidden(!biomeIsExtremeHills);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeDesertHills = modelMap.get("helmetBiomeDesertHills");
        chestPlateBiomeDesertHills = modelMap.get("chestPlateBiomeDesertHills");
        chestPlateLeftBiomeDesertHills = modelMap.get("chestPlateLeftBiomeDesertHills");
        chestPlateMiddleBiomeDesertHills = modelMap.get("chestPlateMiddleBiomeDesertHills");
        chestPlateRightBiomeDesertHills = modelMap.get("chestPlateRightBiomeDesertHills");
        leggingsBiomeDesertHills = modelMap.get("leggingsBiomeDesertHills");
        leggingsLeftBiomeDesertHills = modelMap.get("leggingsLeftBiomeDesertHills");
        leggingsMiddleBiomeDesertHills = modelMap.get("leggingsMiddleBiomeDesertHills");
        leggingsRightBiomeDesertHills = modelMap.get("leggingsRightBiomeDesertHills");
        bootsLeftBiomeDesertHills = modelMap.get("bootsLeftBiomeDesertHills");
        bootsRightBiomeDesertHills = modelMap.get("bootsRightBiomeDesertHills");

        biomeIsDesertHills = maid.getAtBiomeBiome() === "desert_hills";

        if (helmetBiomeDesertHills != undefined) {
            helmetBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (chestPlateBiomeDesertHills != undefined) {
            chestPlateBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (chestPlateLeftBiomeDesertHills != undefined) {
            chestPlateLeftBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (chestPlateMiddleBiomeDesertHills != undefined) {
            chestPlateMiddleBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (chestPlateRightBiomeDesertHills != undefined) {
            chestPlateRightBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (leggingsBiomeDesertHills != undefined) {
            leggingsBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (leggingsLeftBiomeDesertHills != undefined) {
            leggingsLeftBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (leggingsMiddleBiomeDesertHills != undefined) {
            leggingsMiddleBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (leggingsRightBiomeDesertHills != undefined) {
            leggingsRightBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (bootsLeftBiomeDesertHills != undefined) {
            bootsLeftBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
        if (bootsRightBiomeDesertHills != undefined) {
            bootsRightBiomeDesertHills.setHidden(!biomeIsDesertHills);
        }
    }
})

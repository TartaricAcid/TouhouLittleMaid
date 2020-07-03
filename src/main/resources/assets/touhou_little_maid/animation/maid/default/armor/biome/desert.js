Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeDesert = modelMap.get("helmetBiomeDesert");
        chestPlateBiomeDesert = modelMap.get("chestPlateBiomeDesert");
        chestPlateLeftBiomeDesert = modelMap.get("chestPlateLeftBiomeDesert");
        chestPlateMiddleBiomeDesert = modelMap.get("chestPlateMiddleBiomeDesert");
        chestPlateRightBiomeDesert = modelMap.get("chestPlateRightBiomeDesert");
        leggingsBiomeDesert = modelMap.get("leggingsBiomeDesert");
        leggingsLeftBiomeDesert = modelMap.get("leggingsLeftBiomeDesert");
        leggingsMiddleBiomeDesert = modelMap.get("leggingsMiddleBiomeDesert");
        leggingsRightBiomeDesert = modelMap.get("leggingsRightBiomeDesert");
        bootsLeftBiomeDesert = modelMap.get("bootsLeftBiomeDesert");
        bootsRightBiomeDesert = modelMap.get("bootsRightBiomeDesert");

        biomeIsDesert = maid.getAtBiomeBiome() === "desert";

        if (helmetBiomeDesert != undefined) {
            helmetBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (chestPlateBiomeDesert != undefined) {
            chestPlateBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (chestPlateLeftBiomeDesert != undefined) {
            chestPlateLeftBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (chestPlateMiddleBiomeDesert != undefined) {
            chestPlateMiddleBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (chestPlateRightBiomeDesert != undefined) {
            chestPlateRightBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (leggingsBiomeDesert != undefined) {
            leggingsBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (leggingsLeftBiomeDesert != undefined) {
            leggingsLeftBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (leggingsMiddleBiomeDesert != undefined) {
            leggingsMiddleBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (leggingsRightBiomeDesert != undefined) {
            leggingsRightBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (bootsLeftBiomeDesert != undefined) {
            bootsLeftBiomeDesert.setHidden(!biomeIsDesert);
        }
        if (bootsRightBiomeDesert != undefined) {
            bootsRightBiomeDesert.setHidden(!biomeIsDesert);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeForestHills = modelMap.get("helmetBiomeForestHills");
        chestPlateBiomeForestHills = modelMap.get("chestPlateBiomeForestHills");
        chestPlateLeftBiomeForestHills = modelMap.get("chestPlateLeftBiomeForestHills");
        chestPlateMiddleBiomeForestHills = modelMap.get("chestPlateMiddleBiomeForestHills");
        chestPlateRightBiomeForestHills = modelMap.get("chestPlateRightBiomeForestHills");
        leggingsBiomeForestHills = modelMap.get("leggingsBiomeForestHills");
        leggingsLeftBiomeForestHills = modelMap.get("leggingsLeftBiomeForestHills");
        leggingsMiddleBiomeForestHills = modelMap.get("leggingsMiddleBiomeForestHills");
        leggingsRightBiomeForestHills = modelMap.get("leggingsRightBiomeForestHills");
        bootsLeftBiomeForestHills = modelMap.get("bootsLeftBiomeForestHills");
        bootsRightBiomeForestHills = modelMap.get("bootsRightBiomeForestHills");

        biomeIsForestHills = maid.getAtBiomeBiome() === "forest_hills";

        if (helmetBiomeForestHills != undefined) {
            helmetBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (chestPlateBiomeForestHills != undefined) {
            chestPlateBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (chestPlateLeftBiomeForestHills != undefined) {
            chestPlateLeftBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (chestPlateMiddleBiomeForestHills != undefined) {
            chestPlateMiddleBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (chestPlateRightBiomeForestHills != undefined) {
            chestPlateRightBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (leggingsBiomeForestHills != undefined) {
            leggingsBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (leggingsLeftBiomeForestHills != undefined) {
            leggingsLeftBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (leggingsMiddleBiomeForestHills != undefined) {
            leggingsMiddleBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (leggingsRightBiomeForestHills != undefined) {
            leggingsRightBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (bootsLeftBiomeForestHills != undefined) {
            bootsLeftBiomeForestHills.setHidden(!biomeIsForestHills);
        }
        if (bootsRightBiomeForestHills != undefined) {
            bootsRightBiomeForestHills.setHidden(!biomeIsForestHills);
        }
    }
})

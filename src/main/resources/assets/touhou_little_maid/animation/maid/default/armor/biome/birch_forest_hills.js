Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeBirchForestHills = modelMap.get("helmetBiomeBirchForestHills");
        chestPlateBiomeBirchForestHills = modelMap.get("chestPlateBiomeBirchForestHills");
        chestPlateLeftBiomeBirchForestHills = modelMap.get("chestPlateLeftBiomeBirchForestHills");
        chestPlateMiddleBiomeBirchForestHills = modelMap.get("chestPlateMiddleBiomeBirchForestHills");
        chestPlateRightBiomeBirchForestHills = modelMap.get("chestPlateRightBiomeBirchForestHills");
        leggingsBiomeBirchForestHills = modelMap.get("leggingsBiomeBirchForestHills");
        leggingsLeftBiomeBirchForestHills = modelMap.get("leggingsLeftBiomeBirchForestHills");
        leggingsMiddleBiomeBirchForestHills = modelMap.get("leggingsMiddleBiomeBirchForestHills");
        leggingsRightBiomeBirchForestHills = modelMap.get("leggingsRightBiomeBirchForestHills");
        bootsLeftBiomeBirchForestHills = modelMap.get("bootsLeftBiomeBirchForestHills");
        bootsRightBiomeBirchForestHills = modelMap.get("bootsRightBiomeBirchForestHills");

        biomeIsBirchForestHills = maid.getAtBiomeBiome() === "birch_forest_hills";

        if (helmetBiomeBirchForestHills != undefined) {
            helmetBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (chestPlateBiomeBirchForestHills != undefined) {
            chestPlateBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (chestPlateLeftBiomeBirchForestHills != undefined) {
            chestPlateLeftBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (chestPlateMiddleBiomeBirchForestHills != undefined) {
            chestPlateMiddleBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (chestPlateRightBiomeBirchForestHills != undefined) {
            chestPlateRightBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (leggingsBiomeBirchForestHills != undefined) {
            leggingsBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (leggingsLeftBiomeBirchForestHills != undefined) {
            leggingsLeftBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (leggingsMiddleBiomeBirchForestHills != undefined) {
            leggingsMiddleBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (leggingsRightBiomeBirchForestHills != undefined) {
            leggingsRightBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (bootsLeftBiomeBirchForestHills != undefined) {
            bootsLeftBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
        if (bootsRightBiomeBirchForestHills != undefined) {
            bootsRightBiomeBirchForestHills.setHidden(!biomeIsBirchForestHills);
        }
    }
})

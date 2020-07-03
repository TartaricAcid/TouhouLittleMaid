Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedBirchForestHills = modelMap.get("helmetBiomeMutatedBirchForestHills");
        chestPlateBiomeMutatedBirchForestHills = modelMap.get("chestPlateBiomeMutatedBirchForestHills");
        chestPlateLeftBiomeMutatedBirchForestHills = modelMap.get("chestPlateLeftBiomeMutatedBirchForestHills");
        chestPlateMiddleBiomeMutatedBirchForestHills = modelMap.get("chestPlateMiddleBiomeMutatedBirchForestHills");
        chestPlateRightBiomeMutatedBirchForestHills = modelMap.get("chestPlateRightBiomeMutatedBirchForestHills");
        leggingsBiomeMutatedBirchForestHills = modelMap.get("leggingsBiomeMutatedBirchForestHills");
        leggingsLeftBiomeMutatedBirchForestHills = modelMap.get("leggingsLeftBiomeMutatedBirchForestHills");
        leggingsMiddleBiomeMutatedBirchForestHills = modelMap.get("leggingsMiddleBiomeMutatedBirchForestHills");
        leggingsRightBiomeMutatedBirchForestHills = modelMap.get("leggingsRightBiomeMutatedBirchForestHills");
        bootsLeftBiomeMutatedBirchForestHills = modelMap.get("bootsLeftBiomeMutatedBirchForestHills");
        bootsRightBiomeMutatedBirchForestHills = modelMap.get("bootsRightBiomeMutatedBirchForestHills");

        biomeIsMutatedBirchForestHills = maid.getAtBiomeBiome() === "mutated_birch_forest_hills";

        if (helmetBiomeMutatedBirchForestHills != undefined) {
            helmetBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (chestPlateBiomeMutatedBirchForestHills != undefined) {
            chestPlateBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (chestPlateLeftBiomeMutatedBirchForestHills != undefined) {
            chestPlateLeftBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (chestPlateMiddleBiomeMutatedBirchForestHills != undefined) {
            chestPlateMiddleBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (chestPlateRightBiomeMutatedBirchForestHills != undefined) {
            chestPlateRightBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (leggingsBiomeMutatedBirchForestHills != undefined) {
            leggingsBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (leggingsLeftBiomeMutatedBirchForestHills != undefined) {
            leggingsLeftBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (leggingsMiddleBiomeMutatedBirchForestHills != undefined) {
            leggingsMiddleBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (leggingsRightBiomeMutatedBirchForestHills != undefined) {
            leggingsRightBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (bootsLeftBiomeMutatedBirchForestHills != undefined) {
            bootsLeftBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
        if (bootsRightBiomeMutatedBirchForestHills != undefined) {
            bootsRightBiomeMutatedBirchForestHills.setHidden(!biomeIsMutatedBirchForestHills);
        }
    }
})

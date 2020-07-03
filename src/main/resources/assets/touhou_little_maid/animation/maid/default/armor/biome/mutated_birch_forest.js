Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedBirchForest = modelMap.get("helmetBiomeMutatedBirchForest");
        chestPlateBiomeMutatedBirchForest = modelMap.get("chestPlateBiomeMutatedBirchForest");
        chestPlateLeftBiomeMutatedBirchForest = modelMap.get("chestPlateLeftBiomeMutatedBirchForest");
        chestPlateMiddleBiomeMutatedBirchForest = modelMap.get("chestPlateMiddleBiomeMutatedBirchForest");
        chestPlateRightBiomeMutatedBirchForest = modelMap.get("chestPlateRightBiomeMutatedBirchForest");
        leggingsBiomeMutatedBirchForest = modelMap.get("leggingsBiomeMutatedBirchForest");
        leggingsLeftBiomeMutatedBirchForest = modelMap.get("leggingsLeftBiomeMutatedBirchForest");
        leggingsMiddleBiomeMutatedBirchForest = modelMap.get("leggingsMiddleBiomeMutatedBirchForest");
        leggingsRightBiomeMutatedBirchForest = modelMap.get("leggingsRightBiomeMutatedBirchForest");
        bootsLeftBiomeMutatedBirchForest = modelMap.get("bootsLeftBiomeMutatedBirchForest");
        bootsRightBiomeMutatedBirchForest = modelMap.get("bootsRightBiomeMutatedBirchForest");

        biomeIsMutatedBirchForest = maid.getAtBiomeBiome() === "mutated_birch_forest";

        if (helmetBiomeMutatedBirchForest != undefined) {
            helmetBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (chestPlateBiomeMutatedBirchForest != undefined) {
            chestPlateBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (chestPlateLeftBiomeMutatedBirchForest != undefined) {
            chestPlateLeftBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (chestPlateMiddleBiomeMutatedBirchForest != undefined) {
            chestPlateMiddleBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (chestPlateRightBiomeMutatedBirchForest != undefined) {
            chestPlateRightBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (leggingsBiomeMutatedBirchForest != undefined) {
            leggingsBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (leggingsLeftBiomeMutatedBirchForest != undefined) {
            leggingsLeftBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (leggingsMiddleBiomeMutatedBirchForest != undefined) {
            leggingsMiddleBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (leggingsRightBiomeMutatedBirchForest != undefined) {
            leggingsRightBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (bootsLeftBiomeMutatedBirchForest != undefined) {
            bootsLeftBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
        if (bootsRightBiomeMutatedBirchForest != undefined) {
            bootsRightBiomeMutatedBirchForest.setHidden(!biomeIsMutatedBirchForest);
        }
    }
})

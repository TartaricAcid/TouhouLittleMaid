Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedForest = modelMap.get("helmetBiomeMutatedForest");
        chestPlateBiomeMutatedForest = modelMap.get("chestPlateBiomeMutatedForest");
        chestPlateLeftBiomeMutatedForest = modelMap.get("chestPlateLeftBiomeMutatedForest");
        chestPlateMiddleBiomeMutatedForest = modelMap.get("chestPlateMiddleBiomeMutatedForest");
        chestPlateRightBiomeMutatedForest = modelMap.get("chestPlateRightBiomeMutatedForest");
        leggingsBiomeMutatedForest = modelMap.get("leggingsBiomeMutatedForest");
        leggingsLeftBiomeMutatedForest = modelMap.get("leggingsLeftBiomeMutatedForest");
        leggingsMiddleBiomeMutatedForest = modelMap.get("leggingsMiddleBiomeMutatedForest");
        leggingsRightBiomeMutatedForest = modelMap.get("leggingsRightBiomeMutatedForest");
        bootsLeftBiomeMutatedForest = modelMap.get("bootsLeftBiomeMutatedForest");
        bootsRightBiomeMutatedForest = modelMap.get("bootsRightBiomeMutatedForest");

        biomeIsMutatedForest = maid.getAtBiomeBiome() === "mutated_forest";

        if (helmetBiomeMutatedForest != undefined) {
            helmetBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (chestPlateBiomeMutatedForest != undefined) {
            chestPlateBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (chestPlateLeftBiomeMutatedForest != undefined) {
            chestPlateLeftBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (chestPlateMiddleBiomeMutatedForest != undefined) {
            chestPlateMiddleBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (chestPlateRightBiomeMutatedForest != undefined) {
            chestPlateRightBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (leggingsBiomeMutatedForest != undefined) {
            leggingsBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (leggingsLeftBiomeMutatedForest != undefined) {
            leggingsLeftBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (leggingsMiddleBiomeMutatedForest != undefined) {
            leggingsMiddleBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (leggingsRightBiomeMutatedForest != undefined) {
            leggingsRightBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (bootsLeftBiomeMutatedForest != undefined) {
            bootsLeftBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
        if (bootsRightBiomeMutatedForest != undefined) {
            bootsRightBiomeMutatedForest.setHidden(!biomeIsMutatedForest);
        }
    }
})

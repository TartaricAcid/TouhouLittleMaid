Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedRoofedForest = modelMap.get("helmetBiomeMutatedRoofedForest");
        chestPlateBiomeMutatedRoofedForest = modelMap.get("chestPlateBiomeMutatedRoofedForest");
        chestPlateLeftBiomeMutatedRoofedForest = modelMap.get("chestPlateLeftBiomeMutatedRoofedForest");
        chestPlateMiddleBiomeMutatedRoofedForest = modelMap.get("chestPlateMiddleBiomeMutatedRoofedForest");
        chestPlateRightBiomeMutatedRoofedForest = modelMap.get("chestPlateRightBiomeMutatedRoofedForest");
        leggingsBiomeMutatedRoofedForest = modelMap.get("leggingsBiomeMutatedRoofedForest");
        leggingsLeftBiomeMutatedRoofedForest = modelMap.get("leggingsLeftBiomeMutatedRoofedForest");
        leggingsMiddleBiomeMutatedRoofedForest = modelMap.get("leggingsMiddleBiomeMutatedRoofedForest");
        leggingsRightBiomeMutatedRoofedForest = modelMap.get("leggingsRightBiomeMutatedRoofedForest");
        bootsLeftBiomeMutatedRoofedForest = modelMap.get("bootsLeftBiomeMutatedRoofedForest");
        bootsRightBiomeMutatedRoofedForest = modelMap.get("bootsRightBiomeMutatedRoofedForest");

        biomeIsMutatedRoofedForest = maid.getAtBiomeBiome() === "mutated_roofed_forest";

        if (helmetBiomeMutatedRoofedForest != undefined) {
            helmetBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (chestPlateBiomeMutatedRoofedForest != undefined) {
            chestPlateBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (chestPlateLeftBiomeMutatedRoofedForest != undefined) {
            chestPlateLeftBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (chestPlateMiddleBiomeMutatedRoofedForest != undefined) {
            chestPlateMiddleBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (chestPlateRightBiomeMutatedRoofedForest != undefined) {
            chestPlateRightBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (leggingsBiomeMutatedRoofedForest != undefined) {
            leggingsBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (leggingsLeftBiomeMutatedRoofedForest != undefined) {
            leggingsLeftBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (leggingsMiddleBiomeMutatedRoofedForest != undefined) {
            leggingsMiddleBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (leggingsRightBiomeMutatedRoofedForest != undefined) {
            leggingsRightBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (bootsLeftBiomeMutatedRoofedForest != undefined) {
            bootsLeftBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
        if (bootsRightBiomeMutatedRoofedForest != undefined) {
            bootsRightBiomeMutatedRoofedForest.setHidden(!biomeIsMutatedRoofedForest);
        }
    }
})

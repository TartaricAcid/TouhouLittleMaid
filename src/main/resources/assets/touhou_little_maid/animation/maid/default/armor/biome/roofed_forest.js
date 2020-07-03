Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeRoofedForest = modelMap.get("helmetBiomeRoofedForest");
        chestPlateBiomeRoofedForest = modelMap.get("chestPlateBiomeRoofedForest");
        chestPlateLeftBiomeRoofedForest = modelMap.get("chestPlateLeftBiomeRoofedForest");
        chestPlateMiddleBiomeRoofedForest = modelMap.get("chestPlateMiddleBiomeRoofedForest");
        chestPlateRightBiomeRoofedForest = modelMap.get("chestPlateRightBiomeRoofedForest");
        leggingsBiomeRoofedForest = modelMap.get("leggingsBiomeRoofedForest");
        leggingsLeftBiomeRoofedForest = modelMap.get("leggingsLeftBiomeRoofedForest");
        leggingsMiddleBiomeRoofedForest = modelMap.get("leggingsMiddleBiomeRoofedForest");
        leggingsRightBiomeRoofedForest = modelMap.get("leggingsRightBiomeRoofedForest");
        bootsLeftBiomeRoofedForest = modelMap.get("bootsLeftBiomeRoofedForest");
        bootsRightBiomeRoofedForest = modelMap.get("bootsRightBiomeRoofedForest");

        biomeIsRoofedForest = maid.getAtBiomeBiome() === "roofed_forest";

        if (helmetBiomeRoofedForest != undefined) {
            helmetBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (chestPlateBiomeRoofedForest != undefined) {
            chestPlateBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (chestPlateLeftBiomeRoofedForest != undefined) {
            chestPlateLeftBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (chestPlateMiddleBiomeRoofedForest != undefined) {
            chestPlateMiddleBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (chestPlateRightBiomeRoofedForest != undefined) {
            chestPlateRightBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (leggingsBiomeRoofedForest != undefined) {
            leggingsBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (leggingsLeftBiomeRoofedForest != undefined) {
            leggingsLeftBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (leggingsMiddleBiomeRoofedForest != undefined) {
            leggingsMiddleBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (leggingsRightBiomeRoofedForest != undefined) {
            leggingsRightBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (bootsLeftBiomeRoofedForest != undefined) {
            bootsLeftBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
        if (bootsRightBiomeRoofedForest != undefined) {
            bootsRightBiomeRoofedForest.setHidden(!biomeIsRoofedForest);
        }
    }
})

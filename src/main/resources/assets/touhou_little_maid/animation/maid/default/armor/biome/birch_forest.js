Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeBirchForest = modelMap.get("helmetBiomeBirchForest");
        chestPlateBiomeBirchForest = modelMap.get("chestPlateBiomeBirchForest");
        chestPlateLeftBiomeBirchForest = modelMap.get("chestPlateLeftBiomeBirchForest");
        chestPlateMiddleBiomeBirchForest = modelMap.get("chestPlateMiddleBiomeBirchForest");
        chestPlateRightBiomeBirchForest = modelMap.get("chestPlateRightBiomeBirchForest");
        leggingsBiomeBirchForest = modelMap.get("leggingsBiomeBirchForest");
        leggingsLeftBiomeBirchForest = modelMap.get("leggingsLeftBiomeBirchForest");
        leggingsMiddleBiomeBirchForest = modelMap.get("leggingsMiddleBiomeBirchForest");
        leggingsRightBiomeBirchForest = modelMap.get("leggingsRightBiomeBirchForest");
        bootsLeftBiomeBirchForest = modelMap.get("bootsLeftBiomeBirchForest");
        bootsRightBiomeBirchForest = modelMap.get("bootsRightBiomeBirchForest");

        biomeIsBirchForest = maid.getAtBiomeBiome() === "birch_forest";

        if (helmetBiomeBirchForest != undefined) {
            helmetBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (chestPlateBiomeBirchForest != undefined) {
            chestPlateBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (chestPlateLeftBiomeBirchForest != undefined) {
            chestPlateLeftBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (chestPlateMiddleBiomeBirchForest != undefined) {
            chestPlateMiddleBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (chestPlateRightBiomeBirchForest != undefined) {
            chestPlateRightBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (leggingsBiomeBirchForest != undefined) {
            leggingsBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (leggingsLeftBiomeBirchForest != undefined) {
            leggingsLeftBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (leggingsMiddleBiomeBirchForest != undefined) {
            leggingsMiddleBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (leggingsRightBiomeBirchForest != undefined) {
            leggingsRightBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (bootsLeftBiomeBirchForest != undefined) {
            bootsLeftBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
        if (bootsRightBiomeBirchForest != undefined) {
            bootsRightBiomeBirchForest.setHidden(!biomeIsBirchForest);
        }
    }
})

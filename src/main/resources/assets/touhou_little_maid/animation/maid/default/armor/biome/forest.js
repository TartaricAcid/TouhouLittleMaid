Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeForest = modelMap.get("helmetBiomeForest");
        chestPlateBiomeForest = modelMap.get("chestPlateBiomeForest");
        chestPlateLeftBiomeForest = modelMap.get("chestPlateLeftBiomeForest");
        chestPlateMiddleBiomeForest = modelMap.get("chestPlateMiddleBiomeForest");
        chestPlateRightBiomeForest = modelMap.get("chestPlateRightBiomeForest");
        leggingsBiomeForest = modelMap.get("leggingsBiomeForest");
        leggingsLeftBiomeForest = modelMap.get("leggingsLeftBiomeForest");
        leggingsMiddleBiomeForest = modelMap.get("leggingsMiddleBiomeForest");
        leggingsRightBiomeForest = modelMap.get("leggingsRightBiomeForest");
        bootsLeftBiomeForest = modelMap.get("bootsLeftBiomeForest");
        bootsRightBiomeForest = modelMap.get("bootsRightBiomeForest");

        biomeIsForest = maid.getAtBiomeBiome() === "forest";

        if (helmetBiomeForest != undefined) {
            helmetBiomeForest.setHidden(!biomeIsForest);
        }
        if (chestPlateBiomeForest != undefined) {
            chestPlateBiomeForest.setHidden(!biomeIsForest);
        }
        if (chestPlateLeftBiomeForest != undefined) {
            chestPlateLeftBiomeForest.setHidden(!biomeIsForest);
        }
        if (chestPlateMiddleBiomeForest != undefined) {
            chestPlateMiddleBiomeForest.setHidden(!biomeIsForest);
        }
        if (chestPlateRightBiomeForest != undefined) {
            chestPlateRightBiomeForest.setHidden(!biomeIsForest);
        }
        if (leggingsBiomeForest != undefined) {
            leggingsBiomeForest.setHidden(!biomeIsForest);
        }
        if (leggingsLeftBiomeForest != undefined) {
            leggingsLeftBiomeForest.setHidden(!biomeIsForest);
        }
        if (leggingsMiddleBiomeForest != undefined) {
            leggingsMiddleBiomeForest.setHidden(!biomeIsForest);
        }
        if (leggingsRightBiomeForest != undefined) {
            leggingsRightBiomeForest.setHidden(!biomeIsForest);
        }
        if (bootsLeftBiomeForest != undefined) {
            bootsLeftBiomeForest.setHidden(!biomeIsForest);
        }
        if (bootsRightBiomeForest != undefined) {
            bootsRightBiomeForest.setHidden(!biomeIsForest);
        }
    }
})

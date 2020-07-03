Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeExtremeHillsWithTrees = modelMap.get("helmetBiomeExtremeHillsWithTrees");
        chestPlateBiomeExtremeHillsWithTrees = modelMap.get("chestPlateBiomeExtremeHillsWithTrees");
        chestPlateLeftBiomeExtremeHillsWithTrees = modelMap.get("chestPlateLeftBiomeExtremeHillsWithTrees");
        chestPlateMiddleBiomeExtremeHillsWithTrees = modelMap.get("chestPlateMiddleBiomeExtremeHillsWithTrees");
        chestPlateRightBiomeExtremeHillsWithTrees = modelMap.get("chestPlateRightBiomeExtremeHillsWithTrees");
        leggingsBiomeExtremeHillsWithTrees = modelMap.get("leggingsBiomeExtremeHillsWithTrees");
        leggingsLeftBiomeExtremeHillsWithTrees = modelMap.get("leggingsLeftBiomeExtremeHillsWithTrees");
        leggingsMiddleBiomeExtremeHillsWithTrees = modelMap.get("leggingsMiddleBiomeExtremeHillsWithTrees");
        leggingsRightBiomeExtremeHillsWithTrees = modelMap.get("leggingsRightBiomeExtremeHillsWithTrees");
        bootsLeftBiomeExtremeHillsWithTrees = modelMap.get("bootsLeftBiomeExtremeHillsWithTrees");
        bootsRightBiomeExtremeHillsWithTrees = modelMap.get("bootsRightBiomeExtremeHillsWithTrees");

        biomeIsExtremeHillsWithTrees = maid.getAtBiomeBiome() === "extreme_hills_with_trees";

        if (helmetBiomeExtremeHillsWithTrees != undefined) {
            helmetBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (chestPlateBiomeExtremeHillsWithTrees != undefined) {
            chestPlateBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (chestPlateLeftBiomeExtremeHillsWithTrees != undefined) {
            chestPlateLeftBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (chestPlateMiddleBiomeExtremeHillsWithTrees != undefined) {
            chestPlateMiddleBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (chestPlateRightBiomeExtremeHillsWithTrees != undefined) {
            chestPlateRightBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (leggingsBiomeExtremeHillsWithTrees != undefined) {
            leggingsBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (leggingsLeftBiomeExtremeHillsWithTrees != undefined) {
            leggingsLeftBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (leggingsMiddleBiomeExtremeHillsWithTrees != undefined) {
            leggingsMiddleBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (leggingsRightBiomeExtremeHillsWithTrees != undefined) {
            leggingsRightBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (bootsLeftBiomeExtremeHillsWithTrees != undefined) {
            bootsLeftBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
        if (bootsRightBiomeExtremeHillsWithTrees != undefined) {
            bootsRightBiomeExtremeHillsWithTrees.setHidden(!biomeIsExtremeHillsWithTrees);
        }
    }
})

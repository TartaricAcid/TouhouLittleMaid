Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedExtremeHillsWithTrees = modelMap.get("helmetBiomeMutatedExtremeHillsWithTrees");
        chestPlateBiomeMutatedExtremeHillsWithTrees = modelMap.get("chestPlateBiomeMutatedExtremeHillsWithTrees");
        chestPlateLeftBiomeMutatedExtremeHillsWithTrees = modelMap.get("chestPlateLeftBiomeMutatedExtremeHillsWithTrees");
        chestPlateMiddleBiomeMutatedExtremeHillsWithTrees = modelMap.get("chestPlateMiddleBiomeMutatedExtremeHillsWithTrees");
        chestPlateRightBiomeMutatedExtremeHillsWithTrees = modelMap.get("chestPlateRightBiomeMutatedExtremeHillsWithTrees");
        leggingsBiomeMutatedExtremeHillsWithTrees = modelMap.get("leggingsBiomeMutatedExtremeHillsWithTrees");
        leggingsLeftBiomeMutatedExtremeHillsWithTrees = modelMap.get("leggingsLeftBiomeMutatedExtremeHillsWithTrees");
        leggingsMiddleBiomeMutatedExtremeHillsWithTrees = modelMap.get("leggingsMiddleBiomeMutatedExtremeHillsWithTrees");
        leggingsRightBiomeMutatedExtremeHillsWithTrees = modelMap.get("leggingsRightBiomeMutatedExtremeHillsWithTrees");
        bootsLeftBiomeMutatedExtremeHillsWithTrees = modelMap.get("bootsLeftBiomeMutatedExtremeHillsWithTrees");
        bootsRightBiomeMutatedExtremeHillsWithTrees = modelMap.get("bootsRightBiomeMutatedExtremeHillsWithTrees");

        biomeIsMutatedExtremeHillsWithTrees = maid.getAtBiomeBiome() === "mutated_extreme_hills_with_trees";

        if (helmetBiomeMutatedExtremeHillsWithTrees != undefined) {
            helmetBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (chestPlateBiomeMutatedExtremeHillsWithTrees != undefined) {
            chestPlateBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (chestPlateLeftBiomeMutatedExtremeHillsWithTrees != undefined) {
            chestPlateLeftBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (chestPlateMiddleBiomeMutatedExtremeHillsWithTrees != undefined) {
            chestPlateMiddleBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (chestPlateRightBiomeMutatedExtremeHillsWithTrees != undefined) {
            chestPlateRightBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (leggingsBiomeMutatedExtremeHillsWithTrees != undefined) {
            leggingsBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (leggingsLeftBiomeMutatedExtremeHillsWithTrees != undefined) {
            leggingsLeftBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (leggingsMiddleBiomeMutatedExtremeHillsWithTrees != undefined) {
            leggingsMiddleBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (leggingsRightBiomeMutatedExtremeHillsWithTrees != undefined) {
            leggingsRightBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (bootsLeftBiomeMutatedExtremeHillsWithTrees != undefined) {
            bootsLeftBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
        if (bootsRightBiomeMutatedExtremeHillsWithTrees != undefined) {
            bootsRightBiomeMutatedExtremeHillsWithTrees.setHidden(!biomeIsMutatedExtremeHillsWithTrees);
        }
    }
})

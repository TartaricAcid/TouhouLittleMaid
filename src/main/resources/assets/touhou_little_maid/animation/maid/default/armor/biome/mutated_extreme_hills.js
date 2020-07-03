Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedExtremeHills = modelMap.get("helmetBiomeMutatedExtremeHills");
        chestPlateBiomeMutatedExtremeHills = modelMap.get("chestPlateBiomeMutatedExtremeHills");
        chestPlateLeftBiomeMutatedExtremeHills = modelMap.get("chestPlateLeftBiomeMutatedExtremeHills");
        chestPlateMiddleBiomeMutatedExtremeHills = modelMap.get("chestPlateMiddleBiomeMutatedExtremeHills");
        chestPlateRightBiomeMutatedExtremeHills = modelMap.get("chestPlateRightBiomeMutatedExtremeHills");
        leggingsBiomeMutatedExtremeHills = modelMap.get("leggingsBiomeMutatedExtremeHills");
        leggingsLeftBiomeMutatedExtremeHills = modelMap.get("leggingsLeftBiomeMutatedExtremeHills");
        leggingsMiddleBiomeMutatedExtremeHills = modelMap.get("leggingsMiddleBiomeMutatedExtremeHills");
        leggingsRightBiomeMutatedExtremeHills = modelMap.get("leggingsRightBiomeMutatedExtremeHills");
        bootsLeftBiomeMutatedExtremeHills = modelMap.get("bootsLeftBiomeMutatedExtremeHills");
        bootsRightBiomeMutatedExtremeHills = modelMap.get("bootsRightBiomeMutatedExtremeHills");

        biomeIsMutatedExtremeHills = maid.getAtBiomeBiome() === "mutated_extreme_hills";

        if (helmetBiomeMutatedExtremeHills != undefined) {
            helmetBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (chestPlateBiomeMutatedExtremeHills != undefined) {
            chestPlateBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (chestPlateLeftBiomeMutatedExtremeHills != undefined) {
            chestPlateLeftBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (chestPlateMiddleBiomeMutatedExtremeHills != undefined) {
            chestPlateMiddleBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (chestPlateRightBiomeMutatedExtremeHills != undefined) {
            chestPlateRightBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (leggingsBiomeMutatedExtremeHills != undefined) {
            leggingsBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (leggingsLeftBiomeMutatedExtremeHills != undefined) {
            leggingsLeftBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (leggingsMiddleBiomeMutatedExtremeHills != undefined) {
            leggingsMiddleBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (leggingsRightBiomeMutatedExtremeHills != undefined) {
            leggingsRightBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (bootsLeftBiomeMutatedExtremeHills != undefined) {
            bootsLeftBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
        if (bootsRightBiomeMutatedExtremeHills != undefined) {
            bootsRightBiomeMutatedExtremeHills.setHidden(!biomeIsMutatedExtremeHills);
        }
    }
})

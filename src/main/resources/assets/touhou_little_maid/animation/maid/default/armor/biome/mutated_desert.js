Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedDesert = modelMap.get("helmetBiomeMutatedDesert");
        chestPlateBiomeMutatedDesert = modelMap.get("chestPlateBiomeMutatedDesert");
        chestPlateLeftBiomeMutatedDesert = modelMap.get("chestPlateLeftBiomeMutatedDesert");
        chestPlateMiddleBiomeMutatedDesert = modelMap.get("chestPlateMiddleBiomeMutatedDesert");
        chestPlateRightBiomeMutatedDesert = modelMap.get("chestPlateRightBiomeMutatedDesert");
        leggingsBiomeMutatedDesert = modelMap.get("leggingsBiomeMutatedDesert");
        leggingsLeftBiomeMutatedDesert = modelMap.get("leggingsLeftBiomeMutatedDesert");
        leggingsMiddleBiomeMutatedDesert = modelMap.get("leggingsMiddleBiomeMutatedDesert");
        leggingsRightBiomeMutatedDesert = modelMap.get("leggingsRightBiomeMutatedDesert");
        bootsLeftBiomeMutatedDesert = modelMap.get("bootsLeftBiomeMutatedDesert");
        bootsRightBiomeMutatedDesert = modelMap.get("bootsRightBiomeMutatedDesert");

        biomeIsMutatedDesert = maid.getAtBiomeBiome() === "mutated_desert";

        if (helmetBiomeMutatedDesert != undefined) {
            helmetBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (chestPlateBiomeMutatedDesert != undefined) {
            chestPlateBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (chestPlateLeftBiomeMutatedDesert != undefined) {
            chestPlateLeftBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (chestPlateMiddleBiomeMutatedDesert != undefined) {
            chestPlateMiddleBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (chestPlateRightBiomeMutatedDesert != undefined) {
            chestPlateRightBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (leggingsBiomeMutatedDesert != undefined) {
            leggingsBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (leggingsLeftBiomeMutatedDesert != undefined) {
            leggingsLeftBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (leggingsMiddleBiomeMutatedDesert != undefined) {
            leggingsMiddleBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (leggingsRightBiomeMutatedDesert != undefined) {
            leggingsRightBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (bootsLeftBiomeMutatedDesert != undefined) {
            bootsLeftBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
        if (bootsRightBiomeMutatedDesert != undefined) {
            bootsRightBiomeMutatedDesert.setHidden(!biomeIsMutatedDesert);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedSavanna = modelMap.get("helmetBiomeMutatedSavanna");
        chestPlateBiomeMutatedSavanna = modelMap.get("chestPlateBiomeMutatedSavanna");
        chestPlateLeftBiomeMutatedSavanna = modelMap.get("chestPlateLeftBiomeMutatedSavanna");
        chestPlateMiddleBiomeMutatedSavanna = modelMap.get("chestPlateMiddleBiomeMutatedSavanna");
        chestPlateRightBiomeMutatedSavanna = modelMap.get("chestPlateRightBiomeMutatedSavanna");
        leggingsBiomeMutatedSavanna = modelMap.get("leggingsBiomeMutatedSavanna");
        leggingsLeftBiomeMutatedSavanna = modelMap.get("leggingsLeftBiomeMutatedSavanna");
        leggingsMiddleBiomeMutatedSavanna = modelMap.get("leggingsMiddleBiomeMutatedSavanna");
        leggingsRightBiomeMutatedSavanna = modelMap.get("leggingsRightBiomeMutatedSavanna");
        bootsLeftBiomeMutatedSavanna = modelMap.get("bootsLeftBiomeMutatedSavanna");
        bootsRightBiomeMutatedSavanna = modelMap.get("bootsRightBiomeMutatedSavanna");

        biomeIsMutatedSavanna = maid.getAtBiomeBiome() === "mutated_savanna";

        if (helmetBiomeMutatedSavanna != undefined) {
            helmetBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (chestPlateBiomeMutatedSavanna != undefined) {
            chestPlateBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (chestPlateLeftBiomeMutatedSavanna != undefined) {
            chestPlateLeftBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (chestPlateMiddleBiomeMutatedSavanna != undefined) {
            chestPlateMiddleBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (chestPlateRightBiomeMutatedSavanna != undefined) {
            chestPlateRightBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (leggingsBiomeMutatedSavanna != undefined) {
            leggingsBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (leggingsLeftBiomeMutatedSavanna != undefined) {
            leggingsLeftBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (leggingsMiddleBiomeMutatedSavanna != undefined) {
            leggingsMiddleBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (leggingsRightBiomeMutatedSavanna != undefined) {
            leggingsRightBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (bootsLeftBiomeMutatedSavanna != undefined) {
            bootsLeftBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
        if (bootsRightBiomeMutatedSavanna != undefined) {
            bootsRightBiomeMutatedSavanna.setHidden(!biomeIsMutatedSavanna);
        }
    }
})

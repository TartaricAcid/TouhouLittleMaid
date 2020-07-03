Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedTaigaCold = modelMap.get("helmetBiomeMutatedTaigaCold");
        chestPlateBiomeMutatedTaigaCold = modelMap.get("chestPlateBiomeMutatedTaigaCold");
        chestPlateLeftBiomeMutatedTaigaCold = modelMap.get("chestPlateLeftBiomeMutatedTaigaCold");
        chestPlateMiddleBiomeMutatedTaigaCold = modelMap.get("chestPlateMiddleBiomeMutatedTaigaCold");
        chestPlateRightBiomeMutatedTaigaCold = modelMap.get("chestPlateRightBiomeMutatedTaigaCold");
        leggingsBiomeMutatedTaigaCold = modelMap.get("leggingsBiomeMutatedTaigaCold");
        leggingsLeftBiomeMutatedTaigaCold = modelMap.get("leggingsLeftBiomeMutatedTaigaCold");
        leggingsMiddleBiomeMutatedTaigaCold = modelMap.get("leggingsMiddleBiomeMutatedTaigaCold");
        leggingsRightBiomeMutatedTaigaCold = modelMap.get("leggingsRightBiomeMutatedTaigaCold");
        bootsLeftBiomeMutatedTaigaCold = modelMap.get("bootsLeftBiomeMutatedTaigaCold");
        bootsRightBiomeMutatedTaigaCold = modelMap.get("bootsRightBiomeMutatedTaigaCold");

        biomeIsMutatedTaigaCold = maid.getAtBiomeBiome() === "mutated_taiga_cold";

        if (helmetBiomeMutatedTaigaCold != undefined) {
            helmetBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (chestPlateBiomeMutatedTaigaCold != undefined) {
            chestPlateBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (chestPlateLeftBiomeMutatedTaigaCold != undefined) {
            chestPlateLeftBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (chestPlateMiddleBiomeMutatedTaigaCold != undefined) {
            chestPlateMiddleBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (chestPlateRightBiomeMutatedTaigaCold != undefined) {
            chestPlateRightBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (leggingsBiomeMutatedTaigaCold != undefined) {
            leggingsBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (leggingsLeftBiomeMutatedTaigaCold != undefined) {
            leggingsLeftBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (leggingsMiddleBiomeMutatedTaigaCold != undefined) {
            leggingsMiddleBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (leggingsRightBiomeMutatedTaigaCold != undefined) {
            leggingsRightBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (bootsLeftBiomeMutatedTaigaCold != undefined) {
            bootsLeftBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
        if (bootsRightBiomeMutatedTaigaCold != undefined) {
            bootsRightBiomeMutatedTaigaCold.setHidden(!biomeIsMutatedTaigaCold);
        }
    }
})

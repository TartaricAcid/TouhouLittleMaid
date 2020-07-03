Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedJungle = modelMap.get("helmetBiomeMutatedJungle");
        chestPlateBiomeMutatedJungle = modelMap.get("chestPlateBiomeMutatedJungle");
        chestPlateLeftBiomeMutatedJungle = modelMap.get("chestPlateLeftBiomeMutatedJungle");
        chestPlateMiddleBiomeMutatedJungle = modelMap.get("chestPlateMiddleBiomeMutatedJungle");
        chestPlateRightBiomeMutatedJungle = modelMap.get("chestPlateRightBiomeMutatedJungle");
        leggingsBiomeMutatedJungle = modelMap.get("leggingsBiomeMutatedJungle");
        leggingsLeftBiomeMutatedJungle = modelMap.get("leggingsLeftBiomeMutatedJungle");
        leggingsMiddleBiomeMutatedJungle = modelMap.get("leggingsMiddleBiomeMutatedJungle");
        leggingsRightBiomeMutatedJungle = modelMap.get("leggingsRightBiomeMutatedJungle");
        bootsLeftBiomeMutatedJungle = modelMap.get("bootsLeftBiomeMutatedJungle");
        bootsRightBiomeMutatedJungle = modelMap.get("bootsRightBiomeMutatedJungle");

        biomeIsMutatedJungle = maid.getAtBiomeBiome() === "mutated_jungle";

        if (helmetBiomeMutatedJungle != undefined) {
            helmetBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (chestPlateBiomeMutatedJungle != undefined) {
            chestPlateBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (chestPlateLeftBiomeMutatedJungle != undefined) {
            chestPlateLeftBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (chestPlateMiddleBiomeMutatedJungle != undefined) {
            chestPlateMiddleBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (chestPlateRightBiomeMutatedJungle != undefined) {
            chestPlateRightBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (leggingsBiomeMutatedJungle != undefined) {
            leggingsBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (leggingsLeftBiomeMutatedJungle != undefined) {
            leggingsLeftBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (leggingsMiddleBiomeMutatedJungle != undefined) {
            leggingsMiddleBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (leggingsRightBiomeMutatedJungle != undefined) {
            leggingsRightBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (bootsLeftBiomeMutatedJungle != undefined) {
            bootsLeftBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
        if (bootsRightBiomeMutatedJungle != undefined) {
            bootsRightBiomeMutatedJungle.setHidden(!biomeIsMutatedJungle);
        }
    }
})

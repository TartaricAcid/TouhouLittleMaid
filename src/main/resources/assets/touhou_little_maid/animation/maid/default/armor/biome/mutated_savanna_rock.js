Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedSavannaRock = modelMap.get("helmetBiomeMutatedSavannaRock");
        chestPlateBiomeMutatedSavannaRock = modelMap.get("chestPlateBiomeMutatedSavannaRock");
        chestPlateLeftBiomeMutatedSavannaRock = modelMap.get("chestPlateLeftBiomeMutatedSavannaRock");
        chestPlateMiddleBiomeMutatedSavannaRock = modelMap.get("chestPlateMiddleBiomeMutatedSavannaRock");
        chestPlateRightBiomeMutatedSavannaRock = modelMap.get("chestPlateRightBiomeMutatedSavannaRock");
        leggingsBiomeMutatedSavannaRock = modelMap.get("leggingsBiomeMutatedSavannaRock");
        leggingsLeftBiomeMutatedSavannaRock = modelMap.get("leggingsLeftBiomeMutatedSavannaRock");
        leggingsMiddleBiomeMutatedSavannaRock = modelMap.get("leggingsMiddleBiomeMutatedSavannaRock");
        leggingsRightBiomeMutatedSavannaRock = modelMap.get("leggingsRightBiomeMutatedSavannaRock");
        bootsLeftBiomeMutatedSavannaRock = modelMap.get("bootsLeftBiomeMutatedSavannaRock");
        bootsRightBiomeMutatedSavannaRock = modelMap.get("bootsRightBiomeMutatedSavannaRock");

        biomeIsMutatedSavannaRock = maid.getAtBiomeBiome() === "mutated_savanna_rock";

        if (helmetBiomeMutatedSavannaRock != undefined) {
            helmetBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (chestPlateBiomeMutatedSavannaRock != undefined) {
            chestPlateBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (chestPlateLeftBiomeMutatedSavannaRock != undefined) {
            chestPlateLeftBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (chestPlateMiddleBiomeMutatedSavannaRock != undefined) {
            chestPlateMiddleBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (chestPlateRightBiomeMutatedSavannaRock != undefined) {
            chestPlateRightBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (leggingsBiomeMutatedSavannaRock != undefined) {
            leggingsBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (leggingsLeftBiomeMutatedSavannaRock != undefined) {
            leggingsLeftBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (leggingsMiddleBiomeMutatedSavannaRock != undefined) {
            leggingsMiddleBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (leggingsRightBiomeMutatedSavannaRock != undefined) {
            leggingsRightBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (bootsLeftBiomeMutatedSavannaRock != undefined) {
            bootsLeftBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
        if (bootsRightBiomeMutatedSavannaRock != undefined) {
            bootsRightBiomeMutatedSavannaRock.setHidden(!biomeIsMutatedSavannaRock);
        }
    }
})

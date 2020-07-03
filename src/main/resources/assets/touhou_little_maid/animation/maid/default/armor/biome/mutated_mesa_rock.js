Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedMesaRock = modelMap.get("helmetBiomeMutatedMesaRock");
        chestPlateBiomeMutatedMesaRock = modelMap.get("chestPlateBiomeMutatedMesaRock");
        chestPlateLeftBiomeMutatedMesaRock = modelMap.get("chestPlateLeftBiomeMutatedMesaRock");
        chestPlateMiddleBiomeMutatedMesaRock = modelMap.get("chestPlateMiddleBiomeMutatedMesaRock");
        chestPlateRightBiomeMutatedMesaRock = modelMap.get("chestPlateRightBiomeMutatedMesaRock");
        leggingsBiomeMutatedMesaRock = modelMap.get("leggingsBiomeMutatedMesaRock");
        leggingsLeftBiomeMutatedMesaRock = modelMap.get("leggingsLeftBiomeMutatedMesaRock");
        leggingsMiddleBiomeMutatedMesaRock = modelMap.get("leggingsMiddleBiomeMutatedMesaRock");
        leggingsRightBiomeMutatedMesaRock = modelMap.get("leggingsRightBiomeMutatedMesaRock");
        bootsLeftBiomeMutatedMesaRock = modelMap.get("bootsLeftBiomeMutatedMesaRock");
        bootsRightBiomeMutatedMesaRock = modelMap.get("bootsRightBiomeMutatedMesaRock");

        biomeIsMutatedMesaRock = maid.getAtBiomeBiome() === "mutated_mesa_rock";

        if (helmetBiomeMutatedMesaRock != undefined) {
            helmetBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (chestPlateBiomeMutatedMesaRock != undefined) {
            chestPlateBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (chestPlateLeftBiomeMutatedMesaRock != undefined) {
            chestPlateLeftBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (chestPlateMiddleBiomeMutatedMesaRock != undefined) {
            chestPlateMiddleBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (chestPlateRightBiomeMutatedMesaRock != undefined) {
            chestPlateRightBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (leggingsBiomeMutatedMesaRock != undefined) {
            leggingsBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (leggingsLeftBiomeMutatedMesaRock != undefined) {
            leggingsLeftBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (leggingsMiddleBiomeMutatedMesaRock != undefined) {
            leggingsMiddleBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (leggingsRightBiomeMutatedMesaRock != undefined) {
            leggingsRightBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (bootsLeftBiomeMutatedMesaRock != undefined) {
            bootsLeftBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
        if (bootsRightBiomeMutatedMesaRock != undefined) {
            bootsRightBiomeMutatedMesaRock.setHidden(!biomeIsMutatedMesaRock);
        }
    }
})

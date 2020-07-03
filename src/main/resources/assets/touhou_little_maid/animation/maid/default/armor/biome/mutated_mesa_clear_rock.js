Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedMesaClearRock = modelMap.get("helmetBiomeMutatedMesaClearRock");
        chestPlateBiomeMutatedMesaClearRock = modelMap.get("chestPlateBiomeMutatedMesaClearRock");
        chestPlateLeftBiomeMutatedMesaClearRock = modelMap.get("chestPlateLeftBiomeMutatedMesaClearRock");
        chestPlateMiddleBiomeMutatedMesaClearRock = modelMap.get("chestPlateMiddleBiomeMutatedMesaClearRock");
        chestPlateRightBiomeMutatedMesaClearRock = modelMap.get("chestPlateRightBiomeMutatedMesaClearRock");
        leggingsBiomeMutatedMesaClearRock = modelMap.get("leggingsBiomeMutatedMesaClearRock");
        leggingsLeftBiomeMutatedMesaClearRock = modelMap.get("leggingsLeftBiomeMutatedMesaClearRock");
        leggingsMiddleBiomeMutatedMesaClearRock = modelMap.get("leggingsMiddleBiomeMutatedMesaClearRock");
        leggingsRightBiomeMutatedMesaClearRock = modelMap.get("leggingsRightBiomeMutatedMesaClearRock");
        bootsLeftBiomeMutatedMesaClearRock = modelMap.get("bootsLeftBiomeMutatedMesaClearRock");
        bootsRightBiomeMutatedMesaClearRock = modelMap.get("bootsRightBiomeMutatedMesaClearRock");

        biomeIsMutatedMesaClearRock = maid.getAtBiomeBiome() === "mutated_mesa_clear_rock";

        if (helmetBiomeMutatedMesaClearRock != undefined) {
            helmetBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (chestPlateBiomeMutatedMesaClearRock != undefined) {
            chestPlateBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (chestPlateLeftBiomeMutatedMesaClearRock != undefined) {
            chestPlateLeftBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (chestPlateMiddleBiomeMutatedMesaClearRock != undefined) {
            chestPlateMiddleBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (chestPlateRightBiomeMutatedMesaClearRock != undefined) {
            chestPlateRightBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (leggingsBiomeMutatedMesaClearRock != undefined) {
            leggingsBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (leggingsLeftBiomeMutatedMesaClearRock != undefined) {
            leggingsLeftBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (leggingsMiddleBiomeMutatedMesaClearRock != undefined) {
            leggingsMiddleBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (leggingsRightBiomeMutatedMesaClearRock != undefined) {
            leggingsRightBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (bootsLeftBiomeMutatedMesaClearRock != undefined) {
            bootsLeftBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
        if (bootsRightBiomeMutatedMesaClearRock != undefined) {
            bootsRightBiomeMutatedMesaClearRock.setHidden(!biomeIsMutatedMesaClearRock);
        }
    }
})

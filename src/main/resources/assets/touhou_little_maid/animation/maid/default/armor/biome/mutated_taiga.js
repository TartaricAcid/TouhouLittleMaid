Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedTaiga = modelMap.get("helmetBiomeMutatedTaiga");
        chestPlateBiomeMutatedTaiga = modelMap.get("chestPlateBiomeMutatedTaiga");
        chestPlateLeftBiomeMutatedTaiga = modelMap.get("chestPlateLeftBiomeMutatedTaiga");
        chestPlateMiddleBiomeMutatedTaiga = modelMap.get("chestPlateMiddleBiomeMutatedTaiga");
        chestPlateRightBiomeMutatedTaiga = modelMap.get("chestPlateRightBiomeMutatedTaiga");
        leggingsBiomeMutatedTaiga = modelMap.get("leggingsBiomeMutatedTaiga");
        leggingsLeftBiomeMutatedTaiga = modelMap.get("leggingsLeftBiomeMutatedTaiga");
        leggingsMiddleBiomeMutatedTaiga = modelMap.get("leggingsMiddleBiomeMutatedTaiga");
        leggingsRightBiomeMutatedTaiga = modelMap.get("leggingsRightBiomeMutatedTaiga");
        bootsLeftBiomeMutatedTaiga = modelMap.get("bootsLeftBiomeMutatedTaiga");
        bootsRightBiomeMutatedTaiga = modelMap.get("bootsRightBiomeMutatedTaiga");

        biomeIsMutatedTaiga = maid.getAtBiomeBiome() === "mutated_taiga";

        if (helmetBiomeMutatedTaiga != undefined) {
            helmetBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (chestPlateBiomeMutatedTaiga != undefined) {
            chestPlateBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (chestPlateLeftBiomeMutatedTaiga != undefined) {
            chestPlateLeftBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (chestPlateMiddleBiomeMutatedTaiga != undefined) {
            chestPlateMiddleBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (chestPlateRightBiomeMutatedTaiga != undefined) {
            chestPlateRightBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (leggingsBiomeMutatedTaiga != undefined) {
            leggingsBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (leggingsLeftBiomeMutatedTaiga != undefined) {
            leggingsLeftBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (leggingsMiddleBiomeMutatedTaiga != undefined) {
            leggingsMiddleBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (leggingsRightBiomeMutatedTaiga != undefined) {
            leggingsRightBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (bootsLeftBiomeMutatedTaiga != undefined) {
            bootsLeftBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
        if (bootsRightBiomeMutatedTaiga != undefined) {
            bootsRightBiomeMutatedTaiga.setHidden(!biomeIsMutatedTaiga);
        }
    }
})

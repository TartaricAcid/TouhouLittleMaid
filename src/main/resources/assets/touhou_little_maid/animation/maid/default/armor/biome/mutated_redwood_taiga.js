Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedRedwoodTaiga = modelMap.get("helmetBiomeMutatedRedwoodTaiga");
        chestPlateBiomeMutatedRedwoodTaiga = modelMap.get("chestPlateBiomeMutatedRedwoodTaiga");
        chestPlateLeftBiomeMutatedRedwoodTaiga = modelMap.get("chestPlateLeftBiomeMutatedRedwoodTaiga");
        chestPlateMiddleBiomeMutatedRedwoodTaiga = modelMap.get("chestPlateMiddleBiomeMutatedRedwoodTaiga");
        chestPlateRightBiomeMutatedRedwoodTaiga = modelMap.get("chestPlateRightBiomeMutatedRedwoodTaiga");
        leggingsBiomeMutatedRedwoodTaiga = modelMap.get("leggingsBiomeMutatedRedwoodTaiga");
        leggingsLeftBiomeMutatedRedwoodTaiga = modelMap.get("leggingsLeftBiomeMutatedRedwoodTaiga");
        leggingsMiddleBiomeMutatedRedwoodTaiga = modelMap.get("leggingsMiddleBiomeMutatedRedwoodTaiga");
        leggingsRightBiomeMutatedRedwoodTaiga = modelMap.get("leggingsRightBiomeMutatedRedwoodTaiga");
        bootsLeftBiomeMutatedRedwoodTaiga = modelMap.get("bootsLeftBiomeMutatedRedwoodTaiga");
        bootsRightBiomeMutatedRedwoodTaiga = modelMap.get("bootsRightBiomeMutatedRedwoodTaiga");

        biomeIsMutatedRedwoodTaiga = maid.getAtBiomeBiome() === "mutated_redwood_taiga";

        if (helmetBiomeMutatedRedwoodTaiga != undefined) {
            helmetBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (chestPlateBiomeMutatedRedwoodTaiga != undefined) {
            chestPlateBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (chestPlateLeftBiomeMutatedRedwoodTaiga != undefined) {
            chestPlateLeftBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (chestPlateMiddleBiomeMutatedRedwoodTaiga != undefined) {
            chestPlateMiddleBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (chestPlateRightBiomeMutatedRedwoodTaiga != undefined) {
            chestPlateRightBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (leggingsBiomeMutatedRedwoodTaiga != undefined) {
            leggingsBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (leggingsLeftBiomeMutatedRedwoodTaiga != undefined) {
            leggingsLeftBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (leggingsMiddleBiomeMutatedRedwoodTaiga != undefined) {
            leggingsMiddleBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (leggingsRightBiomeMutatedRedwoodTaiga != undefined) {
            leggingsRightBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (bootsLeftBiomeMutatedRedwoodTaiga != undefined) {
            bootsLeftBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
        if (bootsRightBiomeMutatedRedwoodTaiga != undefined) {
            bootsRightBiomeMutatedRedwoodTaiga.setHidden(!biomeIsMutatedRedwoodTaiga);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedPlains = modelMap.get("helmetBiomeMutatedPlains");
        chestPlateBiomeMutatedPlains = modelMap.get("chestPlateBiomeMutatedPlains");
        chestPlateLeftBiomeMutatedPlains = modelMap.get("chestPlateLeftBiomeMutatedPlains");
        chestPlateMiddleBiomeMutatedPlains = modelMap.get("chestPlateMiddleBiomeMutatedPlains");
        chestPlateRightBiomeMutatedPlains = modelMap.get("chestPlateRightBiomeMutatedPlains");
        leggingsBiomeMutatedPlains = modelMap.get("leggingsBiomeMutatedPlains");
        leggingsLeftBiomeMutatedPlains = modelMap.get("leggingsLeftBiomeMutatedPlains");
        leggingsMiddleBiomeMutatedPlains = modelMap.get("leggingsMiddleBiomeMutatedPlains");
        leggingsRightBiomeMutatedPlains = modelMap.get("leggingsRightBiomeMutatedPlains");
        bootsLeftBiomeMutatedPlains = modelMap.get("bootsLeftBiomeMutatedPlains");
        bootsRightBiomeMutatedPlains = modelMap.get("bootsRightBiomeMutatedPlains");

        biomeIsMutatedPlains = maid.getAtBiomeBiome() === "mutated_plains";

        if (helmetBiomeMutatedPlains != undefined) {
            helmetBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (chestPlateBiomeMutatedPlains != undefined) {
            chestPlateBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (chestPlateLeftBiomeMutatedPlains != undefined) {
            chestPlateLeftBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (chestPlateMiddleBiomeMutatedPlains != undefined) {
            chestPlateMiddleBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (chestPlateRightBiomeMutatedPlains != undefined) {
            chestPlateRightBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (leggingsBiomeMutatedPlains != undefined) {
            leggingsBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (leggingsLeftBiomeMutatedPlains != undefined) {
            leggingsLeftBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (leggingsMiddleBiomeMutatedPlains != undefined) {
            leggingsMiddleBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (leggingsRightBiomeMutatedPlains != undefined) {
            leggingsRightBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (bootsLeftBiomeMutatedPlains != undefined) {
            bootsLeftBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
        if (bootsRightBiomeMutatedPlains != undefined) {
            bootsRightBiomeMutatedPlains.setHidden(!biomeIsMutatedPlains);
        }
    }
})

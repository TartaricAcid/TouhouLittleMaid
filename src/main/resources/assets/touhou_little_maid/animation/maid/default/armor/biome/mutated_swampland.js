Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedSwampland = modelMap.get("helmetBiomeMutatedSwampland");
        chestPlateBiomeMutatedSwampland = modelMap.get("chestPlateBiomeMutatedSwampland");
        chestPlateLeftBiomeMutatedSwampland = modelMap.get("chestPlateLeftBiomeMutatedSwampland");
        chestPlateMiddleBiomeMutatedSwampland = modelMap.get("chestPlateMiddleBiomeMutatedSwampland");
        chestPlateRightBiomeMutatedSwampland = modelMap.get("chestPlateRightBiomeMutatedSwampland");
        leggingsBiomeMutatedSwampland = modelMap.get("leggingsBiomeMutatedSwampland");
        leggingsLeftBiomeMutatedSwampland = modelMap.get("leggingsLeftBiomeMutatedSwampland");
        leggingsMiddleBiomeMutatedSwampland = modelMap.get("leggingsMiddleBiomeMutatedSwampland");
        leggingsRightBiomeMutatedSwampland = modelMap.get("leggingsRightBiomeMutatedSwampland");
        bootsLeftBiomeMutatedSwampland = modelMap.get("bootsLeftBiomeMutatedSwampland");
        bootsRightBiomeMutatedSwampland = modelMap.get("bootsRightBiomeMutatedSwampland");

        biomeIsMutatedSwampland = maid.getAtBiomeBiome() === "mutated_swampland";

        if (helmetBiomeMutatedSwampland != undefined) {
            helmetBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (chestPlateBiomeMutatedSwampland != undefined) {
            chestPlateBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (chestPlateLeftBiomeMutatedSwampland != undefined) {
            chestPlateLeftBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (chestPlateMiddleBiomeMutatedSwampland != undefined) {
            chestPlateMiddleBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (chestPlateRightBiomeMutatedSwampland != undefined) {
            chestPlateRightBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (leggingsBiomeMutatedSwampland != undefined) {
            leggingsBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (leggingsLeftBiomeMutatedSwampland != undefined) {
            leggingsLeftBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (leggingsMiddleBiomeMutatedSwampland != undefined) {
            leggingsMiddleBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (leggingsRightBiomeMutatedSwampland != undefined) {
            leggingsRightBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (bootsLeftBiomeMutatedSwampland != undefined) {
            bootsLeftBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
        if (bootsRightBiomeMutatedSwampland != undefined) {
            bootsRightBiomeMutatedSwampland.setHidden(!biomeIsMutatedSwampland);
        }
    }
})

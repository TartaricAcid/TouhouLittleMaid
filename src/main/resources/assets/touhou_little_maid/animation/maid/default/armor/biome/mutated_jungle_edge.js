Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedJungleEdge = modelMap.get("helmetBiomeMutatedJungleEdge");
        chestPlateBiomeMutatedJungleEdge = modelMap.get("chestPlateBiomeMutatedJungleEdge");
        chestPlateLeftBiomeMutatedJungleEdge = modelMap.get("chestPlateLeftBiomeMutatedJungleEdge");
        chestPlateMiddleBiomeMutatedJungleEdge = modelMap.get("chestPlateMiddleBiomeMutatedJungleEdge");
        chestPlateRightBiomeMutatedJungleEdge = modelMap.get("chestPlateRightBiomeMutatedJungleEdge");
        leggingsBiomeMutatedJungleEdge = modelMap.get("leggingsBiomeMutatedJungleEdge");
        leggingsLeftBiomeMutatedJungleEdge = modelMap.get("leggingsLeftBiomeMutatedJungleEdge");
        leggingsMiddleBiomeMutatedJungleEdge = modelMap.get("leggingsMiddleBiomeMutatedJungleEdge");
        leggingsRightBiomeMutatedJungleEdge = modelMap.get("leggingsRightBiomeMutatedJungleEdge");
        bootsLeftBiomeMutatedJungleEdge = modelMap.get("bootsLeftBiomeMutatedJungleEdge");
        bootsRightBiomeMutatedJungleEdge = modelMap.get("bootsRightBiomeMutatedJungleEdge");

        biomeIsMutatedJungleEdge = maid.getAtBiomeBiome() === "mutated_jungle_edge";

        if (helmetBiomeMutatedJungleEdge != undefined) {
            helmetBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (chestPlateBiomeMutatedJungleEdge != undefined) {
            chestPlateBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (chestPlateLeftBiomeMutatedJungleEdge != undefined) {
            chestPlateLeftBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (chestPlateMiddleBiomeMutatedJungleEdge != undefined) {
            chestPlateMiddleBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (chestPlateRightBiomeMutatedJungleEdge != undefined) {
            chestPlateRightBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (leggingsBiomeMutatedJungleEdge != undefined) {
            leggingsBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (leggingsLeftBiomeMutatedJungleEdge != undefined) {
            leggingsLeftBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (leggingsMiddleBiomeMutatedJungleEdge != undefined) {
            leggingsMiddleBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (leggingsRightBiomeMutatedJungleEdge != undefined) {
            leggingsRightBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (bootsLeftBiomeMutatedJungleEdge != undefined) {
            bootsLeftBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
        if (bootsRightBiomeMutatedJungleEdge != undefined) {
            bootsRightBiomeMutatedJungleEdge.setHidden(!biomeIsMutatedJungleEdge);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeJungleEdge = modelMap.get("helmetBiomeJungleEdge");
        chestPlateBiomeJungleEdge = modelMap.get("chestPlateBiomeJungleEdge");
        chestPlateLeftBiomeJungleEdge = modelMap.get("chestPlateLeftBiomeJungleEdge");
        chestPlateMiddleBiomeJungleEdge = modelMap.get("chestPlateMiddleBiomeJungleEdge");
        chestPlateRightBiomeJungleEdge = modelMap.get("chestPlateRightBiomeJungleEdge");
        leggingsBiomeJungleEdge = modelMap.get("leggingsBiomeJungleEdge");
        leggingsLeftBiomeJungleEdge = modelMap.get("leggingsLeftBiomeJungleEdge");
        leggingsMiddleBiomeJungleEdge = modelMap.get("leggingsMiddleBiomeJungleEdge");
        leggingsRightBiomeJungleEdge = modelMap.get("leggingsRightBiomeJungleEdge");
        bootsLeftBiomeJungleEdge = modelMap.get("bootsLeftBiomeJungleEdge");
        bootsRightBiomeJungleEdge = modelMap.get("bootsRightBiomeJungleEdge");

        biomeIsJungleEdge = maid.getAtBiomeBiome() === "jungle_edge";

        if (helmetBiomeJungleEdge != undefined) {
            helmetBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (chestPlateBiomeJungleEdge != undefined) {
            chestPlateBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (chestPlateLeftBiomeJungleEdge != undefined) {
            chestPlateLeftBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (chestPlateMiddleBiomeJungleEdge != undefined) {
            chestPlateMiddleBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (chestPlateRightBiomeJungleEdge != undefined) {
            chestPlateRightBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (leggingsBiomeJungleEdge != undefined) {
            leggingsBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (leggingsLeftBiomeJungleEdge != undefined) {
            leggingsLeftBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (leggingsMiddleBiomeJungleEdge != undefined) {
            leggingsMiddleBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (leggingsRightBiomeJungleEdge != undefined) {
            leggingsRightBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (bootsLeftBiomeJungleEdge != undefined) {
            bootsLeftBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
        if (bootsRightBiomeJungleEdge != undefined) {
            bootsRightBiomeJungleEdge.setHidden(!biomeIsJungleEdge);
        }
    }
})

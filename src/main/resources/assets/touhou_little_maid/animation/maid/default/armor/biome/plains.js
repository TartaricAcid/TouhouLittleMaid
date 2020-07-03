Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomePlains = modelMap.get("helmetBiomePlains");
        chestPlateBiomePlains = modelMap.get("chestPlateBiomePlains");
        chestPlateLeftBiomePlains = modelMap.get("chestPlateLeftBiomePlains");
        chestPlateMiddleBiomePlains = modelMap.get("chestPlateMiddleBiomePlains");
        chestPlateRightBiomePlains = modelMap.get("chestPlateRightBiomePlains");
        leggingsBiomePlains = modelMap.get("leggingsBiomePlains");
        leggingsLeftBiomePlains = modelMap.get("leggingsLeftBiomePlains");
        leggingsMiddleBiomePlains = modelMap.get("leggingsMiddleBiomePlains");
        leggingsRightBiomePlains = modelMap.get("leggingsRightBiomePlains");
        bootsLeftBiomePlains = modelMap.get("bootsLeftBiomePlains");
        bootsRightBiomePlains = modelMap.get("bootsRightBiomePlains");

        biomeIsPlains = maid.getAtBiomeBiome() === "plains";

        if (helmetBiomePlains != undefined) {
            helmetBiomePlains.setHidden(!biomeIsPlains);
        }
        if (chestPlateBiomePlains != undefined) {
            chestPlateBiomePlains.setHidden(!biomeIsPlains);
        }
        if (chestPlateLeftBiomePlains != undefined) {
            chestPlateLeftBiomePlains.setHidden(!biomeIsPlains);
        }
        if (chestPlateMiddleBiomePlains != undefined) {
            chestPlateMiddleBiomePlains.setHidden(!biomeIsPlains);
        }
        if (chestPlateRightBiomePlains != undefined) {
            chestPlateRightBiomePlains.setHidden(!biomeIsPlains);
        }
        if (leggingsBiomePlains != undefined) {
            leggingsBiomePlains.setHidden(!biomeIsPlains);
        }
        if (leggingsLeftBiomePlains != undefined) {
            leggingsLeftBiomePlains.setHidden(!biomeIsPlains);
        }
        if (leggingsMiddleBiomePlains != undefined) {
            leggingsMiddleBiomePlains.setHidden(!biomeIsPlains);
        }
        if (leggingsRightBiomePlains != undefined) {
            leggingsRightBiomePlains.setHidden(!biomeIsPlains);
        }
        if (bootsLeftBiomePlains != undefined) {
            bootsLeftBiomePlains.setHidden(!biomeIsPlains);
        }
        if (bootsRightBiomePlains != undefined) {
            bootsRightBiomePlains.setHidden(!biomeIsPlains);
        }
    }
})

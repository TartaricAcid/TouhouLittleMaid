Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMesaRock = modelMap.get("helmetBiomeMesaRock");
        chestPlateBiomeMesaRock = modelMap.get("chestPlateBiomeMesaRock");
        chestPlateLeftBiomeMesaRock = modelMap.get("chestPlateLeftBiomeMesaRock");
        chestPlateMiddleBiomeMesaRock = modelMap.get("chestPlateMiddleBiomeMesaRock");
        chestPlateRightBiomeMesaRock = modelMap.get("chestPlateRightBiomeMesaRock");
        leggingsBiomeMesaRock = modelMap.get("leggingsBiomeMesaRock");
        leggingsLeftBiomeMesaRock = modelMap.get("leggingsLeftBiomeMesaRock");
        leggingsMiddleBiomeMesaRock = modelMap.get("leggingsMiddleBiomeMesaRock");
        leggingsRightBiomeMesaRock = modelMap.get("leggingsRightBiomeMesaRock");
        bootsLeftBiomeMesaRock = modelMap.get("bootsLeftBiomeMesaRock");
        bootsRightBiomeMesaRock = modelMap.get("bootsRightBiomeMesaRock");

        biomeIsMesaRock = maid.getAtBiomeBiome() === "mesa_rock";

        if (helmetBiomeMesaRock != undefined) {
            helmetBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (chestPlateBiomeMesaRock != undefined) {
            chestPlateBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (chestPlateLeftBiomeMesaRock != undefined) {
            chestPlateLeftBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (chestPlateMiddleBiomeMesaRock != undefined) {
            chestPlateMiddleBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (chestPlateRightBiomeMesaRock != undefined) {
            chestPlateRightBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (leggingsBiomeMesaRock != undefined) {
            leggingsBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (leggingsLeftBiomeMesaRock != undefined) {
            leggingsLeftBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (leggingsMiddleBiomeMesaRock != undefined) {
            leggingsMiddleBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (leggingsRightBiomeMesaRock != undefined) {
            leggingsRightBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (bootsLeftBiomeMesaRock != undefined) {
            bootsLeftBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
        if (bootsRightBiomeMesaRock != undefined) {
            bootsRightBiomeMesaRock.setHidden(!biomeIsMesaRock);
        }
    }
})

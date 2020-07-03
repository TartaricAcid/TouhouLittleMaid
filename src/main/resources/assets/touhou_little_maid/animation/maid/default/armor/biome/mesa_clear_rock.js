Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMesaClearRock = modelMap.get("helmetBiomeMesaClearRock");
        chestPlateBiomeMesaClearRock = modelMap.get("chestPlateBiomeMesaClearRock");
        chestPlateLeftBiomeMesaClearRock = modelMap.get("chestPlateLeftBiomeMesaClearRock");
        chestPlateMiddleBiomeMesaClearRock = modelMap.get("chestPlateMiddleBiomeMesaClearRock");
        chestPlateRightBiomeMesaClearRock = modelMap.get("chestPlateRightBiomeMesaClearRock");
        leggingsBiomeMesaClearRock = modelMap.get("leggingsBiomeMesaClearRock");
        leggingsLeftBiomeMesaClearRock = modelMap.get("leggingsLeftBiomeMesaClearRock");
        leggingsMiddleBiomeMesaClearRock = modelMap.get("leggingsMiddleBiomeMesaClearRock");
        leggingsRightBiomeMesaClearRock = modelMap.get("leggingsRightBiomeMesaClearRock");
        bootsLeftBiomeMesaClearRock = modelMap.get("bootsLeftBiomeMesaClearRock");
        bootsRightBiomeMesaClearRock = modelMap.get("bootsRightBiomeMesaClearRock");

        biomeIsMesaClearRock = maid.getAtBiomeBiome() === "mesa_clear_rock";

        if (helmetBiomeMesaClearRock != undefined) {
            helmetBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (chestPlateBiomeMesaClearRock != undefined) {
            chestPlateBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (chestPlateLeftBiomeMesaClearRock != undefined) {
            chestPlateLeftBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (chestPlateMiddleBiomeMesaClearRock != undefined) {
            chestPlateMiddleBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (chestPlateRightBiomeMesaClearRock != undefined) {
            chestPlateRightBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (leggingsBiomeMesaClearRock != undefined) {
            leggingsBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (leggingsLeftBiomeMesaClearRock != undefined) {
            leggingsLeftBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (leggingsMiddleBiomeMesaClearRock != undefined) {
            leggingsMiddleBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (leggingsRightBiomeMesaClearRock != undefined) {
            leggingsRightBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (bootsLeftBiomeMesaClearRock != undefined) {
            bootsLeftBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
        if (bootsRightBiomeMesaClearRock != undefined) {
            bootsRightBiomeMesaClearRock.setHidden(!biomeIsMesaClearRock);
        }
    }
})

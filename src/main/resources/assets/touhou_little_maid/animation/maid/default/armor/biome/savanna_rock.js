Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeSavannaRock = modelMap.get("helmetBiomeSavannaRock");
        chestPlateBiomeSavannaRock = modelMap.get("chestPlateBiomeSavannaRock");
        chestPlateLeftBiomeSavannaRock = modelMap.get("chestPlateLeftBiomeSavannaRock");
        chestPlateMiddleBiomeSavannaRock = modelMap.get("chestPlateMiddleBiomeSavannaRock");
        chestPlateRightBiomeSavannaRock = modelMap.get("chestPlateRightBiomeSavannaRock");
        leggingsBiomeSavannaRock = modelMap.get("leggingsBiomeSavannaRock");
        leggingsLeftBiomeSavannaRock = modelMap.get("leggingsLeftBiomeSavannaRock");
        leggingsMiddleBiomeSavannaRock = modelMap.get("leggingsMiddleBiomeSavannaRock");
        leggingsRightBiomeSavannaRock = modelMap.get("leggingsRightBiomeSavannaRock");
        bootsLeftBiomeSavannaRock = modelMap.get("bootsLeftBiomeSavannaRock");
        bootsRightBiomeSavannaRock = modelMap.get("bootsRightBiomeSavannaRock");

        biomeIsSavannaRock = maid.getAtBiomeBiome() === "savanna_rock";

        if (helmetBiomeSavannaRock != undefined) {
            helmetBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (chestPlateBiomeSavannaRock != undefined) {
            chestPlateBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (chestPlateLeftBiomeSavannaRock != undefined) {
            chestPlateLeftBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (chestPlateMiddleBiomeSavannaRock != undefined) {
            chestPlateMiddleBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (chestPlateRightBiomeSavannaRock != undefined) {
            chestPlateRightBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (leggingsBiomeSavannaRock != undefined) {
            leggingsBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (leggingsLeftBiomeSavannaRock != undefined) {
            leggingsLeftBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (leggingsMiddleBiomeSavannaRock != undefined) {
            leggingsMiddleBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (leggingsRightBiomeSavannaRock != undefined) {
            leggingsRightBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (bootsLeftBiomeSavannaRock != undefined) {
            bootsLeftBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
        if (bootsRightBiomeSavannaRock != undefined) {
            bootsRightBiomeSavannaRock.setHidden(!biomeIsSavannaRock);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeColdBeach = modelMap.get("helmetBiomeColdBeach");
        chestPlateBiomeColdBeach = modelMap.get("chestPlateBiomeColdBeach");
        chestPlateLeftBiomeColdBeach = modelMap.get("chestPlateLeftBiomeColdBeach");
        chestPlateMiddleBiomeColdBeach = modelMap.get("chestPlateMiddleBiomeColdBeach");
        chestPlateRightBiomeColdBeach = modelMap.get("chestPlateRightBiomeColdBeach");
        leggingsBiomeColdBeach = modelMap.get("leggingsBiomeColdBeach");
        leggingsLeftBiomeColdBeach = modelMap.get("leggingsLeftBiomeColdBeach");
        leggingsMiddleBiomeColdBeach = modelMap.get("leggingsMiddleBiomeColdBeach");
        leggingsRightBiomeColdBeach = modelMap.get("leggingsRightBiomeColdBeach");
        bootsLeftBiomeColdBeach = modelMap.get("bootsLeftBiomeColdBeach");
        bootsRightBiomeColdBeach = modelMap.get("bootsRightBiomeColdBeach");

        biomeIsColdBeach = maid.getAtBiomeBiome() === "cold_beach";

        if (helmetBiomeColdBeach != undefined) {
            helmetBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (chestPlateBiomeColdBeach != undefined) {
            chestPlateBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (chestPlateLeftBiomeColdBeach != undefined) {
            chestPlateLeftBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (chestPlateMiddleBiomeColdBeach != undefined) {
            chestPlateMiddleBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (chestPlateRightBiomeColdBeach != undefined) {
            chestPlateRightBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (leggingsBiomeColdBeach != undefined) {
            leggingsBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (leggingsLeftBiomeColdBeach != undefined) {
            leggingsLeftBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (leggingsMiddleBiomeColdBeach != undefined) {
            leggingsMiddleBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (leggingsRightBiomeColdBeach != undefined) {
            leggingsRightBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (bootsLeftBiomeColdBeach != undefined) {
            bootsLeftBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
        if (bootsRightBiomeColdBeach != undefined) {
            bootsRightBiomeColdBeach.setHidden(!biomeIsColdBeach);
        }
    }
})

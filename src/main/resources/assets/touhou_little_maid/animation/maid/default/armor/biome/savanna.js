Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeSavanna = modelMap.get("helmetBiomeSavanna");
        chestPlateBiomeSavanna = modelMap.get("chestPlateBiomeSavanna");
        chestPlateLeftBiomeSavanna = modelMap.get("chestPlateLeftBiomeSavanna");
        chestPlateMiddleBiomeSavanna = modelMap.get("chestPlateMiddleBiomeSavanna");
        chestPlateRightBiomeSavanna = modelMap.get("chestPlateRightBiomeSavanna");
        leggingsBiomeSavanna = modelMap.get("leggingsBiomeSavanna");
        leggingsLeftBiomeSavanna = modelMap.get("leggingsLeftBiomeSavanna");
        leggingsMiddleBiomeSavanna = modelMap.get("leggingsMiddleBiomeSavanna");
        leggingsRightBiomeSavanna = modelMap.get("leggingsRightBiomeSavanna");
        bootsLeftBiomeSavanna = modelMap.get("bootsLeftBiomeSavanna");
        bootsRightBiomeSavanna = modelMap.get("bootsRightBiomeSavanna");

        biomeIsSavanna = maid.getAtBiomeBiome() === "savanna";

        if (helmetBiomeSavanna != undefined) {
            helmetBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (chestPlateBiomeSavanna != undefined) {
            chestPlateBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (chestPlateLeftBiomeSavanna != undefined) {
            chestPlateLeftBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (chestPlateMiddleBiomeSavanna != undefined) {
            chestPlateMiddleBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (chestPlateRightBiomeSavanna != undefined) {
            chestPlateRightBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (leggingsBiomeSavanna != undefined) {
            leggingsBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (leggingsLeftBiomeSavanna != undefined) {
            leggingsLeftBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (leggingsMiddleBiomeSavanna != undefined) {
            leggingsMiddleBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (leggingsRightBiomeSavanna != undefined) {
            leggingsRightBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (bootsLeftBiomeSavanna != undefined) {
            bootsLeftBiomeSavanna.setHidden(!biomeIsSavanna);
        }
        if (bootsRightBiomeSavanna != undefined) {
            bootsRightBiomeSavanna.setHidden(!biomeIsSavanna);
        }
    }
})

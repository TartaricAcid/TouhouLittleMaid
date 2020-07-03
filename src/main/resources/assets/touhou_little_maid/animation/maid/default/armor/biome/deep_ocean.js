Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeDeepOcean = modelMap.get("helmetBiomeDeepOcean");
        chestPlateBiomeDeepOcean = modelMap.get("chestPlateBiomeDeepOcean");
        chestPlateLeftBiomeDeepOcean = modelMap.get("chestPlateLeftBiomeDeepOcean");
        chestPlateMiddleBiomeDeepOcean = modelMap.get("chestPlateMiddleBiomeDeepOcean");
        chestPlateRightBiomeDeepOcean = modelMap.get("chestPlateRightBiomeDeepOcean");
        leggingsBiomeDeepOcean = modelMap.get("leggingsBiomeDeepOcean");
        leggingsLeftBiomeDeepOcean = modelMap.get("leggingsLeftBiomeDeepOcean");
        leggingsMiddleBiomeDeepOcean = modelMap.get("leggingsMiddleBiomeDeepOcean");
        leggingsRightBiomeDeepOcean = modelMap.get("leggingsRightBiomeDeepOcean");
        bootsLeftBiomeDeepOcean = modelMap.get("bootsLeftBiomeDeepOcean");
        bootsRightBiomeDeepOcean = modelMap.get("bootsRightBiomeDeepOcean");

        biomeIsDeepOcean = maid.getAtBiomeBiome() === "deep_ocean";

        if (helmetBiomeDeepOcean != undefined) {
            helmetBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (chestPlateBiomeDeepOcean != undefined) {
            chestPlateBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (chestPlateLeftBiomeDeepOcean != undefined) {
            chestPlateLeftBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (chestPlateMiddleBiomeDeepOcean != undefined) {
            chestPlateMiddleBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (chestPlateRightBiomeDeepOcean != undefined) {
            chestPlateRightBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (leggingsBiomeDeepOcean != undefined) {
            leggingsBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (leggingsLeftBiomeDeepOcean != undefined) {
            leggingsLeftBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (leggingsMiddleBiomeDeepOcean != undefined) {
            leggingsMiddleBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (leggingsRightBiomeDeepOcean != undefined) {
            leggingsRightBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (bootsLeftBiomeDeepOcean != undefined) {
            bootsLeftBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
        if (bootsRightBiomeDeepOcean != undefined) {
            bootsRightBiomeDeepOcean.setHidden(!biomeIsDeepOcean);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeOcean = modelMap.get("helmetBiomeOcean");
        chestPlateBiomeOcean = modelMap.get("chestPlateBiomeOcean");
        chestPlateLeftBiomeOcean = modelMap.get("chestPlateLeftBiomeOcean");
        chestPlateMiddleBiomeOcean = modelMap.get("chestPlateMiddleBiomeOcean");
        chestPlateRightBiomeOcean = modelMap.get("chestPlateRightBiomeOcean");
        leggingsBiomeOcean = modelMap.get("leggingsBiomeOcean");
        leggingsLeftBiomeOcean = modelMap.get("leggingsLeftBiomeOcean");
        leggingsMiddleBiomeOcean = modelMap.get("leggingsMiddleBiomeOcean");
        leggingsRightBiomeOcean = modelMap.get("leggingsRightBiomeOcean");
        bootsLeftBiomeOcean = modelMap.get("bootsLeftBiomeOcean");
        bootsRightBiomeOcean = modelMap.get("bootsRightBiomeOcean");

        biomeIsOcean = maid.getAtBiomeBiome() === "ocean";

        if (helmetBiomeOcean != undefined) {
            helmetBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (chestPlateBiomeOcean != undefined) {
            chestPlateBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (chestPlateLeftBiomeOcean != undefined) {
            chestPlateLeftBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (chestPlateMiddleBiomeOcean != undefined) {
            chestPlateMiddleBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (chestPlateRightBiomeOcean != undefined) {
            chestPlateRightBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (leggingsBiomeOcean != undefined) {
            leggingsBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (leggingsLeftBiomeOcean != undefined) {
            leggingsLeftBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (leggingsMiddleBiomeOcean != undefined) {
            leggingsMiddleBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (leggingsRightBiomeOcean != undefined) {
            leggingsRightBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (bootsLeftBiomeOcean != undefined) {
            bootsLeftBiomeOcean.setHidden(!biomeIsOcean);
        }
        if (bootsRightBiomeOcean != undefined) {
            bootsRightBiomeOcean.setHidden(!biomeIsOcean);
        }
    }
})

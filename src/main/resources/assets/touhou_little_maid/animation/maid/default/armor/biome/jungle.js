Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeJungle = modelMap.get("helmetBiomeJungle");
        chestPlateBiomeJungle = modelMap.get("chestPlateBiomeJungle");
        chestPlateLeftBiomeJungle = modelMap.get("chestPlateLeftBiomeJungle");
        chestPlateMiddleBiomeJungle = modelMap.get("chestPlateMiddleBiomeJungle");
        chestPlateRightBiomeJungle = modelMap.get("chestPlateRightBiomeJungle");
        leggingsBiomeJungle = modelMap.get("leggingsBiomeJungle");
        leggingsLeftBiomeJungle = modelMap.get("leggingsLeftBiomeJungle");
        leggingsMiddleBiomeJungle = modelMap.get("leggingsMiddleBiomeJungle");
        leggingsRightBiomeJungle = modelMap.get("leggingsRightBiomeJungle");
        bootsLeftBiomeJungle = modelMap.get("bootsLeftBiomeJungle");
        bootsRightBiomeJungle = modelMap.get("bootsRightBiomeJungle");

        biomeIsJungle = maid.getAtBiomeBiome() === "jungle";

        if (helmetBiomeJungle != undefined) {
            helmetBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (chestPlateBiomeJungle != undefined) {
            chestPlateBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (chestPlateLeftBiomeJungle != undefined) {
            chestPlateLeftBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (chestPlateMiddleBiomeJungle != undefined) {
            chestPlateMiddleBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (chestPlateRightBiomeJungle != undefined) {
            chestPlateRightBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (leggingsBiomeJungle != undefined) {
            leggingsBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (leggingsLeftBiomeJungle != undefined) {
            leggingsLeftBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (leggingsMiddleBiomeJungle != undefined) {
            leggingsMiddleBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (leggingsRightBiomeJungle != undefined) {
            leggingsRightBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (bootsLeftBiomeJungle != undefined) {
            bootsLeftBiomeJungle.setHidden(!biomeIsJungle);
        }
        if (bootsRightBiomeJungle != undefined) {
            bootsRightBiomeJungle.setHidden(!biomeIsJungle);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeJungleHills = modelMap.get("helmetBiomeJungleHills");
        chestPlateBiomeJungleHills = modelMap.get("chestPlateBiomeJungleHills");
        chestPlateLeftBiomeJungleHills = modelMap.get("chestPlateLeftBiomeJungleHills");
        chestPlateMiddleBiomeJungleHills = modelMap.get("chestPlateMiddleBiomeJungleHills");
        chestPlateRightBiomeJungleHills = modelMap.get("chestPlateRightBiomeJungleHills");
        leggingsBiomeJungleHills = modelMap.get("leggingsBiomeJungleHills");
        leggingsLeftBiomeJungleHills = modelMap.get("leggingsLeftBiomeJungleHills");
        leggingsMiddleBiomeJungleHills = modelMap.get("leggingsMiddleBiomeJungleHills");
        leggingsRightBiomeJungleHills = modelMap.get("leggingsRightBiomeJungleHills");
        bootsLeftBiomeJungleHills = modelMap.get("bootsLeftBiomeJungleHills");
        bootsRightBiomeJungleHills = modelMap.get("bootsRightBiomeJungleHills");

        biomeIsJungleHills = maid.getAtBiomeBiome() === "jungle_hills";

        if (helmetBiomeJungleHills != undefined) {
            helmetBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (chestPlateBiomeJungleHills != undefined) {
            chestPlateBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (chestPlateLeftBiomeJungleHills != undefined) {
            chestPlateLeftBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (chestPlateMiddleBiomeJungleHills != undefined) {
            chestPlateMiddleBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (chestPlateRightBiomeJungleHills != undefined) {
            chestPlateRightBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (leggingsBiomeJungleHills != undefined) {
            leggingsBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (leggingsLeftBiomeJungleHills != undefined) {
            leggingsLeftBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (leggingsMiddleBiomeJungleHills != undefined) {
            leggingsMiddleBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (leggingsRightBiomeJungleHills != undefined) {
            leggingsRightBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (bootsLeftBiomeJungleHills != undefined) {
            bootsLeftBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
        if (bootsRightBiomeJungleHills != undefined) {
            bootsRightBiomeJungleHills.setHidden(!biomeIsJungleHills);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeTaigaColdHills = modelMap.get("helmetBiomeTaigaColdHills");
        chestPlateBiomeTaigaColdHills = modelMap.get("chestPlateBiomeTaigaColdHills");
        chestPlateLeftBiomeTaigaColdHills = modelMap.get("chestPlateLeftBiomeTaigaColdHills");
        chestPlateMiddleBiomeTaigaColdHills = modelMap.get("chestPlateMiddleBiomeTaigaColdHills");
        chestPlateRightBiomeTaigaColdHills = modelMap.get("chestPlateRightBiomeTaigaColdHills");
        leggingsBiomeTaigaColdHills = modelMap.get("leggingsBiomeTaigaColdHills");
        leggingsLeftBiomeTaigaColdHills = modelMap.get("leggingsLeftBiomeTaigaColdHills");
        leggingsMiddleBiomeTaigaColdHills = modelMap.get("leggingsMiddleBiomeTaigaColdHills");
        leggingsRightBiomeTaigaColdHills = modelMap.get("leggingsRightBiomeTaigaColdHills");
        bootsLeftBiomeTaigaColdHills = modelMap.get("bootsLeftBiomeTaigaColdHills");
        bootsRightBiomeTaigaColdHills = modelMap.get("bootsRightBiomeTaigaColdHills");

        biomeIsTaigaColdHills = maid.getAtBiomeBiome() === "taiga_cold_hills";

        if (helmetBiomeTaigaColdHills != undefined) {
            helmetBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (chestPlateBiomeTaigaColdHills != undefined) {
            chestPlateBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (chestPlateLeftBiomeTaigaColdHills != undefined) {
            chestPlateLeftBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (chestPlateMiddleBiomeTaigaColdHills != undefined) {
            chestPlateMiddleBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (chestPlateRightBiomeTaigaColdHills != undefined) {
            chestPlateRightBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (leggingsBiomeTaigaColdHills != undefined) {
            leggingsBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (leggingsLeftBiomeTaigaColdHills != undefined) {
            leggingsLeftBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (leggingsMiddleBiomeTaigaColdHills != undefined) {
            leggingsMiddleBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (leggingsRightBiomeTaigaColdHills != undefined) {
            leggingsRightBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (bootsLeftBiomeTaigaColdHills != undefined) {
            bootsLeftBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
        if (bootsRightBiomeTaigaColdHills != undefined) {
            bootsRightBiomeTaigaColdHills.setHidden(!biomeIsTaigaColdHills);
        }
    }
})

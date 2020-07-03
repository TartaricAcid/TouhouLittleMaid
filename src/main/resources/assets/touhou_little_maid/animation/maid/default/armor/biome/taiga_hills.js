Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeTaigaHills = modelMap.get("helmetBiomeTaigaHills");
        chestPlateBiomeTaigaHills = modelMap.get("chestPlateBiomeTaigaHills");
        chestPlateLeftBiomeTaigaHills = modelMap.get("chestPlateLeftBiomeTaigaHills");
        chestPlateMiddleBiomeTaigaHills = modelMap.get("chestPlateMiddleBiomeTaigaHills");
        chestPlateRightBiomeTaigaHills = modelMap.get("chestPlateRightBiomeTaigaHills");
        leggingsBiomeTaigaHills = modelMap.get("leggingsBiomeTaigaHills");
        leggingsLeftBiomeTaigaHills = modelMap.get("leggingsLeftBiomeTaigaHills");
        leggingsMiddleBiomeTaigaHills = modelMap.get("leggingsMiddleBiomeTaigaHills");
        leggingsRightBiomeTaigaHills = modelMap.get("leggingsRightBiomeTaigaHills");
        bootsLeftBiomeTaigaHills = modelMap.get("bootsLeftBiomeTaigaHills");
        bootsRightBiomeTaigaHills = modelMap.get("bootsRightBiomeTaigaHills");

        biomeIsTaigaHills = maid.getAtBiomeBiome() === "taiga_hills";

        if (helmetBiomeTaigaHills != undefined) {
            helmetBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (chestPlateBiomeTaigaHills != undefined) {
            chestPlateBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (chestPlateLeftBiomeTaigaHills != undefined) {
            chestPlateLeftBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (chestPlateMiddleBiomeTaigaHills != undefined) {
            chestPlateMiddleBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (chestPlateRightBiomeTaigaHills != undefined) {
            chestPlateRightBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (leggingsBiomeTaigaHills != undefined) {
            leggingsBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (leggingsLeftBiomeTaigaHills != undefined) {
            leggingsLeftBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (leggingsMiddleBiomeTaigaHills != undefined) {
            leggingsMiddleBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (leggingsRightBiomeTaigaHills != undefined) {
            leggingsRightBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (bootsLeftBiomeTaigaHills != undefined) {
            bootsLeftBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
        if (bootsRightBiomeTaigaHills != undefined) {
            bootsRightBiomeTaigaHills.setHidden(!biomeIsTaigaHills);
        }
    }
})

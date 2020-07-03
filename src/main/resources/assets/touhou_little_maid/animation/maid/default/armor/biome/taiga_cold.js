Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeTaigaCold = modelMap.get("helmetBiomeTaigaCold");
        chestPlateBiomeTaigaCold = modelMap.get("chestPlateBiomeTaigaCold");
        chestPlateLeftBiomeTaigaCold = modelMap.get("chestPlateLeftBiomeTaigaCold");
        chestPlateMiddleBiomeTaigaCold = modelMap.get("chestPlateMiddleBiomeTaigaCold");
        chestPlateRightBiomeTaigaCold = modelMap.get("chestPlateRightBiomeTaigaCold");
        leggingsBiomeTaigaCold = modelMap.get("leggingsBiomeTaigaCold");
        leggingsLeftBiomeTaigaCold = modelMap.get("leggingsLeftBiomeTaigaCold");
        leggingsMiddleBiomeTaigaCold = modelMap.get("leggingsMiddleBiomeTaigaCold");
        leggingsRightBiomeTaigaCold = modelMap.get("leggingsRightBiomeTaigaCold");
        bootsLeftBiomeTaigaCold = modelMap.get("bootsLeftBiomeTaigaCold");
        bootsRightBiomeTaigaCold = modelMap.get("bootsRightBiomeTaigaCold");

        biomeIsTaigaCold = maid.getAtBiomeBiome() === "taiga_cold";

        if (helmetBiomeTaigaCold != undefined) {
            helmetBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (chestPlateBiomeTaigaCold != undefined) {
            chestPlateBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (chestPlateLeftBiomeTaigaCold != undefined) {
            chestPlateLeftBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (chestPlateMiddleBiomeTaigaCold != undefined) {
            chestPlateMiddleBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (chestPlateRightBiomeTaigaCold != undefined) {
            chestPlateRightBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (leggingsBiomeTaigaCold != undefined) {
            leggingsBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (leggingsLeftBiomeTaigaCold != undefined) {
            leggingsLeftBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (leggingsMiddleBiomeTaigaCold != undefined) {
            leggingsMiddleBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (leggingsRightBiomeTaigaCold != undefined) {
            leggingsRightBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (bootsLeftBiomeTaigaCold != undefined) {
            bootsLeftBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
        if (bootsRightBiomeTaigaCold != undefined) {
            bootsRightBiomeTaigaCold.setHidden(!biomeIsTaigaCold);
        }
    }
})

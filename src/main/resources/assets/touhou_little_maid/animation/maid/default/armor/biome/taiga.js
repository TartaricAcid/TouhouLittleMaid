Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeTaiga = modelMap.get("helmetBiomeTaiga");
        chestPlateBiomeTaiga = modelMap.get("chestPlateBiomeTaiga");
        chestPlateLeftBiomeTaiga = modelMap.get("chestPlateLeftBiomeTaiga");
        chestPlateMiddleBiomeTaiga = modelMap.get("chestPlateMiddleBiomeTaiga");
        chestPlateRightBiomeTaiga = modelMap.get("chestPlateRightBiomeTaiga");
        leggingsBiomeTaiga = modelMap.get("leggingsBiomeTaiga");
        leggingsLeftBiomeTaiga = modelMap.get("leggingsLeftBiomeTaiga");
        leggingsMiddleBiomeTaiga = modelMap.get("leggingsMiddleBiomeTaiga");
        leggingsRightBiomeTaiga = modelMap.get("leggingsRightBiomeTaiga");
        bootsLeftBiomeTaiga = modelMap.get("bootsLeftBiomeTaiga");
        bootsRightBiomeTaiga = modelMap.get("bootsRightBiomeTaiga");

        biomeIsTaiga = maid.getAtBiomeBiome() === "taiga";

        if (helmetBiomeTaiga != undefined) {
            helmetBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (chestPlateBiomeTaiga != undefined) {
            chestPlateBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (chestPlateLeftBiomeTaiga != undefined) {
            chestPlateLeftBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (chestPlateMiddleBiomeTaiga != undefined) {
            chestPlateMiddleBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (chestPlateRightBiomeTaiga != undefined) {
            chestPlateRightBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (leggingsBiomeTaiga != undefined) {
            leggingsBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (leggingsLeftBiomeTaiga != undefined) {
            leggingsLeftBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (leggingsMiddleBiomeTaiga != undefined) {
            leggingsMiddleBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (leggingsRightBiomeTaiga != undefined) {
            leggingsRightBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (bootsLeftBiomeTaiga != undefined) {
            bootsLeftBiomeTaiga.setHidden(!biomeIsTaiga);
        }
        if (bootsRightBiomeTaiga != undefined) {
            bootsRightBiomeTaiga.setHidden(!biomeIsTaiga);
        }
    }
})

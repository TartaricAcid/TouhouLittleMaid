Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeRedwoodTaiga = modelMap.get("helmetBiomeRedwoodTaiga");
        chestPlateBiomeRedwoodTaiga = modelMap.get("chestPlateBiomeRedwoodTaiga");
        chestPlateLeftBiomeRedwoodTaiga = modelMap.get("chestPlateLeftBiomeRedwoodTaiga");
        chestPlateMiddleBiomeRedwoodTaiga = modelMap.get("chestPlateMiddleBiomeRedwoodTaiga");
        chestPlateRightBiomeRedwoodTaiga = modelMap.get("chestPlateRightBiomeRedwoodTaiga");
        leggingsBiomeRedwoodTaiga = modelMap.get("leggingsBiomeRedwoodTaiga");
        leggingsLeftBiomeRedwoodTaiga = modelMap.get("leggingsLeftBiomeRedwoodTaiga");
        leggingsMiddleBiomeRedwoodTaiga = modelMap.get("leggingsMiddleBiomeRedwoodTaiga");
        leggingsRightBiomeRedwoodTaiga = modelMap.get("leggingsRightBiomeRedwoodTaiga");
        bootsLeftBiomeRedwoodTaiga = modelMap.get("bootsLeftBiomeRedwoodTaiga");
        bootsRightBiomeRedwoodTaiga = modelMap.get("bootsRightBiomeRedwoodTaiga");

        biomeIsRedwoodTaiga = maid.getAtBiomeBiome() === "redwood_taiga";

        if (helmetBiomeRedwoodTaiga != undefined) {
            helmetBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (chestPlateBiomeRedwoodTaiga != undefined) {
            chestPlateBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (chestPlateLeftBiomeRedwoodTaiga != undefined) {
            chestPlateLeftBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (chestPlateMiddleBiomeRedwoodTaiga != undefined) {
            chestPlateMiddleBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (chestPlateRightBiomeRedwoodTaiga != undefined) {
            chestPlateRightBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (leggingsBiomeRedwoodTaiga != undefined) {
            leggingsBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (leggingsLeftBiomeRedwoodTaiga != undefined) {
            leggingsLeftBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (leggingsMiddleBiomeRedwoodTaiga != undefined) {
            leggingsMiddleBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (leggingsRightBiomeRedwoodTaiga != undefined) {
            leggingsRightBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (bootsLeftBiomeRedwoodTaiga != undefined) {
            bootsLeftBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
        if (bootsRightBiomeRedwoodTaiga != undefined) {
            bootsRightBiomeRedwoodTaiga.setHidden(!biomeIsRedwoodTaiga);
        }
    }
})

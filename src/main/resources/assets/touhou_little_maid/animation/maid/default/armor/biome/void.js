Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeVoid = modelMap.get("helmetBiomeVoid");
        chestPlateBiomeVoid = modelMap.get("chestPlateBiomeVoid");
        chestPlateLeftBiomeVoid = modelMap.get("chestPlateLeftBiomeVoid");
        chestPlateMiddleBiomeVoid = modelMap.get("chestPlateMiddleBiomeVoid");
        chestPlateRightBiomeVoid = modelMap.get("chestPlateRightBiomeVoid");
        leggingsBiomeVoid = modelMap.get("leggingsBiomeVoid");
        leggingsLeftBiomeVoid = modelMap.get("leggingsLeftBiomeVoid");
        leggingsMiddleBiomeVoid = modelMap.get("leggingsMiddleBiomeVoid");
        leggingsRightBiomeVoid = modelMap.get("leggingsRightBiomeVoid");
        bootsLeftBiomeVoid = modelMap.get("bootsLeftBiomeVoid");
        bootsRightBiomeVoid = modelMap.get("bootsRightBiomeVoid");

        biomeIsVoid = maid.getAtBiomeBiome() === "void";

        if (helmetBiomeVoid != undefined) {
            helmetBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (chestPlateBiomeVoid != undefined) {
            chestPlateBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (chestPlateLeftBiomeVoid != undefined) {
            chestPlateLeftBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (chestPlateMiddleBiomeVoid != undefined) {
            chestPlateMiddleBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (chestPlateRightBiomeVoid != undefined) {
            chestPlateRightBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (leggingsBiomeVoid != undefined) {
            leggingsBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (leggingsLeftBiomeVoid != undefined) {
            leggingsLeftBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (leggingsMiddleBiomeVoid != undefined) {
            leggingsMiddleBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (leggingsRightBiomeVoid != undefined) {
            leggingsRightBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (bootsLeftBiomeVoid != undefined) {
            bootsLeftBiomeVoid.setHidden(!biomeIsVoid);
        }
        if (bootsRightBiomeVoid != undefined) {
            bootsRightBiomeVoid.setHidden(!biomeIsVoid);
        }
    }
})

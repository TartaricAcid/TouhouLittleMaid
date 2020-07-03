Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMesa = modelMap.get("helmetBiomeMesa");
        chestPlateBiomeMesa = modelMap.get("chestPlateBiomeMesa");
        chestPlateLeftBiomeMesa = modelMap.get("chestPlateLeftBiomeMesa");
        chestPlateMiddleBiomeMesa = modelMap.get("chestPlateMiddleBiomeMesa");
        chestPlateRightBiomeMesa = modelMap.get("chestPlateRightBiomeMesa");
        leggingsBiomeMesa = modelMap.get("leggingsBiomeMesa");
        leggingsLeftBiomeMesa = modelMap.get("leggingsLeftBiomeMesa");
        leggingsMiddleBiomeMesa = modelMap.get("leggingsMiddleBiomeMesa");
        leggingsRightBiomeMesa = modelMap.get("leggingsRightBiomeMesa");
        bootsLeftBiomeMesa = modelMap.get("bootsLeftBiomeMesa");
        bootsRightBiomeMesa = modelMap.get("bootsRightBiomeMesa");

        biomeIsMesa = maid.getAtBiomeBiome() === "mesa";

        if (helmetBiomeMesa != undefined) {
            helmetBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (chestPlateBiomeMesa != undefined) {
            chestPlateBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (chestPlateLeftBiomeMesa != undefined) {
            chestPlateLeftBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (chestPlateMiddleBiomeMesa != undefined) {
            chestPlateMiddleBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (chestPlateRightBiomeMesa != undefined) {
            chestPlateRightBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (leggingsBiomeMesa != undefined) {
            leggingsBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (leggingsLeftBiomeMesa != undefined) {
            leggingsLeftBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (leggingsMiddleBiomeMesa != undefined) {
            leggingsMiddleBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (leggingsRightBiomeMesa != undefined) {
            leggingsRightBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (bootsLeftBiomeMesa != undefined) {
            bootsLeftBiomeMesa.setHidden(!biomeIsMesa);
        }
        if (bootsRightBiomeMesa != undefined) {
            bootsRightBiomeMesa.setHidden(!biomeIsMesa);
        }
    }
})

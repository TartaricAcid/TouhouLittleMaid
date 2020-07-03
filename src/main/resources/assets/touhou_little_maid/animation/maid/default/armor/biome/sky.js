Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeSky = modelMap.get("helmetBiomeSky");
        chestPlateBiomeSky = modelMap.get("chestPlateBiomeSky");
        chestPlateLeftBiomeSky = modelMap.get("chestPlateLeftBiomeSky");
        chestPlateMiddleBiomeSky = modelMap.get("chestPlateMiddleBiomeSky");
        chestPlateRightBiomeSky = modelMap.get("chestPlateRightBiomeSky");
        leggingsBiomeSky = modelMap.get("leggingsBiomeSky");
        leggingsLeftBiomeSky = modelMap.get("leggingsLeftBiomeSky");
        leggingsMiddleBiomeSky = modelMap.get("leggingsMiddleBiomeSky");
        leggingsRightBiomeSky = modelMap.get("leggingsRightBiomeSky");
        bootsLeftBiomeSky = modelMap.get("bootsLeftBiomeSky");
        bootsRightBiomeSky = modelMap.get("bootsRightBiomeSky");

        biomeIsSky = maid.getAtBiomeBiome() === "sky";

        if (helmetBiomeSky != undefined) {
            helmetBiomeSky.setHidden(!biomeIsSky);
        }
        if (chestPlateBiomeSky != undefined) {
            chestPlateBiomeSky.setHidden(!biomeIsSky);
        }
        if (chestPlateLeftBiomeSky != undefined) {
            chestPlateLeftBiomeSky.setHidden(!biomeIsSky);
        }
        if (chestPlateMiddleBiomeSky != undefined) {
            chestPlateMiddleBiomeSky.setHidden(!biomeIsSky);
        }
        if (chestPlateRightBiomeSky != undefined) {
            chestPlateRightBiomeSky.setHidden(!biomeIsSky);
        }
        if (leggingsBiomeSky != undefined) {
            leggingsBiomeSky.setHidden(!biomeIsSky);
        }
        if (leggingsLeftBiomeSky != undefined) {
            leggingsLeftBiomeSky.setHidden(!biomeIsSky);
        }
        if (leggingsMiddleBiomeSky != undefined) {
            leggingsMiddleBiomeSky.setHidden(!biomeIsSky);
        }
        if (leggingsRightBiomeSky != undefined) {
            leggingsRightBiomeSky.setHidden(!biomeIsSky);
        }
        if (bootsLeftBiomeSky != undefined) {
            bootsLeftBiomeSky.setHidden(!biomeIsSky);
        }
        if (bootsRightBiomeSky != undefined) {
            bootsRightBiomeSky.setHidden(!biomeIsSky);
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeIceMountains = modelMap.get("helmetBiomeIceMountains");
        chestPlateBiomeIceMountains = modelMap.get("chestPlateBiomeIceMountains");
        chestPlateLeftBiomeIceMountains = modelMap.get("chestPlateLeftBiomeIceMountains");
        chestPlateMiddleBiomeIceMountains = modelMap.get("chestPlateMiddleBiomeIceMountains");
        chestPlateRightBiomeIceMountains = modelMap.get("chestPlateRightBiomeIceMountains");
        leggingsBiomeIceMountains = modelMap.get("leggingsBiomeIceMountains");
        leggingsLeftBiomeIceMountains = modelMap.get("leggingsLeftBiomeIceMountains");
        leggingsMiddleBiomeIceMountains = modelMap.get("leggingsMiddleBiomeIceMountains");
        leggingsRightBiomeIceMountains = modelMap.get("leggingsRightBiomeIceMountains");
        bootsLeftBiomeIceMountains = modelMap.get("bootsLeftBiomeIceMountains");
        bootsRightBiomeIceMountains = modelMap.get("bootsRightBiomeIceMountains");

        biomeIsIceMountains = maid.getAtBiomeBiome() === "ice_mountains";

        if (helmetBiomeIceMountains != undefined) {
            helmetBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (chestPlateBiomeIceMountains != undefined) {
            chestPlateBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (chestPlateLeftBiomeIceMountains != undefined) {
            chestPlateLeftBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (chestPlateMiddleBiomeIceMountains != undefined) {
            chestPlateMiddleBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (chestPlateRightBiomeIceMountains != undefined) {
            chestPlateRightBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (leggingsBiomeIceMountains != undefined) {
            leggingsBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (leggingsLeftBiomeIceMountains != undefined) {
            leggingsLeftBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (leggingsMiddleBiomeIceMountains != undefined) {
            leggingsMiddleBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (leggingsRightBiomeIceMountains != undefined) {
            leggingsRightBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (bootsLeftBiomeIceMountains != undefined) {
            bootsLeftBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
        if (bootsRightBiomeIceMountains != undefined) {
            bootsRightBiomeIceMountains.setHidden(!biomeIsIceMountains);
        }
    }
})

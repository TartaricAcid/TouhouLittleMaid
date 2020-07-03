Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeSwampland = modelMap.get("helmetBiomeSwampland");
        chestPlateBiomeSwampland = modelMap.get("chestPlateBiomeSwampland");
        chestPlateLeftBiomeSwampland = modelMap.get("chestPlateLeftBiomeSwampland");
        chestPlateMiddleBiomeSwampland = modelMap.get("chestPlateMiddleBiomeSwampland");
        chestPlateRightBiomeSwampland = modelMap.get("chestPlateRightBiomeSwampland");
        leggingsBiomeSwampland = modelMap.get("leggingsBiomeSwampland");
        leggingsLeftBiomeSwampland = modelMap.get("leggingsLeftBiomeSwampland");
        leggingsMiddleBiomeSwampland = modelMap.get("leggingsMiddleBiomeSwampland");
        leggingsRightBiomeSwampland = modelMap.get("leggingsRightBiomeSwampland");
        bootsLeftBiomeSwampland = modelMap.get("bootsLeftBiomeSwampland");
        bootsRightBiomeSwampland = modelMap.get("bootsRightBiomeSwampland");

        biomeIsSwampland = maid.getAtBiomeBiome() === "swampland";

        if (helmetBiomeSwampland != undefined) {
            helmetBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (chestPlateBiomeSwampland != undefined) {
            chestPlateBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (chestPlateLeftBiomeSwampland != undefined) {
            chestPlateLeftBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (chestPlateMiddleBiomeSwampland != undefined) {
            chestPlateMiddleBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (chestPlateRightBiomeSwampland != undefined) {
            chestPlateRightBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (leggingsBiomeSwampland != undefined) {
            leggingsBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (leggingsLeftBiomeSwampland != undefined) {
            leggingsLeftBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (leggingsMiddleBiomeSwampland != undefined) {
            leggingsMiddleBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (leggingsRightBiomeSwampland != undefined) {
            leggingsRightBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (bootsLeftBiomeSwampland != undefined) {
            bootsLeftBiomeSwampland.setHidden(!biomeIsSwampland);
        }
        if (bootsRightBiomeSwampland != undefined) {
            bootsRightBiomeSwampland.setHidden(!biomeIsSwampland);
        }
    }
})

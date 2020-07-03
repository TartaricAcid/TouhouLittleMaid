Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeHell = modelMap.get("helmetBiomeHell");
        chestPlateBiomeHell = modelMap.get("chestPlateBiomeHell");
        chestPlateLeftBiomeHell = modelMap.get("chestPlateLeftBiomeHell");
        chestPlateMiddleBiomeHell = modelMap.get("chestPlateMiddleBiomeHell");
        chestPlateRightBiomeHell = modelMap.get("chestPlateRightBiomeHell");
        leggingsBiomeHell = modelMap.get("leggingsBiomeHell");
        leggingsLeftBiomeHell = modelMap.get("leggingsLeftBiomeHell");
        leggingsMiddleBiomeHell = modelMap.get("leggingsMiddleBiomeHell");
        leggingsRightBiomeHell = modelMap.get("leggingsRightBiomeHell");
        bootsLeftBiomeHell = modelMap.get("bootsLeftBiomeHell");
        bootsRightBiomeHell = modelMap.get("bootsRightBiomeHell");

        biomeIsHell = maid.getAtBiomeBiome() === "hell";

        if (helmetBiomeHell != undefined) {
            helmetBiomeHell.setHidden(!biomeIsHell);
        }
        if (chestPlateBiomeHell != undefined) {
            chestPlateBiomeHell.setHidden(!biomeIsHell);
        }
        if (chestPlateLeftBiomeHell != undefined) {
            chestPlateLeftBiomeHell.setHidden(!biomeIsHell);
        }
        if (chestPlateMiddleBiomeHell != undefined) {
            chestPlateMiddleBiomeHell.setHidden(!biomeIsHell);
        }
        if (chestPlateRightBiomeHell != undefined) {
            chestPlateRightBiomeHell.setHidden(!biomeIsHell);
        }
        if (leggingsBiomeHell != undefined) {
            leggingsBiomeHell.setHidden(!biomeIsHell);
        }
        if (leggingsLeftBiomeHell != undefined) {
            leggingsLeftBiomeHell.setHidden(!biomeIsHell);
        }
        if (leggingsMiddleBiomeHell != undefined) {
            leggingsMiddleBiomeHell.setHidden(!biomeIsHell);
        }
        if (leggingsRightBiomeHell != undefined) {
            leggingsRightBiomeHell.setHidden(!biomeIsHell);
        }
        if (bootsLeftBiomeHell != undefined) {
            bootsLeftBiomeHell.setHidden(!biomeIsHell);
        }
        if (bootsRightBiomeHell != undefined) {
            bootsRightBiomeHell.setHidden(!biomeIsHell);
        }
    }
})

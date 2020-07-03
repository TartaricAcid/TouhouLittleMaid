Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeRiver = modelMap.get("helmetBiomeRiver");
        chestPlateBiomeRiver = modelMap.get("chestPlateBiomeRiver");
        chestPlateLeftBiomeRiver = modelMap.get("chestPlateLeftBiomeRiver");
        chestPlateMiddleBiomeRiver = modelMap.get("chestPlateMiddleBiomeRiver");
        chestPlateRightBiomeRiver = modelMap.get("chestPlateRightBiomeRiver");
        leggingsBiomeRiver = modelMap.get("leggingsBiomeRiver");
        leggingsLeftBiomeRiver = modelMap.get("leggingsLeftBiomeRiver");
        leggingsMiddleBiomeRiver = modelMap.get("leggingsMiddleBiomeRiver");
        leggingsRightBiomeRiver = modelMap.get("leggingsRightBiomeRiver");
        bootsLeftBiomeRiver = modelMap.get("bootsLeftBiomeRiver");
        bootsRightBiomeRiver = modelMap.get("bootsRightBiomeRiver");

        biomeIsRiver = maid.getAtBiomeBiome() === "river";

        if (helmetBiomeRiver != undefined) {
            helmetBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (chestPlateBiomeRiver != undefined) {
            chestPlateBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (chestPlateLeftBiomeRiver != undefined) {
            chestPlateLeftBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (chestPlateMiddleBiomeRiver != undefined) {
            chestPlateMiddleBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (chestPlateRightBiomeRiver != undefined) {
            chestPlateRightBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (leggingsBiomeRiver != undefined) {
            leggingsBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (leggingsLeftBiomeRiver != undefined) {
            leggingsLeftBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (leggingsMiddleBiomeRiver != undefined) {
            leggingsMiddleBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (leggingsRightBiomeRiver != undefined) {
            leggingsRightBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (bootsLeftBiomeRiver != undefined) {
            bootsLeftBiomeRiver.setHidden(!biomeIsRiver);
        }
        if (bootsRightBiomeRiver != undefined) {
            bootsRightBiomeRiver.setHidden(!biomeIsRiver);
        }
    }
})

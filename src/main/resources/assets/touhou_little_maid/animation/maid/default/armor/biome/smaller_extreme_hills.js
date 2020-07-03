Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeSmallerExtremeHills = modelMap.get("helmetBiomeSmallerExtremeHills");
        chestPlateBiomeSmallerExtremeHills = modelMap.get("chestPlateBiomeSmallerExtremeHills");
        chestPlateLeftBiomeSmallerExtremeHills = modelMap.get("chestPlateLeftBiomeSmallerExtremeHills");
        chestPlateMiddleBiomeSmallerExtremeHills = modelMap.get("chestPlateMiddleBiomeSmallerExtremeHills");
        chestPlateRightBiomeSmallerExtremeHills = modelMap.get("chestPlateRightBiomeSmallerExtremeHills");
        leggingsBiomeSmallerExtremeHills = modelMap.get("leggingsBiomeSmallerExtremeHills");
        leggingsLeftBiomeSmallerExtremeHills = modelMap.get("leggingsLeftBiomeSmallerExtremeHills");
        leggingsMiddleBiomeSmallerExtremeHills = modelMap.get("leggingsMiddleBiomeSmallerExtremeHills");
        leggingsRightBiomeSmallerExtremeHills = modelMap.get("leggingsRightBiomeSmallerExtremeHills");
        bootsLeftBiomeSmallerExtremeHills = modelMap.get("bootsLeftBiomeSmallerExtremeHills");
        bootsRightBiomeSmallerExtremeHills = modelMap.get("bootsRightBiomeSmallerExtremeHills");

        biomeIsSmallerExtremeHills = maid.getAtBiomeBiome() === "smaller_extreme_hills";

        if (helmetBiomeSmallerExtremeHills != undefined) {
            helmetBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (chestPlateBiomeSmallerExtremeHills != undefined) {
            chestPlateBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (chestPlateLeftBiomeSmallerExtremeHills != undefined) {
            chestPlateLeftBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (chestPlateMiddleBiomeSmallerExtremeHills != undefined) {
            chestPlateMiddleBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (chestPlateRightBiomeSmallerExtremeHills != undefined) {
            chestPlateRightBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (leggingsBiomeSmallerExtremeHills != undefined) {
            leggingsBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (leggingsLeftBiomeSmallerExtremeHills != undefined) {
            leggingsLeftBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (leggingsMiddleBiomeSmallerExtremeHills != undefined) {
            leggingsMiddleBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (leggingsRightBiomeSmallerExtremeHills != undefined) {
            leggingsRightBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (bootsLeftBiomeSmallerExtremeHills != undefined) {
            bootsLeftBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
        if (bootsRightBiomeSmallerExtremeHills != undefined) {
            bootsRightBiomeSmallerExtremeHills.setHidden(!biomeIsSmallerExtremeHills);
        }
    }
})

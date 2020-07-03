Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeFrozenRiver = modelMap.get("helmetBiomeFrozenRiver");
        chestPlateBiomeFrozenRiver = modelMap.get("chestPlateBiomeFrozenRiver");
        chestPlateLeftBiomeFrozenRiver = modelMap.get("chestPlateLeftBiomeFrozenRiver");
        chestPlateMiddleBiomeFrozenRiver = modelMap.get("chestPlateMiddleBiomeFrozenRiver");
        chestPlateRightBiomeFrozenRiver = modelMap.get("chestPlateRightBiomeFrozenRiver");
        leggingsBiomeFrozenRiver = modelMap.get("leggingsBiomeFrozenRiver");
        leggingsLeftBiomeFrozenRiver = modelMap.get("leggingsLeftBiomeFrozenRiver");
        leggingsMiddleBiomeFrozenRiver = modelMap.get("leggingsMiddleBiomeFrozenRiver");
        leggingsRightBiomeFrozenRiver = modelMap.get("leggingsRightBiomeFrozenRiver");
        bootsLeftBiomeFrozenRiver = modelMap.get("bootsLeftBiomeFrozenRiver");
        bootsRightBiomeFrozenRiver = modelMap.get("bootsRightBiomeFrozenRiver");

        biomeIsFrozenRiver = maid.getAtBiomeBiome() === "frozen_river";

        if (helmetBiomeFrozenRiver != undefined) {
            helmetBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (chestPlateBiomeFrozenRiver != undefined) {
            chestPlateBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (chestPlateLeftBiomeFrozenRiver != undefined) {
            chestPlateLeftBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (chestPlateMiddleBiomeFrozenRiver != undefined) {
            chestPlateMiddleBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (chestPlateRightBiomeFrozenRiver != undefined) {
            chestPlateRightBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (leggingsBiomeFrozenRiver != undefined) {
            leggingsBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (leggingsLeftBiomeFrozenRiver != undefined) {
            leggingsLeftBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (leggingsMiddleBiomeFrozenRiver != undefined) {
            leggingsMiddleBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (leggingsRightBiomeFrozenRiver != undefined) {
            leggingsRightBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (bootsLeftBiomeFrozenRiver != undefined) {
            bootsLeftBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
        if (bootsRightBiomeFrozenRiver != undefined) {
            bootsRightBiomeFrozenRiver.setHidden(!biomeIsFrozenRiver);
        }
    }
})

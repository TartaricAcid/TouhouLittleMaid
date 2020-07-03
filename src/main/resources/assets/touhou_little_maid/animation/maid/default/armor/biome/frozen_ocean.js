Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeFrozenOcean = modelMap.get("helmetBiomeFrozenOcean");
        chestPlateBiomeFrozenOcean = modelMap.get("chestPlateBiomeFrozenOcean");
        chestPlateLeftBiomeFrozenOcean = modelMap.get("chestPlateLeftBiomeFrozenOcean");
        chestPlateMiddleBiomeFrozenOcean = modelMap.get("chestPlateMiddleBiomeFrozenOcean");
        chestPlateRightBiomeFrozenOcean = modelMap.get("chestPlateRightBiomeFrozenOcean");
        leggingsBiomeFrozenOcean = modelMap.get("leggingsBiomeFrozenOcean");
        leggingsLeftBiomeFrozenOcean = modelMap.get("leggingsLeftBiomeFrozenOcean");
        leggingsMiddleBiomeFrozenOcean = modelMap.get("leggingsMiddleBiomeFrozenOcean");
        leggingsRightBiomeFrozenOcean = modelMap.get("leggingsRightBiomeFrozenOcean");
        bootsLeftBiomeFrozenOcean = modelMap.get("bootsLeftBiomeFrozenOcean");
        bootsRightBiomeFrozenOcean = modelMap.get("bootsRightBiomeFrozenOcean");

        biomeIsFrozenOcean = maid.getAtBiomeBiome() === "frozen_ocean";

        if (helmetBiomeFrozenOcean != undefined) {
            helmetBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (chestPlateBiomeFrozenOcean != undefined) {
            chestPlateBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (chestPlateLeftBiomeFrozenOcean != undefined) {
            chestPlateLeftBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (chestPlateMiddleBiomeFrozenOcean != undefined) {
            chestPlateMiddleBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (chestPlateRightBiomeFrozenOcean != undefined) {
            chestPlateRightBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (leggingsBiomeFrozenOcean != undefined) {
            leggingsBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (leggingsLeftBiomeFrozenOcean != undefined) {
            leggingsLeftBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (leggingsMiddleBiomeFrozenOcean != undefined) {
            leggingsMiddleBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (leggingsRightBiomeFrozenOcean != undefined) {
            leggingsRightBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (bootsLeftBiomeFrozenOcean != undefined) {
            bootsLeftBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
        if (bootsRightBiomeFrozenOcean != undefined) {
            bootsRightBiomeFrozenOcean.setHidden(!biomeIsFrozenOcean);
        }
    }
})

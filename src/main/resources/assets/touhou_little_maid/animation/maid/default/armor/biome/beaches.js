Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeBeaches = modelMap.get("helmetBiomeBeaches");
        chestPlateBiomeBeaches = modelMap.get("chestPlateBiomeBeaches");
        chestPlateLeftBiomeBeaches = modelMap.get("chestPlateLeftBiomeBeaches");
        chestPlateMiddleBiomeBeaches = modelMap.get("chestPlateMiddleBiomeBeaches");
        chestPlateRightBiomeBeaches = modelMap.get("chestPlateRightBiomeBeaches");
        leggingsBiomeBeaches = modelMap.get("leggingsBiomeBeaches");
        leggingsLeftBiomeBeaches = modelMap.get("leggingsLeftBiomeBeaches");
        leggingsMiddleBiomeBeaches = modelMap.get("leggingsMiddleBiomeBeaches");
        leggingsRightBiomeBeaches = modelMap.get("leggingsRightBiomeBeaches");
        bootsLeftBiomeBeaches = modelMap.get("bootsLeftBiomeBeaches");
        bootsRightBiomeBeaches = modelMap.get("bootsRightBiomeBeaches");

        biomeIsBeaches = maid.getAtBiomeBiome() === "beaches";

        if (helmetBiomeBeaches != undefined) {
            helmetBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (chestPlateBiomeBeaches != undefined) {
            chestPlateBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (chestPlateLeftBiomeBeaches != undefined) {
            chestPlateLeftBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (chestPlateMiddleBiomeBeaches != undefined) {
            chestPlateMiddleBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (chestPlateRightBiomeBeaches != undefined) {
            chestPlateRightBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (leggingsBiomeBeaches != undefined) {
            leggingsBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (leggingsLeftBiomeBeaches != undefined) {
            leggingsLeftBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (leggingsMiddleBiomeBeaches != undefined) {
            leggingsMiddleBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (leggingsRightBiomeBeaches != undefined) {
            leggingsRightBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (bootsLeftBiomeBeaches != undefined) {
            bootsLeftBiomeBeaches.setHidden(!biomeIsBeaches);
        }
        if (bootsRightBiomeBeaches != undefined) {
            bootsRightBiomeBeaches.setHidden(!biomeIsBeaches);
        }
    }
})

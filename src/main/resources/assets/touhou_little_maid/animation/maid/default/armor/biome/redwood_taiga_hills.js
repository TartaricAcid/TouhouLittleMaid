Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeRedwoodTaigaHills = modelMap.get("helmetBiomeRedwoodTaigaHills");
        chestPlateBiomeRedwoodTaigaHills = modelMap.get("chestPlateBiomeRedwoodTaigaHills");
        chestPlateLeftBiomeRedwoodTaigaHills = modelMap.get("chestPlateLeftBiomeRedwoodTaigaHills");
        chestPlateMiddleBiomeRedwoodTaigaHills = modelMap.get("chestPlateMiddleBiomeRedwoodTaigaHills");
        chestPlateRightBiomeRedwoodTaigaHills = modelMap.get("chestPlateRightBiomeRedwoodTaigaHills");
        leggingsBiomeRedwoodTaigaHills = modelMap.get("leggingsBiomeRedwoodTaigaHills");
        leggingsLeftBiomeRedwoodTaigaHills = modelMap.get("leggingsLeftBiomeRedwoodTaigaHills");
        leggingsMiddleBiomeRedwoodTaigaHills = modelMap.get("leggingsMiddleBiomeRedwoodTaigaHills");
        leggingsRightBiomeRedwoodTaigaHills = modelMap.get("leggingsRightBiomeRedwoodTaigaHills");
        bootsLeftBiomeRedwoodTaigaHills = modelMap.get("bootsLeftBiomeRedwoodTaigaHills");
        bootsRightBiomeRedwoodTaigaHills = modelMap.get("bootsRightBiomeRedwoodTaigaHills");

        biomeIsRedwoodTaigaHills = maid.getAtBiomeBiome() === "redwood_taiga_hills";

        if (helmetBiomeRedwoodTaigaHills != undefined) {
            helmetBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (chestPlateBiomeRedwoodTaigaHills != undefined) {
            chestPlateBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (chestPlateLeftBiomeRedwoodTaigaHills != undefined) {
            chestPlateLeftBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (chestPlateMiddleBiomeRedwoodTaigaHills != undefined) {
            chestPlateMiddleBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (chestPlateRightBiomeRedwoodTaigaHills != undefined) {
            chestPlateRightBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (leggingsBiomeRedwoodTaigaHills != undefined) {
            leggingsBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (leggingsLeftBiomeRedwoodTaigaHills != undefined) {
            leggingsLeftBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (leggingsMiddleBiomeRedwoodTaigaHills != undefined) {
            leggingsMiddleBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (leggingsRightBiomeRedwoodTaigaHills != undefined) {
            leggingsRightBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (bootsLeftBiomeRedwoodTaigaHills != undefined) {
            bootsLeftBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
        if (bootsRightBiomeRedwoodTaigaHills != undefined) {
            bootsRightBiomeRedwoodTaigaHills.setHidden(!biomeIsRedwoodTaigaHills);
        }
    }
})

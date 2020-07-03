Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeIceFlats = modelMap.get("helmetBiomeIceFlats");
        chestPlateBiomeIceFlats = modelMap.get("chestPlateBiomeIceFlats");
        chestPlateLeftBiomeIceFlats = modelMap.get("chestPlateLeftBiomeIceFlats");
        chestPlateMiddleBiomeIceFlats = modelMap.get("chestPlateMiddleBiomeIceFlats");
        chestPlateRightBiomeIceFlats = modelMap.get("chestPlateRightBiomeIceFlats");
        leggingsBiomeIceFlats = modelMap.get("leggingsBiomeIceFlats");
        leggingsLeftBiomeIceFlats = modelMap.get("leggingsLeftBiomeIceFlats");
        leggingsMiddleBiomeIceFlats = modelMap.get("leggingsMiddleBiomeIceFlats");
        leggingsRightBiomeIceFlats = modelMap.get("leggingsRightBiomeIceFlats");
        bootsLeftBiomeIceFlats = modelMap.get("bootsLeftBiomeIceFlats");
        bootsRightBiomeIceFlats = modelMap.get("bootsRightBiomeIceFlats");

        biomeIsIceFlats = maid.getAtBiomeBiome() === "ice_flats";

        if (helmetBiomeIceFlats != undefined) {
            helmetBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (chestPlateBiomeIceFlats != undefined) {
            chestPlateBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (chestPlateLeftBiomeIceFlats != undefined) {
            chestPlateLeftBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (chestPlateMiddleBiomeIceFlats != undefined) {
            chestPlateMiddleBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (chestPlateRightBiomeIceFlats != undefined) {
            chestPlateRightBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (leggingsBiomeIceFlats != undefined) {
            leggingsBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (leggingsLeftBiomeIceFlats != undefined) {
            leggingsLeftBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (leggingsMiddleBiomeIceFlats != undefined) {
            leggingsMiddleBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (leggingsRightBiomeIceFlats != undefined) {
            leggingsRightBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (bootsLeftBiomeIceFlats != undefined) {
            bootsLeftBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
        if (bootsRightBiomeIceFlats != undefined) {
            bootsRightBiomeIceFlats.setHidden(!biomeIsIceFlats);
        }
    }
})

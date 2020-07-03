Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedIceFlats = modelMap.get("helmetBiomeMutatedIceFlats");
        chestPlateBiomeMutatedIceFlats = modelMap.get("chestPlateBiomeMutatedIceFlats");
        chestPlateLeftBiomeMutatedIceFlats = modelMap.get("chestPlateLeftBiomeMutatedIceFlats");
        chestPlateMiddleBiomeMutatedIceFlats = modelMap.get("chestPlateMiddleBiomeMutatedIceFlats");
        chestPlateRightBiomeMutatedIceFlats = modelMap.get("chestPlateRightBiomeMutatedIceFlats");
        leggingsBiomeMutatedIceFlats = modelMap.get("leggingsBiomeMutatedIceFlats");
        leggingsLeftBiomeMutatedIceFlats = modelMap.get("leggingsLeftBiomeMutatedIceFlats");
        leggingsMiddleBiomeMutatedIceFlats = modelMap.get("leggingsMiddleBiomeMutatedIceFlats");
        leggingsRightBiomeMutatedIceFlats = modelMap.get("leggingsRightBiomeMutatedIceFlats");
        bootsLeftBiomeMutatedIceFlats = modelMap.get("bootsLeftBiomeMutatedIceFlats");
        bootsRightBiomeMutatedIceFlats = modelMap.get("bootsRightBiomeMutatedIceFlats");

        biomeIsMutatedIceFlats = maid.getAtBiomeBiome() === "mutated_ice_flats";

        if (helmetBiomeMutatedIceFlats != undefined) {
            helmetBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (chestPlateBiomeMutatedIceFlats != undefined) {
            chestPlateBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (chestPlateLeftBiomeMutatedIceFlats != undefined) {
            chestPlateLeftBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (chestPlateMiddleBiomeMutatedIceFlats != undefined) {
            chestPlateMiddleBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (chestPlateRightBiomeMutatedIceFlats != undefined) {
            chestPlateRightBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (leggingsBiomeMutatedIceFlats != undefined) {
            leggingsBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (leggingsLeftBiomeMutatedIceFlats != undefined) {
            leggingsLeftBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (leggingsMiddleBiomeMutatedIceFlats != undefined) {
            leggingsMiddleBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (leggingsRightBiomeMutatedIceFlats != undefined) {
            leggingsRightBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (bootsLeftBiomeMutatedIceFlats != undefined) {
            bootsLeftBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
        if (bootsRightBiomeMutatedIceFlats != undefined) {
            bootsRightBiomeMutatedIceFlats.setHidden(!biomeIsMutatedIceFlats);
        }
    }
})

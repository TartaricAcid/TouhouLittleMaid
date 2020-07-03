Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedRedwoodTaigaHills = modelMap.get("helmetBiomeMutatedRedwoodTaigaHills");
        chestPlateBiomeMutatedRedwoodTaigaHills = modelMap.get("chestPlateBiomeMutatedRedwoodTaigaHills");
        chestPlateLeftBiomeMutatedRedwoodTaigaHills = modelMap.get("chestPlateLeftBiomeMutatedRedwoodTaigaHills");
        chestPlateMiddleBiomeMutatedRedwoodTaigaHills = modelMap.get("chestPlateMiddleBiomeMutatedRedwoodTaigaHills");
        chestPlateRightBiomeMutatedRedwoodTaigaHills = modelMap.get("chestPlateRightBiomeMutatedRedwoodTaigaHills");
        leggingsBiomeMutatedRedwoodTaigaHills = modelMap.get("leggingsBiomeMutatedRedwoodTaigaHills");
        leggingsLeftBiomeMutatedRedwoodTaigaHills = modelMap.get("leggingsLeftBiomeMutatedRedwoodTaigaHills");
        leggingsMiddleBiomeMutatedRedwoodTaigaHills = modelMap.get("leggingsMiddleBiomeMutatedRedwoodTaigaHills");
        leggingsRightBiomeMutatedRedwoodTaigaHills = modelMap.get("leggingsRightBiomeMutatedRedwoodTaigaHills");
        bootsLeftBiomeMutatedRedwoodTaigaHills = modelMap.get("bootsLeftBiomeMutatedRedwoodTaigaHills");
        bootsRightBiomeMutatedRedwoodTaigaHills = modelMap.get("bootsRightBiomeMutatedRedwoodTaigaHills");

        biomeIsMutatedRedwoodTaigaHills = maid.getAtBiomeBiome() === "mutated_redwood_taiga_hills";

        if (helmetBiomeMutatedRedwoodTaigaHills != undefined) {
            helmetBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (chestPlateBiomeMutatedRedwoodTaigaHills != undefined) {
            chestPlateBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (chestPlateLeftBiomeMutatedRedwoodTaigaHills != undefined) {
            chestPlateLeftBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (chestPlateMiddleBiomeMutatedRedwoodTaigaHills != undefined) {
            chestPlateMiddleBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (chestPlateRightBiomeMutatedRedwoodTaigaHills != undefined) {
            chestPlateRightBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (leggingsBiomeMutatedRedwoodTaigaHills != undefined) {
            leggingsBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (leggingsLeftBiomeMutatedRedwoodTaigaHills != undefined) {
            leggingsLeftBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (leggingsMiddleBiomeMutatedRedwoodTaigaHills != undefined) {
            leggingsMiddleBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (leggingsRightBiomeMutatedRedwoodTaigaHills != undefined) {
            leggingsRightBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (bootsLeftBiomeMutatedRedwoodTaigaHills != undefined) {
            bootsLeftBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
        if (bootsRightBiomeMutatedRedwoodTaigaHills != undefined) {
            bootsRightBiomeMutatedRedwoodTaigaHills.setHidden(!biomeIsMutatedRedwoodTaigaHills);
        }
    }
})

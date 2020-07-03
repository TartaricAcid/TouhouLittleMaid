Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMutatedMesa = modelMap.get("helmetBiomeMutatedMesa");
        chestPlateBiomeMutatedMesa = modelMap.get("chestPlateBiomeMutatedMesa");
        chestPlateLeftBiomeMutatedMesa = modelMap.get("chestPlateLeftBiomeMutatedMesa");
        chestPlateMiddleBiomeMutatedMesa = modelMap.get("chestPlateMiddleBiomeMutatedMesa");
        chestPlateRightBiomeMutatedMesa = modelMap.get("chestPlateRightBiomeMutatedMesa");
        leggingsBiomeMutatedMesa = modelMap.get("leggingsBiomeMutatedMesa");
        leggingsLeftBiomeMutatedMesa = modelMap.get("leggingsLeftBiomeMutatedMesa");
        leggingsMiddleBiomeMutatedMesa = modelMap.get("leggingsMiddleBiomeMutatedMesa");
        leggingsRightBiomeMutatedMesa = modelMap.get("leggingsRightBiomeMutatedMesa");
        bootsLeftBiomeMutatedMesa = modelMap.get("bootsLeftBiomeMutatedMesa");
        bootsRightBiomeMutatedMesa = modelMap.get("bootsRightBiomeMutatedMesa");

        biomeIsMutatedMesa = maid.getAtBiomeBiome() === "mutated_mesa";

        if (helmetBiomeMutatedMesa != undefined) {
            helmetBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (chestPlateBiomeMutatedMesa != undefined) {
            chestPlateBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (chestPlateLeftBiomeMutatedMesa != undefined) {
            chestPlateLeftBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (chestPlateMiddleBiomeMutatedMesa != undefined) {
            chestPlateMiddleBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (chestPlateRightBiomeMutatedMesa != undefined) {
            chestPlateRightBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (leggingsBiomeMutatedMesa != undefined) {
            leggingsBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (leggingsLeftBiomeMutatedMesa != undefined) {
            leggingsLeftBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (leggingsMiddleBiomeMutatedMesa != undefined) {
            leggingsMiddleBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (leggingsRightBiomeMutatedMesa != undefined) {
            leggingsRightBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (bootsLeftBiomeMutatedMesa != undefined) {
            bootsLeftBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
        if (bootsRightBiomeMutatedMesa != undefined) {
            bootsRightBiomeMutatedMesa.setHidden(!biomeIsMutatedMesa);
        }
    }
})

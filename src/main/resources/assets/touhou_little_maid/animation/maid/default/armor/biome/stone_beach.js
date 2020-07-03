Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeStoneBeach = modelMap.get("helmetBiomeStoneBeach");
        chestPlateBiomeStoneBeach = modelMap.get("chestPlateBiomeStoneBeach");
        chestPlateLeftBiomeStoneBeach = modelMap.get("chestPlateLeftBiomeStoneBeach");
        chestPlateMiddleBiomeStoneBeach = modelMap.get("chestPlateMiddleBiomeStoneBeach");
        chestPlateRightBiomeStoneBeach = modelMap.get("chestPlateRightBiomeStoneBeach");
        leggingsBiomeStoneBeach = modelMap.get("leggingsBiomeStoneBeach");
        leggingsLeftBiomeStoneBeach = modelMap.get("leggingsLeftBiomeStoneBeach");
        leggingsMiddleBiomeStoneBeach = modelMap.get("leggingsMiddleBiomeStoneBeach");
        leggingsRightBiomeStoneBeach = modelMap.get("leggingsRightBiomeStoneBeach");
        bootsLeftBiomeStoneBeach = modelMap.get("bootsLeftBiomeStoneBeach");
        bootsRightBiomeStoneBeach = modelMap.get("bootsRightBiomeStoneBeach");

        biomeIsStoneBeach = maid.getAtBiomeBiome() === "stone_beach";

        if (helmetBiomeStoneBeach != undefined) {
            helmetBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (chestPlateBiomeStoneBeach != undefined) {
            chestPlateBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (chestPlateLeftBiomeStoneBeach != undefined) {
            chestPlateLeftBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (chestPlateMiddleBiomeStoneBeach != undefined) {
            chestPlateMiddleBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (chestPlateRightBiomeStoneBeach != undefined) {
            chestPlateRightBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (leggingsBiomeStoneBeach != undefined) {
            leggingsBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (leggingsLeftBiomeStoneBeach != undefined) {
            leggingsLeftBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (leggingsMiddleBiomeStoneBeach != undefined) {
            leggingsMiddleBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (leggingsRightBiomeStoneBeach != undefined) {
            leggingsRightBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (bootsLeftBiomeStoneBeach != undefined) {
            bootsLeftBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
        if (bootsRightBiomeStoneBeach != undefined) {
            bootsRightBiomeStoneBeach.setHidden(!biomeIsStoneBeach);
        }
    }
})

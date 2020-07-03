Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMushroomIsland = modelMap.get("helmetBiomeMushroomIsland");
        chestPlateBiomeMushroomIsland = modelMap.get("chestPlateBiomeMushroomIsland");
        chestPlateLeftBiomeMushroomIsland = modelMap.get("chestPlateLeftBiomeMushroomIsland");
        chestPlateMiddleBiomeMushroomIsland = modelMap.get("chestPlateMiddleBiomeMushroomIsland");
        chestPlateRightBiomeMushroomIsland = modelMap.get("chestPlateRightBiomeMushroomIsland");
        leggingsBiomeMushroomIsland = modelMap.get("leggingsBiomeMushroomIsland");
        leggingsLeftBiomeMushroomIsland = modelMap.get("leggingsLeftBiomeMushroomIsland");
        leggingsMiddleBiomeMushroomIsland = modelMap.get("leggingsMiddleBiomeMushroomIsland");
        leggingsRightBiomeMushroomIsland = modelMap.get("leggingsRightBiomeMushroomIsland");
        bootsLeftBiomeMushroomIsland = modelMap.get("bootsLeftBiomeMushroomIsland");
        bootsRightBiomeMushroomIsland = modelMap.get("bootsRightBiomeMushroomIsland");

        biomeIsMushroomIsland = maid.getAtBiomeBiome() === "mushroom_island";

        if (helmetBiomeMushroomIsland != undefined) {
            helmetBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (chestPlateBiomeMushroomIsland != undefined) {
            chestPlateBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (chestPlateLeftBiomeMushroomIsland != undefined) {
            chestPlateLeftBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (chestPlateMiddleBiomeMushroomIsland != undefined) {
            chestPlateMiddleBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (chestPlateRightBiomeMushroomIsland != undefined) {
            chestPlateRightBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (leggingsBiomeMushroomIsland != undefined) {
            leggingsBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (leggingsLeftBiomeMushroomIsland != undefined) {
            leggingsLeftBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (leggingsMiddleBiomeMushroomIsland != undefined) {
            leggingsMiddleBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (leggingsRightBiomeMushroomIsland != undefined) {
            leggingsRightBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (bootsLeftBiomeMushroomIsland != undefined) {
            bootsLeftBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
        if (bootsRightBiomeMushroomIsland != undefined) {
            bootsRightBiomeMushroomIsland.setHidden(!biomeIsMushroomIsland);
        }
    }
})

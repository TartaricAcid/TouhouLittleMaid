Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetBiomeMushroomIslandShore = modelMap.get("helmetBiomeMushroomIslandShore");
        chestPlateBiomeMushroomIslandShore = modelMap.get("chestPlateBiomeMushroomIslandShore");
        chestPlateLeftBiomeMushroomIslandShore = modelMap.get("chestPlateLeftBiomeMushroomIslandShore");
        chestPlateMiddleBiomeMushroomIslandShore = modelMap.get("chestPlateMiddleBiomeMushroomIslandShore");
        chestPlateRightBiomeMushroomIslandShore = modelMap.get("chestPlateRightBiomeMushroomIslandShore");
        leggingsBiomeMushroomIslandShore = modelMap.get("leggingsBiomeMushroomIslandShore");
        leggingsLeftBiomeMushroomIslandShore = modelMap.get("leggingsLeftBiomeMushroomIslandShore");
        leggingsMiddleBiomeMushroomIslandShore = modelMap.get("leggingsMiddleBiomeMushroomIslandShore");
        leggingsRightBiomeMushroomIslandShore = modelMap.get("leggingsRightBiomeMushroomIslandShore");
        bootsLeftBiomeMushroomIslandShore = modelMap.get("bootsLeftBiomeMushroomIslandShore");
        bootsRightBiomeMushroomIslandShore = modelMap.get("bootsRightBiomeMushroomIslandShore");

        biomeIsMushroomIslandShore = maid.getAtBiomeBiome() === "mushroom_island_shore";

        if (helmetBiomeMushroomIslandShore != undefined) {
            helmetBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (chestPlateBiomeMushroomIslandShore != undefined) {
            chestPlateBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (chestPlateLeftBiomeMushroomIslandShore != undefined) {
            chestPlateLeftBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (chestPlateMiddleBiomeMushroomIslandShore != undefined) {
            chestPlateMiddleBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (chestPlateRightBiomeMushroomIslandShore != undefined) {
            chestPlateRightBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (leggingsBiomeMushroomIslandShore != undefined) {
            leggingsBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (leggingsLeftBiomeMushroomIslandShore != undefined) {
            leggingsLeftBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (leggingsMiddleBiomeMushroomIslandShore != undefined) {
            leggingsMiddleBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (leggingsRightBiomeMushroomIslandShore != undefined) {
            leggingsRightBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (bootsLeftBiomeMushroomIslandShore != undefined) {
            bootsLeftBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
        if (bootsRightBiomeMushroomIslandShore != undefined) {
            bootsRightBiomeMushroomIslandShore.setHidden(!biomeIsMushroomIslandShore);
        }
    }
})

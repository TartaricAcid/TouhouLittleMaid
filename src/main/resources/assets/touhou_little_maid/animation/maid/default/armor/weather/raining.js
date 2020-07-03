Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetWeatherRainging = modelMap.get("helmetWeatherRainging");
        chestPlateWeatherRainging = modelMap.get("chestPlateWeatherRainging");
        chestPlateLeftWeatherRainging = modelMap.get("chestPlateLeftWeatherRainging");
        chestPlateMiddleWeatherRainging = modelMap.get("chestPlateMiddleWeatherRainging");
        chestPlateRightWeatherRainging = modelMap.get("chestPlateRightWeatherRainging");
        leggingsWeatherRainging = modelMap.get("leggingsWeatherRainging");
        leggingsLeftWeatherRainging = modelMap.get("leggingsLeftWeatherRainging");
        leggingsMiddleWeatherRainging = modelMap.get("leggingsMiddleWeatherRainging");
        leggingsRightWeatherRainging = modelMap.get("leggingsRightWeatherRainging");
        bootsLeftWeatherRainging = modelMap.get("bootsLeftWeatherRainging");
        bootsRightWeatherRainging = modelMap.get("bootsRightWeatherRainging");

        weatherIsRainging = maid.getWorld().isRaining()

        if (helmetWeatherRainging != undefined) {
            helmetWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (chestPlateWeatherRainging != undefined) {
            chestPlateWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (chestPlateLeftWeatherRainging != undefined) {
            chestPlateLeftWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (chestPlateMiddleWeatherRainging != undefined) {
            chestPlateMiddleWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (chestPlateRightWeatherRainging != undefined) {
            chestPlateRightWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (leggingsWeatherRainging != undefined) {
            leggingsWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (leggingsLeftWeatherRainging != undefined) {
            leggingsLeftWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (leggingsMiddleWeatherRainging != undefined) {
            leggingsMiddleWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (leggingsRightWeatherRainging != undefined) {
            leggingsRightWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (bootsLeftWeatherRainging != undefined) {
            bootsLeftWeatherRainging.setHidden(!weatherIsRainging);
        }
        if (bootsRightWeatherRainging != undefined) {
            bootsRightWeatherRainging.setHidden(!weatherIsRainging);
        }
    }
})
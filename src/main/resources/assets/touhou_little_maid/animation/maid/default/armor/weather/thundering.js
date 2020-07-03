Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetWeatherThundering = modelMap.get("helmetWeatherThundering");
        chestPlateWeatherThundering = modelMap.get("chestPlateWeatherThundering");
        chestPlateLeftWeatherThundering = modelMap.get("chestPlateLeftWeatherThundering");
        chestPlateMiddleWeatherThundering = modelMap.get("chestPlateMiddleWeatherThundering");
        chestPlateRightWeatherThundering = modelMap.get("chestPlateRightWeatherThundering");
        leggingsWeatherThundering = modelMap.get("leggingsWeatherThundering");
        leggingsLeftWeatherThundering = modelMap.get("leggingsLeftWeatherThundering");
        leggingsMiddleWeatherThundering = modelMap.get("leggingsMiddleWeatherThundering");
        leggingsRightWeatherThundering = modelMap.get("leggingsRightWeatherThundering");
        bootsLeftWeatherThundering = modelMap.get("bootsLeftWeatherThundering");
        bootsRightWeatherThundering = modelMap.get("bootsRightWeatherThundering");

        weatherIsThundering = maid.getWorld().isThundering()

        if (helmetWeatherThundering != undefined) {
            helmetWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (chestPlateWeatherThundering != undefined) {
            chestPlateWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (chestPlateLeftWeatherThundering != undefined) {
            chestPlateLeftWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (chestPlateMiddleWeatherThundering != undefined) {
            chestPlateMiddleWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (chestPlateRightWeatherThundering != undefined) {
            chestPlateRightWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (leggingsWeatherThundering != undefined) {
            leggingsWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (leggingsLeftWeatherThundering != undefined) {
            leggingsLeftWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (leggingsMiddleWeatherThundering != undefined) {
            leggingsMiddleWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (leggingsRightWeatherThundering != undefined) {
            leggingsRightWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (bootsLeftWeatherThundering != undefined) {
            bootsLeftWeatherThundering.setHidden(!weatherIsThundering);
        }
        if (bootsRightWeatherThundering != undefined) {
            bootsRightWeatherThundering.setHidden(!weatherIsThundering);
        }
    }
})
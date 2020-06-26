Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        world = entity.getWorld();
        dayShow = modelMap.get("dayShow");
        nightShow = modelMap.get("nightShow");

        if (dayShow != undefined) {
            dayShow.setHidden(world.isNight());
        }

        if (nightShow != undefined) {
            nightShow.setHidden(world.isDay());
        }
    }
})
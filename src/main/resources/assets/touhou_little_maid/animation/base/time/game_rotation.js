Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        time = entity.getWorld().getWorldTime();
        hourDeg = Math.PI + ((time / 1000) % 12) * (Math.PI / 6);
        minDeg = ((time % 1000) / (50 / 3)) * (Math.PI / 30);

        gameHourRotationX = modelMap.get("gameHourRotationX");
        gameMinuteRotationX = modelMap.get("gameMinuteRotationX");
        gameHourRotationY = modelMap.get("gameHourRotationY");
        gameMinuteRotationY = modelMap.get("gameMinuteRotationY");
        gameHourRotationZ = modelMap.get("gameHourRotationZ");
        gameMinuteRotationZ = modelMap.get("gameMinuteRotationZ");

        if (gameHourRotationX != undefined) {
            gameHourRotationX.setRotateAngleX(hourDeg);
        }

        if (gameMinuteRotationX != undefined) {
            gameMinuteRotationX.setRotateAngleX(minDeg);
        }

        if (gameHourRotationY != undefined) {
            gameHourRotationY.setRotateAngleY(hourDeg);
        }

        if (gameMinuteRotationY != undefined) {
            gameMinuteRotationY.setRotateAngleY(minDeg);
        }

        if (gameHourRotationZ != undefined) {
            gameHourRotationZ.setRotateAngleZ(hourDeg);
        }

        if (gameMinuteRotationZ != undefined) {
            gameMinuteRotationZ.setRotateAngleZ(minDeg);
        }
    }
})
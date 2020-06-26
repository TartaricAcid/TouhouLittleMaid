Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        date = new Date();
        hourDeg = (date.getHours() % 12) * (Math.PI / 6);
        minDeg = date.getMinutes() * (Math.PI / 30);
        secDeg = date.getSeconds() * (Math.PI / 30);

        systemHourRotationX = modelMap.get("systemHourRotationX");
        systemMinuteRotationX = modelMap.get("systemMinuteRotationX");
        systemSecondRotationX = modelMap.get("systemSecondRotationX");
        systemHourRotationY = modelMap.get("systemHourRotationY");
        systemMinuteRotationY = modelMap.get("systemMinuteRotationY");
        systemSecondRotationY = modelMap.get("systemSecondRotationY");
        systemHourRotationZ = modelMap.get("systemHourRotationZ");
        systemMinuteRotationZ = modelMap.get("systemMinuteRotationZ");
        systemSecondRotationZ = modelMap.get("systemSecondRotationZ");

        if (systemHourRotationX != undefined) {
            systemHourRotationX.setRotateAngleX(hourDeg);
        }

        if (systemMinuteRotationX != undefined) {
            systemMinuteRotationX.setRotateAngleX(minDeg);
        }

        if (systemSecondRotationX != undefined) {
            systemSecondRotationX.setRotateAngleX(secDeg);
        }

        if (systemHourRotationY != undefined) {
            systemHourRotationY.setRotateAngleY(hourDeg);
        }

        if (systemMinuteRotationY != undefined) {
            systemMinuteRotationY.setRotateAngleY(minDeg);
        }

        if (systemSecondRotationY != undefined) {
            systemSecondRotationY.setRotateAngleY(secDeg);
        }

        if (systemHourRotationZ != undefined) {
            systemHourRotationZ.setRotateAngleZ(hourDeg);
        }

        if (systemMinuteRotationZ != undefined) {
            systemMinuteRotationZ.setRotateAngleZ(minDeg);
        }

        if (systemSecondRotationZ != undefined) {
            systemSecondRotationZ.setRotateAngleZ(secDeg);
        }
    }
})
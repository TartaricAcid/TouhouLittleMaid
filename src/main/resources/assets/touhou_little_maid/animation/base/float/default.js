Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        sinFloat = modelMap.get("sinFloat");
        cosFloat = modelMap.get("cosFloat");
        negativeSinFloat = modelMap.get("_sinFloat");
        negativeCosFloat = modelMap.get("_cosFloat");

        if (sinFloat != undefined) {
            sinFloat.setOffsetY(Math.sin(ageInTicks * 0.1) * 0.05);
        }
        if (cosFloat != undefined) {
            cosFloat.setOffsetY(Math.cos(ageInTicks * 0.1) * 0.05);
        }
        if (negativeSinFloat != undefined) {
            negativeSinFloat.setOffsetY(-Math.sin(ageInTicks * 0.1) * 0.05);
        }
        if (negativeCosFloat != undefined) {
            negativeCosFloat.setOffsetY(-Math.cos(ageInTicks * 0.1) * 0.05);
        }
    }
})
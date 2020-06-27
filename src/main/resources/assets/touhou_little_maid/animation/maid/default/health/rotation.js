Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        healthRotationX90 = modelMap.get("healthRotationX90");

        if (healthRotationX90 != undefined) {
            deg = (Math.PI / 4) - (Math.PI / 2) * (maid.getHealth() / maid.getMaxHealth());
            healthRotationX90.setRotateAngleX(deg);
        }
    }
})
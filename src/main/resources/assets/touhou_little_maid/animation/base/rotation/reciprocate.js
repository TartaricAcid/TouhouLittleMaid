Java.asJSONCompatible({
    animation: function (entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        xReciprocate = modelMap.get("xReciprocate");
        yReciprocate = modelMap.get("yReciprocate");
        zReciprocate = modelMap.get("zReciprocate");

        if (xReciprocate != undefined) {
            xReciprocate.setRotateAngleX(Math.cos(ageInTicks * 0.3) * 0.2);
        }
        if (yReciprocate != undefined) {
            yReciprocate.setRotateAngleY(Math.cos(ageInTicks * 0.3) * 0.2);
        }
        if (zReciprocate != undefined) {
            zReciprocate.setRotateAngleZ(Math.cos(ageInTicks * 0.3) * 0.2);
        }
    }
})
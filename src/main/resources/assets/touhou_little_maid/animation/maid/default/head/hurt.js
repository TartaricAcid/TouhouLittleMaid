Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        hurtBlink = modelMap.get("hurtBlink");

        if (hurtBlink != undefined) {
            hurtBlink.setHidden(!maid.onHurt());
        }
    }
})
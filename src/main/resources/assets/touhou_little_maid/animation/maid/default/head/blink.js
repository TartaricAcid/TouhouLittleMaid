Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        blink = modelMap.get("blink");

        if (blink != undefined) {
            if (maid.isSleep()) {
                blink.setHidden(false)
                return;
            }
            remainder = ageInTicks % 60;
            blink.setHidden(!(55 < remainder && remainder < 60));
        }
    }
})
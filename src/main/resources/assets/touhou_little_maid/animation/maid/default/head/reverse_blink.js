Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        reverseBlink = modelMap.get("_bink");
        if (reverseBlink != undefined) {
            remainder = ageInTicks % 60;
            blink.setHidden(55 < remainder && remainder < 60);
        }
    }
})
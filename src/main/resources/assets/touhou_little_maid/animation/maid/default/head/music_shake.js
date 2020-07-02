Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");

        if (head != undefined) {
            if (maid.isPortableAudioPlay()) {
                head.setRotateAngleZ(Math.cos(ageInTicks * 0.4) * 0.06)
            }
        }
    }
})
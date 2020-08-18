Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        wingLeft = modelMap.get("wingLeft");
        wingRight = modelMap.get("wingRight");

        if (wingLeft != undefined) {
            wingLeft.setRotateAngleY(-Math.cos(ageInTicks * 0.3) * 0.2 + 1.0);
            if (maid.isSleep()) {
                wingLeft.setHidden(true)
            } else {
                wingLeft.setHidden(false)
            }
        }
        if (wingRight != undefined) {
            wingRight.setRotateAngleY(Math.cos(ageInTicks * 0.3) * 0.2 - 1.0);
            if (maid.isSleep()) {
                wingRight.setHidden(true)
            } else {
                wingRight.setHidden(false)
            }
        }
    }
})
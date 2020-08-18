Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        tail = modelMap.get("tail");

        if (tail != undefined) {
            tail.setRotateAngleX(Math.sin(ageInTicks * 0.2) * 0.05);
            tail.setRotateAngleZ(Math.cos(ageInTicks * 0.2) * 0.1);

            if (maid.isSleep()) {
                tail.setHidden(true)
            } else {
                tail.setHidden(false)
            }
        }
    }
})
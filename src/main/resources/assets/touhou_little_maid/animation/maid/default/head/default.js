Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");
        if (head != undefined) {
            head.setRotateAngleX(headPitch * 0.017453292);
            head.setRotateAngleY(netHeadYaw * 0.017453292);
            if (maid.isSleep()) {
                head.setRotateAngleX(15 * 0.017453292);
            }
        }

        hat = modelMap.get("hat");
        if (hat != undefined) {
            if (maid.isSleep()) {
                hat.setHidden(true)
            } else {
                hat.setHidden(false)
            }
        }
    }
})
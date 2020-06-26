Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        head = modelMap.get("head");
        if (head != undefined) {
            head.setRotateAngleX(headPitch * 0.017453292);
            head.setRotateAngleY(netHeadYaw * 0.017453292);
        }
    }
})
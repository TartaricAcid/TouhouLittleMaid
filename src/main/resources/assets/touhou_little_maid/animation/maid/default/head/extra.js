Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        headExtraA = modelMap.get("headExtraA");
        if (headExtraA != undefined) {
            headExtraA.setRotateAngleX(headPitch * 0.017453292);
            headExtraA.setRotateAngleY(netHeadYaw * 0.017453292);
        }

        headExtraB = modelMap.get("headExtraB");
        if (headExtraB != undefined) {
            headExtraB.setRotateAngleX(headPitch * 0.017453292);
            headExtraB.setRotateAngleY(netHeadYaw * 0.017453292);
        }

        headExtraC = modelMap.get("headExtraC");
        if (headExtraC != undefined) {
            headExtraC.setRotateAngleX(headPitch * 0.017453292);
            headExtraC.setRotateAngleY(netHeadYaw * 0.017453292);
        }
    }
})
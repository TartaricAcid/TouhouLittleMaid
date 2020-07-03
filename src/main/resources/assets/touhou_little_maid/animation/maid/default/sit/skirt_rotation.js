Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        sittingRotationSkirt = modelMap.get("sittingRotationSkirt");
        if (sittingRotationSkirt != undefined) {
            if (maid.isRidingMarisaBroom() || maid.isRiding() || maid.isSitting()) {
                sittingRotationSkirt.setRotateAngleX(-0.567);
            } else {
                sittingRotationSkirt.setRotateAngleX(0);
            }
        }
    }
})
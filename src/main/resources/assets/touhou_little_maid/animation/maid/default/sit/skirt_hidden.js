Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        sittingHiddenSkirt = modelMap.get("sittingHiddenSkirt");
        if (sittingHiddenSkirt != undefined) {
            if (maid.isRidingMarisaBroom() || maid.isRiding() || maid.isSitting()) {
                sittingHiddenSkirt.setHidden(false)
            } else {
                sittingHiddenSkirt.setHidden(true)
            }
        }

        reverseSittingHiddenSkirt = modelMap.get("_sittingHiddenSkirt");
        if (reverseSittingHiddenSkirt != undefined) {
            if (maid.isRidingMarisaBroom() || maid.isRiding() || maid.isSitting()) {
                reverseSittingHiddenSkirt.setHidden(true)
            } else {
                reverseSittingHiddenSkirt.setHidden(false)
            }
        }
    }
})
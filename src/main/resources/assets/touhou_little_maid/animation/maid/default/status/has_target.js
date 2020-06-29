Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        hasTargetShow = modelMap.get("hasTargetShow");
        hasTargetHidden = modelMap.get("hasTargetHidden");

        if (hasTargetShow != undefined) {
            hasTargetShow.setHidden(!maid.hasAttackTarget());
        }
        if (hasTargetHidden != undefined) {
            hasTargetHidden.setHidden(maid.hasAttackTarget());
        }
    }
})
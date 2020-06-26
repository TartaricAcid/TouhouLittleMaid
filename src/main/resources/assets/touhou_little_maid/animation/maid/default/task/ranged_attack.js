Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        rangedAttackHidden = modelMap.get("rangedAttackHidden");

        if (rangedAttackHidden != undefined) {
            if (maid.getTask() === "rangedAttack") {
                rangedAttackHidden.setHidden(true);
            } else {
                rangedAttackHidden.setHidden(false);
            }
        }

        rangedAttackShow = modelMap.get("rangedAttackShow");
        if (rangedAttackShow != undefined) {
            if (maid.getTask() === "rangedAttack") {
                rangedAttackShow.setHidden(false);
            } else {
                rangedAttackShow.setHidden(true);
            }
        }
    }
})

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        healthMoreQuarterShow = modelMap.get("healthMoreQuarterShow");
        healthMoreHalfShow = modelMap.get("healthMoreHalfShow");
        healthMoreThreeQuartersShow = modelMap.get("healthMoreThreeQuartersShow");

        i = maid.getHealth() / maid.getMaxHealth();

        if (healthMoreQuarterShow != undefined) {
            if (i <= 0.25) {
                healthMoreQuarterShow.setHidden(true);
            } else {
                healthMoreQuarterShow.setHidden(false);
            }
        }

        if (healthMoreHalfShow != undefined) {
            if (i <= 0.5) {
                healthMoreHalfShow.setHidden(true);
            } else {
                healthMoreHalfShow.setHidden(false);
            }
        }

        if (healthMoreThreeQuartersShow != undefined) {
            if (i <= 0.75) {
                healthMoreThreeQuartersShow.setHidden(true);
            } else {
                healthMoreThreeQuartersShow.setHidden(false);
            }
        }
    }
})
Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        healthLessQuarterShow = modelMap.get("healthLessQuarterShow");
        healthLessHalfShow = modelMap.get("healthLessHalfShow");
        healthLessThreeQuartersShow = modelMap.get("healthLessThreeQuartersShow");

        i = maid.gethealth() / maid.getMaxHealth();

        if (healthLessQuarterShow != undefined) {
            if (i <= 0.25) {
                healthLessQuarterShow.setHidden(false);
            } else {
                healthLessQuarterShow.setHidden(true);
            }
        }

        if (healthLessHalfShow != undefined) {
            if (i <= 0.5) {
                healthLessHalfShow.setHidden(false);
            } else {
                healthLessHalfShow.setHidden(true);
            }
        }

        if (healthLessThreeQuartersShow != undefined) {
            if (i <= 0.75) {
                healthLessThreeQuartersShow.setHidden(false);
            } else {
                healthLessThreeQuartersShow.setHidden(true);
            }
        }
    }
})
Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        extinguishingHidden = modelMap.get("extinguishingHidden");

        if (extinguishingHidden != undefined) {
            if (maid.getTask() === "extinguishing") {
                extinguishingHidden.setHidden(true);
            } else {
                extinguishingHidden.setHidden(false);
            }
        }

        extinguishingShow = modelMap.get("extinguishingShow");
        if (extinguishingShow != undefined) {
            if (maid.getTask() === "extinguishing") {
                extinguishingShow.setHidden(false);
            } else {
                extinguishingShow.setHidden(true);
            }
        }
    }
})

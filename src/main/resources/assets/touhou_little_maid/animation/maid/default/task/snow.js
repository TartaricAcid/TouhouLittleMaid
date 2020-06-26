Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        snowHidden = modelMap.get("snowHidden");

        if (snowHidden != undefined) {
            if (maid.getTask() === "snow") {
                snowHidden.setHidden(true);
            } else {
                snowHidden.setHidden(false);
            }
        }

        snowShow = modelMap.get("snowShow");
        if (snowShow != undefined) {
            if (maid.getTask() === "snow") {
                snowShow.setHidden(false);
            } else {
                snowShow.setHidden(true);
            }
        }
    }
})

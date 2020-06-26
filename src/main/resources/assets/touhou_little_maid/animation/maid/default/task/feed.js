Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        feedHidden = modelMap.get("feedHidden");

        if (feedHidden != undefined) {
            if (maid.getTask() === "feed") {
                feedHidden.setHidden(true);
            } else {
                feedHidden.setHidden(false);
            }
        }

        feedShow = modelMap.get("feedShow");
        if (feedShow != undefined) {
            if (maid.getTask() === "feed") {
                feedShow.setHidden(false);
            } else {
                feedShow.setHidden(true);
            }
        }
    }
})

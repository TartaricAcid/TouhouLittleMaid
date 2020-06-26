Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        milkHidden = modelMap.get("milkHidden");

        if (milkHidden != undefined) {
            if (maid.getTask() === "milk") {
                milkHidden.setHidden(true);
            } else {
                milkHidden.setHidden(false);
            }
        }

        milkShow = modelMap.get("milkShow");
        if (milkShow != undefined) {
            if (maid.getTask() === "milk") {
                milkShow.setHidden(false);
            } else {
                milkShow.setHidden(true);
            }
        }
    }
})

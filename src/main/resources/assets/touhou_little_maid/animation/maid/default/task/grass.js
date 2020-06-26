Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        grassHidden = modelMap.get("grassHidden");

        if (grassHidden != undefined) {
            if (maid.getTask() === "grass") {
                grassHidden.setHidden(true);
            } else {
                grassHidden.setHidden(false);
            }
        }

        grassShow = modelMap.get("grassShow");
        if (grassShow != undefined) {
            if (maid.getTask() === "grass") {
                grassShow.setHidden(false);
            } else {
                grassShow.setHidden(true);
            }
        }
    }
})

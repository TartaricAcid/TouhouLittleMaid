Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        idleHidden = modelMap.get("idleHidden");

        if (idleHidden != undefined) {
            if (maid.getTask() === "idle") {
                idleHidden.setHidden(true);
            } else {
                idleHidden.setHidden(false);
            }
        }

        idleShow = modelMap.get("idleShow");
        if (idleShow != undefined) {
            if (maid.getTask() === "idle") {
                idleShow.setHidden(false);
            } else {
                idleShow.setHidden(true);
            }
        }
    }
})

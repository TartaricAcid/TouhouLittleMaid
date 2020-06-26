Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        shearsHidden = modelMap.get("shearsHidden");

        if (shearsHidden != undefined) {
            if (maid.getTask() === "shears") {
                shearsHidden.setHidden(true);
            } else {
                shearsHidden.setHidden(false);
            }
        }

        shearsShow = modelMap.get("shearsShow");
        if (shearsShow != undefined) {
            if (maid.getTask() === "shears") {
                shearsShow.setHidden(false);
            } else {
                shearsShow.setHidden(true);
            }
        }
    }
})

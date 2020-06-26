Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        farmHidden = modelMap.get("farmHidden");

        if (farmHidden != undefined) {
            if (maid.getTask() === "farm") {
                farmHidden.setHidden(true);
            } else {
                farmHidden.setHidden(false);
            }
        }

        farmShow = modelMap.get("farmShow");
        if (farmShow != undefined) {
            if (maid.getTask() === "farm") {
                farmShow.setHidden(false);
            } else {
                farmShow.setHidden(true);
            }
        }
    }
})

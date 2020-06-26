Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        cocoaHidden = modelMap.get("cocoaHidden");

        if (cocoaHidden != undefined) {
            if (maid.getTask() === "cocoa") {
                cocoaHidden.setHidden(true);
            } else {
                cocoaHidden.setHidden(false);
            }
        }

        cocoaShow = modelMap.get("cocoaShow");
        if (cocoaShow != undefined) {
            if (maid.getTask() === "cocoa") {
                cocoaShow.setHidden(false);
            } else {
                cocoaShow.setHidden(true);
            }
        }
    }
})

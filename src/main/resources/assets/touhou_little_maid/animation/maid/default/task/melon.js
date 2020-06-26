Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        melonHidden = modelMap.get("melonHidden");

        if (melonHidden != undefined) {
            if (maid.getTask() === "melon") {
                melonHidden.setHidden(true);
            } else {
                melonHidden.setHidden(false);
            }
        }

        melonShow = modelMap.get("melonShow");
        if (melonShow != undefined) {
            if (maid.getTask() === "melon") {
                melonShow.setHidden(false);
            } else {
                melonShow.setHidden(true);
            }
        }
    }
})

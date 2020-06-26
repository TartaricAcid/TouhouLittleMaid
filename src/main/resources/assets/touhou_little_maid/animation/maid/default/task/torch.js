Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        torchHidden = modelMap.get("torchHidden");

        if (torchHidden != undefined) {
            if (maid.getTask() === "torch") {
                torchHidden.setHidden(true);
            } else {
                torchHidden.setHidden(false);
            }
        }

        torchShow = modelMap.get("torchShow");
        if (torchShow != undefined) {
            if (maid.getTask() === "torch") {
                torchShow.setHidden(false);
            } else {
                torchShow.setHidden(true);
            }
        }
    }
})

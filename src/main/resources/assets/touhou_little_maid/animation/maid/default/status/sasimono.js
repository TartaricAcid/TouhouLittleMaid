Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        sasimonoShow = modelMap.get("sasimonoShow");
        sasimonoHidden = modelMap.get("sasimonoHidden");

        if (sasimonoShow != undefined) {
            sasimonoShow.setHidden(!maid.hasSasimono());
        }
        if (sasimonoHidden != undefined) {
            sasimonoHidden.setHidden(maid.hasSasimono());
        }
    }
})
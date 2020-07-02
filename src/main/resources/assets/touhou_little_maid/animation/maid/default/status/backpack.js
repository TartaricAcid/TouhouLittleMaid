Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        backpackShow = modelMap.get("backpackShow");
        backpackHidden = modelMap.get("backpackHidden");

        if (backpackShow != undefined) {
            backpackShow.setHidden(!maid.hasBackpack());
        }
        if (backpackHidden != undefined) {
            backpackHidden.setHidden(maid.hasBackpack());
        }
    }
})
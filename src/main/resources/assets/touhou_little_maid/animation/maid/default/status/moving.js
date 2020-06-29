Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        movingShow = modelMap.get("movingShow");
        movingHidden = modelMap.get("movingHidden");

        if (movingShow != undefined) {
            movingShow.setHidden(limbSwing <= 0);
        }
        if (movingHidden != undefined) {
            movingHidden.setHidden(limbSwing > 0);
        }
    }
})
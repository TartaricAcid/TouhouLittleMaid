Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        attackHidden = modelMap.get("attackHidden");

        if (attackHidden != undefined) {
            if (maid.getTask() === "attack") {
                attackHidden.setHidden(true);
            } else {
                attackHidden.setHidden(false);
            }
        }

        attackShow = modelMap.get("attackShow");
        if (attackShow != undefined) {
            if (maid.getTask() === "attack") {
                attackShow.setHidden(false);
            } else {
                attackShow.setHidden(true);
            }
        }
    }
})

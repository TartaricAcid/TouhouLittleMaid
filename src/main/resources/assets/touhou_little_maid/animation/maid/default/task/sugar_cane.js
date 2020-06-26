Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        sugarCaneHidden = modelMap.get("sugarCaneHidden");

        if (sugarCaneHidden != undefined) {
            if (maid.getTask() === "sugarCane") {
                sugarCaneHidden.setHidden(true);
            } else {
                sugarCaneHidden.setHidden(false);
            }
        }

        sugarCaneShow = modelMap.get("sugarCaneShow");
        if (sugarCaneShow != undefined) {
            if (maid.getTask() === "sugarCane") {
                sugarCaneShow.setHidden(false);
            } else {
                sugarCaneShow.setHidden(true);
            }
        }
    }
})

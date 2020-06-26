Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        danmakuAttackHidden = modelMap.get("danmakuAttackHidden");

        if (danmakuAttackHidden != undefined) {
            if (maid.getTask() === "danmakuAttack") {
                danmakuAttackHidden.setHidden(true);
            } else {
                danmakuAttackHidden.setHidden(false);
            }
        }

        danmakuAttackShow = modelMap.get("danmakuAttackShow");
        if (danmakuAttackShow != undefined) {
            if (maid.getTask() === "danmakuAttack") {
                danmakuAttackShow.setHidden(false);
            } else {
                danmakuAttackShow.setHidden(true);
            }
        }
    }
})

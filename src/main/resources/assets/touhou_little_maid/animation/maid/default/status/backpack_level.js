Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        backpackLevelEmpty = modelMap.get("backpackLevelEmpty");
        backpackLevelSmall = modelMap.get("backpackLevelSmall");
        backpackLevelMiddle = modelMap.get("backpackLevelMiddle");
        backpackLevelBig = modelMap.get("backpackLevelBig");

        level = maid.getBackpackLevel()

        if (backpackLevelEmpty != undefined) {
            backpackLevelEmpty.setHidden(level !== 0);
        }

        if (backpackLevelSmall != undefined) {
            backpackLevelSmall.setHidden(level !== 1);
        }

        if (backpackLevelMiddle != undefined) {
            backpackLevelMiddle.setHidden(level !== 2);
        }

        if (backpackLevelBig != undefined) {
            backpackLevelBig.setHidden(level !== 3);
        }
    }
})
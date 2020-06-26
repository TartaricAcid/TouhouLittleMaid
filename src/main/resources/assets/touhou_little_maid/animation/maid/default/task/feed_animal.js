Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        feedAnimalHidden = modelMap.get("feedAnimalHidden");

        if (feedAnimalHidden != undefined) {
            if (maid.getTask() === "feedAnimal") {
                feedAnimalHidden.setHidden(true);
            } else {
                feedAnimalHidden.setHidden(false);
            }
        }

        feedAnimalShow = modelMap.get("feedAnimalShow");
        if (feedAnimalShow != undefined) {
            if (maid.getTask() === "feedAnimal") {
                feedAnimalShow.setHidden(false);
            } else {
                feedAnimalShow.setHidden(true);
            }
        }
    }
})

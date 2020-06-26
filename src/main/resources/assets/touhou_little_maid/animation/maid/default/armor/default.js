Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmet = modelMap.get("helmet");
        chestPlate = modelMap.get("chestPlate");
        chestPlateLeft = modelMap.get("chestPlateLeft");
        chestPlateMiddle = modelMap.get("chestPlateMiddle");
        chestPlateRight = modelMap.get("chestPlateRight");
        leggings = modelMap.get("leggings");
        leggingsLeft = modelMap.get("leggingsLeft");
        leggingsMiddle = modelMap.get("leggingsMiddle");
        leggingsRight = modelMap.get("leggingsRight");
        bootsLeft = modelMap.get("bootsLeft");
        bootsRight = modelMap.get("bootsRight");

        if (helmet != undefined) {
            helmet.setHidden(!maid.hasHelmet());
        }
        if (chestPlate != undefined) {
            chestPlate.setHidden(!maid.hasChestPlate());
        }
        if (chestPlateLeft != undefined) {
            chestPlateLeft.setHidden(!maid.hasChestPlate());
        }
        if (chestPlateMiddle != undefined) {
            chestPlateMiddle.setHidden(!maid.hasChestPlate());
        }
        if (chestPlateRight != undefined) {
            chestPlateRight.setHidden(!maid.hasChestPlate());
        }
        if (leggings != undefined) {
            leggings.setHidden(!maid.hasLeggings());
        }
        if (leggingsLeft != undefined) {
            leggingsLeft.setHidden(!maid.hasLeggings());
        }
        if (leggingsMiddle != undefined) {
            leggingsMiddle.setHidden(!maid.hasLeggings());
        }
        if (leggingsRight != undefined) {
            leggingsRight.setHidden(!maid.hasLeggings());
        }
        if (bootsLeft != undefined) {
            bootsLeft.setHidden(!maid.hasBoots());
        }
        if (bootsRight != undefined) {
            bootsRight.setHidden(!maid.hasBoots());
        }
    }
})
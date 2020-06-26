Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        reverseHelmet = modelMap.get("_helmet");
        reverseChestPlate = modelMap.get("_chestPlate");
        reverseChestPlateLeft = modelMap.get("_chestPlateLeft");
        reverseChestPlateMiddle = modelMap.get("_chestPlateMiddle");
        reverseChestPlateRight = modelMap.get("_chestPlateRight");
        reverseLeggings = modelMap.get("_leggings");
        reverseLeggingsLeft = modelMap.get("_leggingsLeft");
        reverseLeggingsMiddle = modelMap.get("_leggingsMiddle");
        reverseLeggingsRight = modelMap.get("_leggingsRight");
        reverseBootsLeft = modelMap.get("_bootsLeft");
        reverseBootsRight = modelMap.get("_bootsRight");

        if (reverseHelmet != undefined) {
            reverseHelmet.setHidden(maid.hasHelmet());
        }
        if (reverseChestPlate != undefined) {
            reverseChestPlate.setHidden(maid.hasChestPlate());
        }
        if (reverseChestPlateLeft != undefined) {
            reverseChestPlateLeft.setHidden(maid.hasChestPlate());
        }
        if (reverseChestPlateMiddle != undefined) {
            reverseChestPlateMiddle.setHidden(maid.hasChestPlate());
        }
        if (reverseChestPlateRight != undefined) {
            reverseChestPlateRight.setHidden(maid.hasChestPlate());
        }
        if (reverseLeggings != undefined) {
            reverseLeggings.setHidden(maid.hasLeggings());
        }
        if (reverseLeggingsLeft != undefined) {
            reverseLeggingsLeft.setHidden(maid.hasLeggings());
        }
        if (reverseLeggingsMiddle != undefined) {
            reverseLeggingsMiddle.setHidden(maid.hasLeggings());
        }
        if (reverseLeggingsRight != undefined) {
            reverseLeggingsRight.setHidden(maid.hasLeggings());
        }
        if (reverseBootsLeft != undefined) {
            reverseBootsLeft.setHidden(maid.hasBoots());
        }
        if (reverseBootsRight != undefined) {
            reverseBootsRight.setHidden(maid.hasBoots());
        }
    }
})
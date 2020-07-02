Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetValueHigh = modelMap.get("helmetValueHigh");
        chestPlateValueHigh = modelMap.get("chestPlateValueHigh");
        chestPlateLeftValueHigh = modelMap.get("chestPlateLeftValueHigh");
        chestPlateMiddleValueHigh = modelMap.get("chestPlateMiddleValueHigh");
        chestPlateRightValueHigh = modelMap.get("chestPlateRightValueHigh");
        leggingsValueHigh = modelMap.get("leggingsValueHigh");
        leggingsLeftValueHigh = modelMap.get("leggingsLeftValueHigh");
        leggingsMiddleValueHigh = modelMap.get("leggingsMiddleValueHigh");
        leggingsRightValueHigh = modelMap.get("leggingsRightValueHigh");
        bootsLeftValueHigh = modelMap.get("bootsLeftValueHigh");
        bootsRightValueHigh = modelMap.get("bootsRightValueHigh");

        valueIsHigh = (10 < maid.getArmorValue()) && (maid.getArmorValue() <= 15);

        if (helmetValueHigh != undefined) {
            helmetValueHigh.setHidden(!valueIsHigh);
        }
        if (chestPlateValueHigh != undefined) {
            chestPlateValueHigh.setHidden(!valueIsHigh);
        }
        if (chestPlateLeftValueHigh != undefined) {
            chestPlateLeftValueHigh.setHidden(!valueIsHigh);
        }
        if (chestPlateMiddleValueHigh != undefined) {
            chestPlateMiddleValueHigh.setHidden(!valueIsHigh);
        }
        if (chestPlateRightValueHigh != undefined) {
            chestPlateRightValueHigh.setHidden(!valueIsHigh);
        }
        if (leggingsValueHigh != undefined) {
            leggingsValueHigh.setHidden(!valueIsHigh);
        }
        if (leggingsLeftValueHigh != undefined) {
            leggingsLeftValueHigh.setHidden(!valueIsHigh);
        }
        if (leggingsMiddleValueHigh != undefined) {
            leggingsMiddleValueHigh.setHidden(!valueIsHigh);
        }
        if (leggingsRightValueHigh != undefined) {
            leggingsRightValueHigh.setHidden(!valueIsHigh);
        }
        if (bootsLeftValueHigh != undefined) {
            bootsLeftValueHigh.setHidden(!valueIsHigh);
        }
        if (bootsRightValueHigh != undefined) {
            bootsRightValueHigh.setHidden(!valueIsHigh);
        }
    }
})
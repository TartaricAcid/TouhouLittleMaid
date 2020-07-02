Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetValueLow = modelMap.get("helmetValueLow");
        chestPlateValueLow = modelMap.get("chestPlateValueLow");
        chestPlateLeftValueLow = modelMap.get("chestPlateLeftValueLow");
        chestPlateMiddleValueLow = modelMap.get("chestPlateMiddleValueLow");
        chestPlateRightValueLow = modelMap.get("chestPlateRightValueLow");
        leggingsValueLow = modelMap.get("leggingsValueLow");
        leggingsLeftValueLow = modelMap.get("leggingsLeftValueLow");
        leggingsMiddleValueLow = modelMap.get("leggingsMiddleValueLow");
        leggingsRightValueLow = modelMap.get("leggingsRightValueLow");
        bootsLeftValueLow = modelMap.get("bootsLeftValueLow");
        bootsRightValueLow = modelMap.get("bootsRightValueLow");

        valueIsLow = (0 < maid.getArmorValue()) && (maid.getArmorValue() <= 5);

        if (helmetValueLow != undefined) {
            helmetValueLow.setHidden(!valueIsLow);
        }
        if (chestPlateValueLow != undefined) {
            chestPlateValueLow.setHidden(!valueIsLow);
        }
        if (chestPlateLeftValueLow != undefined) {
            chestPlateLeftValueLow.setHidden(!valueIsLow);
        }
        if (chestPlateMiddleValueLow != undefined) {
            chestPlateMiddleValueLow.setHidden(!valueIsLow);
        }
        if (chestPlateRightValueLow != undefined) {
            chestPlateRightValueLow.setHidden(!valueIsLow);
        }
        if (leggingsValueLow != undefined) {
            leggingsValueLow.setHidden(!valueIsLow);
        }
        if (leggingsLeftValueLow != undefined) {
            leggingsLeftValueLow.setHidden(!valueIsLow);
        }
        if (leggingsMiddleValueLow != undefined) {
            leggingsMiddleValueLow.setHidden(!valueIsLow);
        }
        if (leggingsRightValueLow != undefined) {
            leggingsRightValueLow.setHidden(!valueIsLow);
        }
        if (bootsLeftValueLow != undefined) {
            bootsLeftValueLow.setHidden(!valueIsLow);
        }
        if (bootsRightValueLow != undefined) {
            bootsRightValueLow.setHidden(!valueIsLow);
        }
    }
})
Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetValueFull = modelMap.get("helmetValueFull");
        chestPlateValueFull = modelMap.get("chestPlateValueFull");
        chestPlateLeftValueFull = modelMap.get("chestPlateLeftValueFull");
        chestPlateMiddleValueFull = modelMap.get("chestPlateMiddleValueFull");
        chestPlateRightValueFull = modelMap.get("chestPlateRightValueFull");
        leggingsValueFull = modelMap.get("leggingsValueFull");
        leggingsLeftValueFull = modelMap.get("leggingsLeftValueFull");
        leggingsMiddleValueFull = modelMap.get("leggingsMiddleValueFull");
        leggingsRightValueFull = modelMap.get("leggingsRightValueFull");
        bootsLeftValueFull = modelMap.get("bootsLeftValueFull");
        bootsRightValueFull = modelMap.get("bootsRightValueFull");

        valueIsFull = 15 < maid.getArmorValue();

        if (helmetValueFull != undefined) {
            helmetValueFull.setHidden(!valueIsFull);
        }
        if (chestPlateValueFull != undefined) {
            chestPlateValueFull.setHidden(!valueIsFull);
        }
        if (chestPlateLeftValueFull != undefined) {
            chestPlateLeftValueFull.setHidden(!valueIsFull);
        }
        if (chestPlateMiddleValueFull != undefined) {
            chestPlateMiddleValueFull.setHidden(!valueIsFull);
        }
        if (chestPlateRightValueFull != undefined) {
            chestPlateRightValueFull.setHidden(!valueIsFull);
        }
        if (leggingsValueFull != undefined) {
            leggingsValueFull.setHidden(!valueIsFull);
        }
        if (leggingsLeftValueFull != undefined) {
            leggingsLeftValueFull.setHidden(!valueIsFull);
        }
        if (leggingsMiddleValueFull != undefined) {
            leggingsMiddleValueFull.setHidden(!valueIsFull);
        }
        if (leggingsRightValueFull != undefined) {
            leggingsRightValueFull.setHidden(!valueIsFull);
        }
        if (bootsLeftValueFull != undefined) {
            bootsLeftValueFull.setHidden(!valueIsFull);
        }
        if (bootsRightValueFull != undefined) {
            bootsRightValueFull.setHidden(!valueIsFull);
        }
    }
})
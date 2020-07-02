Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        helmetValueNormal = modelMap.get("helmetValueNormal");
        chestPlateValueNormal = modelMap.get("chestPlateValueNormal");
        chestPlateLeftValueNormal = modelMap.get("chestPlateLeftValueNormal");
        chestPlateMiddleValueNormal = modelMap.get("chestPlateMiddleValueNormal");
        chestPlateRightValueNormal = modelMap.get("chestPlateRightValueNormal");
        leggingsValueNormal = modelMap.get("leggingsValueNormal");
        leggingsLeftValueNormal = modelMap.get("leggingsLeftValueNormal");
        leggingsMiddleValueNormal = modelMap.get("leggingsMiddleValueNormal");
        leggingsRightValueNormal = modelMap.get("leggingsRightValueNormal");
        bootsLeftValueNormal = modelMap.get("bootsLeftValueNormal");
        bootsRightValueNormal = modelMap.get("bootsRightValueNormal");

        valueIsNormal = (5 < maid.getArmorValue()) && (maid.getArmorValue() <= 10);

        if (helmetValueNormal != undefined) {
            helmetValueNormal.setHidden(!valueIsNormal);
        }
        if (chestPlateValueNormal != undefined) {
            chestPlateValueNormal.setHidden(!valueIsNormal);
        }
        if (chestPlateLeftValueNormal != undefined) {
            chestPlateLeftValueNormal.setHidden(!valueIsNormal);
        }
        if (chestPlateMiddleValueNormal != undefined) {
            chestPlateMiddleValueNormal.setHidden(!valueIsNormal);
        }
        if (chestPlateRightValueNormal != undefined) {
            chestPlateRightValueNormal.setHidden(!valueIsNormal);
        }
        if (leggingsValueNormal != undefined) {
            leggingsValueNormal.setHidden(!valueIsNormal);
        }
        if (leggingsLeftValueNormal != undefined) {
            leggingsLeftValueNormal.setHidden(!valueIsNormal);
        }
        if (leggingsMiddleValueNormal != undefined) {
            leggingsMiddleValueNormal.setHidden(!valueIsNormal);
        }
        if (leggingsRightValueNormal != undefined) {
            leggingsRightValueNormal.setHidden(!valueIsNormal);
        }
        if (bootsLeftValueNormal != undefined) {
            bootsLeftValueNormal.setHidden(!valueIsNormal);
        }
        if (bootsRightValueNormal != undefined) {
            bootsRightValueNormal.setHidden(!valueIsNormal);
        }
    }
})
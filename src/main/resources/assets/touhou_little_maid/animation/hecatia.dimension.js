Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        earthHair = modelMap.get("earthHair");
        logoEarth = modelMap.get("logoEarth");
        earthTop = modelMap.get("earthTop");
        earthSideLeft = modelMap.get("earthSideLeft");
        earthSideRight = modelMap.get("earthSideRight");

        moonHair = modelMap.get("moonHair");
        logoMoon = modelMap.get("logoMoon");
        moonTop = modelMap.get("moonTop");
        moonSideLeft = modelMap.get("moonSideLeft");
        moonSideRight = modelMap.get("moonSideRight");

        otherHair = modelMap.get("otherHair");
        logoOther = modelMap.get("logoOther");
        otherTop = modelMap.get("otherTop");
        otherSideLeft = modelMap.get("otherSideLeft");
        otherSideRight = modelMap.get("otherSideRight");

        dim = maid.getDim();
        if (dim == 0) {
            earthHair.setHidden(false);
            logoEarth.setHidden(false);
            earthTop.setHidden(false);
            earthSideLeft.setHidden(true);
            earthSideRight.setHidden(true);

            moonHair.setHidden(true);
            logoMoon.setHidden(true);
            moonTop.setHidden(true);
            moonSideLeft.setHidden(true);
            moonSideRight.setHidden(false);

            otherHair.setHidden(true);
            logoOther.setHidden(true);
            otherTop.setHidden(true);
            otherSideLeft.setHidden(false);
            otherSideRight.setHidden(true);
        } else if (dim == 1) {
            earthHair.setHidden(true);
            logoEarth.setHidden(true);
            earthTop.setHidden(true);
            earthSideLeft.setHidden(false);
            earthSideRight.setHidden(true);

            moonHair.setHidden(false);
            logoMoon.setHidden(false);
            moonTop.setHidden(false);
            moonSideLeft.setHidden(true);
            moonSideRight.setHidden(true);

            otherHair.setHidden(true);
            logoOther.setHidden(true);
            otherTop.setHidden(true);
            otherSideLeft.setHidden(true);
            otherSideRight.setHidden(false);
        } else {
            earthHair.setHidden(true);
            logoEarth.setHidden(true);
            earthTop.setHidden(true);
            earthSideLeft.setHidden(false);
            earthSideRight.setHidden(true);

            moonHair.setHidden(true);
            logoMoon.setHidden(true);
            moonTop.setHidden(true);
            moonSideLeft.setHidden(true);
            moonSideRight.setHidden(false);

            otherHair.setHidden(false);
            logoOther.setHidden(false);
            otherTop.setHidden(false);
            otherSideLeft.setHidden(true);
            otherSideRight.setHidden(true);
        }

        if (maid.hasHelmet()) {
            earthTop.setHidden(true);
            moonTop.setHidden(true);
            otherTop.setHidden(true);
        }
    }
})
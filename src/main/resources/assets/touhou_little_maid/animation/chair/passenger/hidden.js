Java.asJSONCompatible({
    animation: function (chair, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        passengerHidden = modelMap.get("passengerHidden");
        passengerShow = modelMap.get("passengerShow");

        if (passengerHidden != undefined) {
            passengerHidden.setRotateAngleX(chair.hasPassenger())
        }
        if (passengerShow != undefined) {
            passengerShow.setHidden(!chair.hasPassenger())
        }
    }
})
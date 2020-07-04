Java.asJSONCompatible({
    animation: function (chair, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        passengerRotationYaw = modelMap.get("passengerRotationYaw");
        passengerRotationPitch = modelMap.get("passengerRotationPitch");

        if (passengerRotationYaw != undefined) {
            passengerRotationYaw.setRotateAngleY((chair.getPassengerYaw() - chair.getYaw()) * 0.017453292);
        }
        if (passengerRotationPitch != undefined) {
            passengerRotationPitch.setRotateAngleX(chair.getPassengerPitch() * 0.017453292);
        }
    }
})
Java.asJSONCompatible({
    animation: function (chair, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        rotation = modelMap.get("rRotationX10");
        if (rotation != undefined) {
            rotation.setRotateAngleZ(ageInTicks % 360 * 0.017453292);
        }
    }
})
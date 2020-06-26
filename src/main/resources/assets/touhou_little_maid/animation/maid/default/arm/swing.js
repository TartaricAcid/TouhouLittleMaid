Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        armLeft = modelMap.get("armLeft");
        armRight = modelMap.get("armRight");

        if (maid.isSwingingArms()) {
            if (armLeft != undefined) {
                armLeft.setRotateAngleX(-1.396);
                armLeft.setRotateAngleY(0.785);
            }
            if (armRight != undefined) {
                armRight.setRotateAngleX(-1.396);
                armRight.setRotateAngleY(-0.174);
            }
        }
    }
})
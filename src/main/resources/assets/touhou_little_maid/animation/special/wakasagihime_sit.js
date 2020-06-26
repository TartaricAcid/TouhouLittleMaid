var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, modelMap) {
        armLeft = modelMap.get("armLeft");
        armRight = modelMap.get("armRight");

        if (maid.isSitting()) {
            if (armLeft != undefined) {
                armLeft.setRotateAngleX(-0.798);
                armLeft.setRotateAngleZ(0.274);
            }
            if (armRight != undefined) {
                armRight.setRotateAngleX(-0.798);
                armRight.setRotateAngleZ(-0.274);
            }
        }
    }
})
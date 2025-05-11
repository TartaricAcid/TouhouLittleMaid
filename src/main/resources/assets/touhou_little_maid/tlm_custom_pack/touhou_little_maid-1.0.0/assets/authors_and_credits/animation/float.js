var GlWrapper = Java.type("com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper");

Java.asJSONCompatible({
    animation: function (maid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
        headPitch, scale, modelMap) {
        var floatLeg = modelMap.get("floatLeg");
        var left = modelMap.get("left");
        var right = modelMap.get("right");
        var armLeft = modelMap.get("armLeft")
        var armRight = modelMap.get("armRight")

        floatLeg.setRotateAngleX(floatLeg.getInitRotateAngleX() + Math.atan(limbSwingAmount) * 1.25)
        if (maid.isSitting()) {
            armLeft.setRotateAngleX(armLeft.getInitRotateAngleX() - 1)
            armLeft.setRotateAngleY(armLeft.getInitRotateAngleY() + 0.75)
            armRight.setRotateAngleX(armRight.getInitRotateAngleX() - 0.5)
            armRight.setRotateAngleY(armRight.getInitRotateAngleY() - 0.25)
            armRight.setRotateAngleZ(armRight.getInitRotateAngleZ() + 0.25)
            left.setRotateAngleZ(left.getInitRotateAngleZ() + 0.25)
            left.setRotateAngleX(left.getInitRotateAngleX() + 0.3)
            right.setRotateAngleZ(right.getInitRotateAngleZ() - 0.5)
        } else {
            left.setRotateAngleX(left.getInitRotateAngleX())
            left.setRotateAngleZ(left.getInitRotateAngleZ() + Math.atan(limbSwingAmount) * 0.75)
            right.setRotateAngleZ(right.getInitRotateAngleZ() - Math.atan(limbSwingAmount) * 0.75)
        }
        if (!maid.isSleep()) {
            GlWrapper.translate(0, 0.1 * Math.sin(ageInTicks * 0.075) - 0.1, 0);
        } else {
            GlWrapper.translate(0, 0.5, 0);
        }
    }
})